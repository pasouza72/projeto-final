package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.BatchProductRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.response.ListBatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductStockCountResponseDTO;
import com.mercadolibre.projetofinal.enums.SectionCategoryEnum;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.exceptions.ProductsOutOfStockException;
import com.mercadolibre.projetofinal.model.*;
import com.mercadolibre.projetofinal.repository.BatchProductRepository;
import com.mercadolibre.projetofinal.service.IBatchProductService;
import com.mercadolibre.projetofinal.service.IProductService;
import com.mercadolibre.projetofinal.service.IWarehouseService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.*;

@Service
public class BatchProductService implements IBatchProductService {

    private final BatchProductRepository batchProductRepository;
    private final IProductService productService;
    private final IWarehouseService warehouseService;
    private final JwtUtil jwtUtil;


    public BatchProductService(BatchProductRepository batchProductRepository, IProductService productService,
                               @Lazy IWarehouseService warehouseService, JwtUtil jwtUtil) {
        this.batchProductRepository = batchProductRepository;
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public List<BatchProduct> convertToModel(List<BatchProductRequestDTO> productsDTO, WarehousesSections warehousesSections) {

        Map<String, Product> mapProducts = new HashMap<>();
        List<Product> products = productService.findAllById(productsDTO.stream()
                                                            .map(BatchProductRequestDTO::getProductId)
                                                            .collect(Collectors.toList()));
        if (products.size() != productsDTO.stream().map(BatchProductRequestDTO::getProductId)
                .collect(Collectors.toSet()).size()){
            throw new NotFoundException(ML001);
        }

        products.forEach( el -> mapProducts.put(el.getId(), el));

        return productsDTO.stream()
                .map(el -> el.toModel(mapProducts.get(el.getProductId()), warehousesSections))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BatchProduct> findBatchProductsByProductId(String productId, Warehouse warehouse, PageRequest pageRequest) {
        return batchProductRepository.
                findBatchesInWarehouseByProductId(
                        warehouse.getId(),productId, pageRequest);
    }

    @Override
    public ListBatchStockSimplifiedResponseDTO findBatchesForRepresentative(String productId, String token, PageRequest pageRequest) {
        AccountDTO accountDTO = jwtUtil.getAccountDTO(token);
        String warehouseId = accountDTO.getWarehouse();
        Warehouse warehouse = warehouseService.findWarehouseById(warehouseId);
        Page<BatchProduct> batchProducts = findBatchProductsByProductId(productId, warehouse, pageRequest);
        if (batchProducts.getTotalElements() == 0){
            throw new NotFoundException(ML601);
        }
        Section section = null;
        try {
             section = batchProducts.getContent().get(0).getWarehousesSections().getSection();
        } catch (IndexOutOfBoundsException e){
            throw new NotFoundException(ML1009);
        }
        return new ListBatchStockSimplifiedResponseDTO(batchProducts,warehouse, section);
    }

    @Override
    public Page<BatchProduct> findBatchesByRepresentativeAndDueDate(String token, Integer days, PageRequest pageRequest) {
        AccountDTO accountDTO = jwtUtil.getAccountDTO(token);
        String warehouseId = accountDTO.getWarehouse();

        return batchProductRepository.findByWarehouseIdAndDueDate(warehouseId, days, pageRequest);
    }

    @Override
    public Page<BatchProduct> findBatchesByRepresentativeAndDueDateAndCategory(String token, Integer days, String category, PageRequest pageRequest) {
        AccountDTO accountDTO = jwtUtil.getAccountDTO(token);
        String warehouseId = accountDTO.getWarehouse();
        SectionCategoryEnum sectionCategoryEnum = SectionCategoryEnum.of(category);

        return batchProductRepository.findByWarehouseIdAndDueDateAndCategory(warehouseId, days, sectionCategoryEnum.getId(), pageRequest);
    }

    public int getQuantityOfBatchAvailableByCountryAndProductId(Integer userCountry, String productId){
        List<BatchProduct> batchProducts = batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry)
                                                .orElseGet(ArrayList::new);

        if(batchProducts.isEmpty()){
            throw new NotFoundException(ML000);
        }

        return batchProducts.stream().mapToInt(BatchProduct::getCurrentQuantity).sum();
    }

    public void removeProductFromBatch(String productId, Integer quantityOfProduct, Integer userCountry) {
        List<BatchProduct> batchProducts = batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry)
                .orElseGet(ArrayList::new);

        var i = 0;
        while (quantityOfProduct > 0 && i < batchProducts.size()){
            var batchProduct = batchProducts.get(i);
            Integer currentQuantity = batchProduct.getCurrentQuantity();

            if(quantityOfProduct > currentQuantity){
                batchProduct.setCurrentQuantity( 0 );
                quantityOfProduct -= currentQuantity;
            }else{
                batchProduct.setCurrentQuantity( currentQuantity - quantityOfProduct );
                quantityOfProduct = 0;
            }
            i++;
        }
        batchProductRepository.saveAll(batchProducts);
    }

