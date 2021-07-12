package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.ProductDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseOrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseValueResponseDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.factory.PurchaseOrderFactory;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.PurchaseOrder;
import com.mercadolibre.projetofinal.model.PurchaseOrdersProducts;
import com.mercadolibre.projetofinal.repository.PurchaseOrderRepository;
import com.mercadolibre.projetofinal.service.IPurchaseOrderService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML401;

@Service
@AllArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrdersProductsService purchaseOrdersProductsService;
    private final BatchProductService batchProductService;
    private final JwtUtil jwtUtil;
    private final ProductsFromPurchaseService productsFromPurchaseService;
    private final ValidStockProductService validProductsStock;

    @Override
    public PurchaseValueResponseDTO create(PurchaseRequestDTO purchaseRequestDTO, String token) {
        var account = jwtUtil.getAccountDTO(token);
        List<Product> products = productsFromPurchaseService.getProductsFromRequestByAccount(purchaseRequestDTO, account);
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = createPurchaseOrder(purchaseRequestDTO, account, products);

        double totalPriceCalculated = purchaseOrdersProductsList.stream()
                                                                .mapToDouble(PurchaseOrdersProducts::priceCalculated)
                                                                .sum();

        return new PurchaseValueResponseDTO(totalPriceCalculated);
    }

    @Override
    public List<PurchaseOrderProductResponseDTO> getProductsOfPurchase(String orderId) {
        var purchaseOrder = this.getPurchaseOrderById(orderId);

        return purchaseOrder.getPurchaseOrdersProducts().stream()
                            .map(PurchaseOrderFactory::toPurchaseOrderProductResponseDTO)
                            .collect(Collectors.toList());
    }

    public PurchaseOrderResponseDTO update(PurchaseRequestDTO purchaseRequestDTO, String token){
        var purchaseOrderRequestDTO = purchaseRequestDTO.getPurchaseOrder();

        var purchaseOrder = getPurchaseOrderById(purchaseOrderRequestDTO.getId());

        List<PurchaseOrdersProducts> purchaseOrdersProducts = purchaseOrder.getPurchaseOrdersProducts();

        List<String> productsIdFromRequest = purchaseOrderRequestDTO.getProducts()
                                                                    .stream()
                                                                    .map(ProductsRequestDTO::getProductId)
                                                                    .collect(Collectors.toList());

        Map<String, ProductDTO> map = getMapOfOldProductsOrder(purchaseOrdersProducts, productsIdFromRequest);

        updateNewProductsOrderMap(purchaseOrderRequestDTO, map);

        Integer userCountry = jwtUtil.getUserCountry(token);
        List<PurchaseOrdersProducts> purchaseOrdersProductsList = updateOrderInDatabase(userCountry, purchaseOrder, map);

        return PurchaseOrderFactory.toPurchaseOrderResponseDTO(purchaseOrderRequestDTO, purchaseOrdersProductsList);
    }

    private List<PurchaseOrdersProducts> createPurchaseOrder(PurchaseRequestDTO purchaseRequestDTO, AccountDTO accountDTO,
                                                             List<Product> products) {
        var purchaseOrderRequestDTO = purchaseRequestDTO.getPurchaseOrder();

        var purchaseOrderToSave = PurchaseOrderFactory.toPurchaseOrder(purchaseOrderRequestDTO, accountDTO.getId());
        var newPurchaseOrder = purchaseOrderRepository.save(purchaseOrderToSave);

        List<PurchaseOrdersProducts> purchaseOrdersProductsList =
                purchaseOrdersProductsService.save(products,newPurchaseOrder,purchaseOrderRequestDTO.getProducts());

        batchProductService.updateStock(purchaseOrderRequestDTO.getProducts(), accountDTO.getCountry());

        return purchaseOrdersProductsList;
    }

    private List<PurchaseOrdersProducts> updateOrderInDatabase(Integer userCountry, PurchaseOrder purchaseOrder,
                                                               Map<String, ProductDTO> map) {
        List<ProductsRequestDTO> productsRequestDTOList = new ArrayList<>();

        List<String> productsId = updateNewProductsOrderList(map, productsRequestDTOList);

        updateBatchProductsInDatabase(userCountry, map, productsId);

        return savePurchaseOrdersProducts(purchaseOrder, productsRequestDTOList);
    }

    private List<PurchaseOrdersProducts> savePurchaseOrdersProducts(PurchaseOrder purchaseOrder,
                                                                    List<ProductsRequestDTO> productsRequestDTOList) {
        purchaseOrder.getPurchaseOrdersProducts().clear();

        purchaseOrderRepository.save(purchaseOrder);

        List<Product> products = productsRequestDTOList.stream().map(p -> new Product(p.getProductId()))
                .collect(Collectors.toList());

        return purchaseOrdersProductsService.save(products, purchaseOrder, productsRequestDTOList);
    }

    private void updateBatchProductsInDatabase(Integer userCountry, Map<String, ProductDTO> map, List<String> productsId) {
        validProductsStock.hasStockOfProducts(userCountry, map, productsId);

        productsId.forEach(p -> {
            Integer newValue = map.get(p).getNewValue();
            Integer oldValue = map.get(p).getOldValue();
            if(newValue > oldValue){
                batchProductService.removeProductFromBatch(p, newValue - oldValue, userCountry);
            }else{
                batchProductService.incrementProductOnBatch(p, oldValue - newValue, userCountry);
            }
        });
    }

    private List<String> updateNewProductsOrderList(Map<String, ProductDTO> map, List<ProductsRequestDTO> productsRequestDTOList) {
        List<String> productsId = new ArrayList<>();

        for ( String key : map.keySet() ) {
            productsId.add(key);
            var productDTO = map.get(key);

            if(!productDTO.isToRemoveFromPurchase()){
                var productsRequestDTO = new ProductsRequestDTO(key, productDTO.getNewValue());
                productsRequestDTOList.add(productsRequestDTO);
            }
        }

        return productsId;
    }

    private void updateNewProductsOrderMap(PurchaseOrderRequestDTO purchaseOrderRequestDTO, Map<String, ProductDTO> map) {
        purchaseOrderRequestDTO.getProducts().forEach(p -> {
            var productDTO = map.get(p.getProductId());
            if(productDTO != null){
                productDTO.setNewValue(productDTO.getNewValue() + p.getQuantity());
            }else{
                productDTO = new ProductDTO(p.getProductId(), 0, p.getQuantity(), false);
            }
            map.put(p.getProductId(), productDTO);
        });
    }

    private Map<String, ProductDTO> getMapOfOldProductsOrder(List<PurchaseOrdersProducts> purchaseOrdersProducts,
                                                             List<String> productsIdFromRequest) {
        Map<String, ProductDTO> map = new HashMap<>();

        purchaseOrdersProducts.forEach(p -> {
            ProductDTO productDTO;
            if(productsIdFromRequest.contains(p.getProduct().getId())) {
                productDTO = new ProductDTO(p.getProduct().getId(), p.getQuantity(), 0, false);
            }else{
                productDTO = new ProductDTO(p.getProduct().getId(), p.getQuantity(), 0, true);
            }
            map.put(productDTO.getId(), productDTO);
        });

        return map;
    }

    private PurchaseOrder getPurchaseOrderById(String orderId) {
        return purchaseOrderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(ML401));
    }
}
