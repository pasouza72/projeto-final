package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML000;

@AllArgsConstructor
@Service
public class ProductsFromPurchaseService {

    private final ProductService productService;
    private final BatchProductService batchProductService;

    public List<Product> getProductsFromRequestByAccount(PurchaseRequestDTO purchaseRequestDTO, AccountDTO accountDTO) {
        List<String> productsId = getProductsIdList(purchaseRequestDTO);
        List<Product> products = productService.findAllByIdAndCountry(productsId, accountDTO.getCountry());

        if(products.size() != productsId.size()){
            throw new NotFoundException(ML000);
        }

        batchProductService.validStockOfProductsByCountry(products, purchaseRequestDTO.getPurchaseOrder().getProducts(),
                accountDTO.getUsername());

        return products;
    }

    private List<String> getProductsIdList(PurchaseRequestDTO purchaseRequestDTO) {
        return purchaseRequestDTO.getPurchaseOrder()
                                    .getProducts()
                                    .stream()
                                    .map(ProductsRequestDTO::getProductId)
                                    .collect(Collectors.toList());
    }
}