    public void incrementProductOnBatch(String productId, Integer quantityOfProduct, Integer userCountry) {
        List<BatchProduct> batchProducts = batchProductRepository.findAllBatchByProductAndCountry(productId, userCountry).orElseGet(ArrayList::new);

        var batchProduct = batchProducts.get(0);
        batchProduct.setCurrentQuantity(batchProduct.getCurrentQuantity() + quantityOfProduct);

        batchProductRepository.save(batchProduct);
    }

    @Override
    public void validStockOfProductsByCountry(List<Product> products, List<ProductsRequestDTO> productsRequest, String username) {
        List<ProductStockCountResponseDTO> currentProductStock = batchProductRepository
                .getStockProductsByUsername(username, products.stream().map(Product::getId).collect(Collectors.toList()));
        Map<String, Integer> map = new HashMap<>();
        productsRequest.forEach(p -> map.put(p.getProductId(), p.getQuantity()));
        validAllBatchProductsOutOfStock(currentProductStock, map);
    }

    private void validAllBatchProductsOutOfStock(List<ProductStockCountResponseDTO> currentProductStock, Map<String, Integer> map) {
        List<String> productsOutOfStock = new ArrayList<>();
        currentProductStock.forEach(p -> {
            Integer quantityOfProductRequest = map.get(p.getProductId());
            if(quantityOfProductRequest > p.getCurrentQuantity()){
                productsOutOfStock.add(p.getProductId());
            }
        } );
        if(productsOutOfStock.size()>0){
            throw new ProductsOutOfStockException(ML002, productsOutOfStock);
        }
    }

    @Override
    public void updateStock(List<ProductsRequestDTO> products, Integer country) {
        List<String> productIds = products.stream().map(ProductsRequestDTO::getProductId).collect(Collectors.toList());
        Map<String, Integer> map = new HashMap<>();
        products.forEach(p -> map.put(p.getProductId(), p.getQuantity()));
        List<BatchProduct> batchProducts = batchProductRepository.findAllBatchByProductsAndCountry(productIds, country)
                .orElseGet(ArrayList::new);
        saveAllNewBatchProducts(map, batchProducts);
    }

    private void saveAllNewBatchProducts(Map<String, Integer> map, List<BatchProduct> batchProducts) {
        batchProducts.forEach(b -> {
            var product = b.getProduct();
            Integer productQuantityRequest = map.get(product.getId());
            Integer currentQuantity = b.getCurrentQuantity();
            if(productQuantityRequest <= currentQuantity){
                b.setCurrentQuantity( currentQuantity - productQuantityRequest);
                map.put(product.getId(), 0);
            }else{
                b.setCurrentQuantity(0);
                map.put(product.getId(), (productQuantityRequest - currentQuantity) );
            }
        });
        batchProductRepository.saveAll(batchProducts);
    }
}
