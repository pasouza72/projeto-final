package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.ProductDTO;
import com.mercadolibre.projetofinal.exceptions.ProductsOutOfStockException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mercadolibre.projetofinal.enums.ErrorsEnum.ML002;

@AllArgsConstructor
@Service
public class ValidStockProductService {

    private final BatchProductService batchProductService;

    public void hasStockOfProducts(Integer userCountry, Map<String, ProductDTO> map, List<String> productsId) {
        List<String> productsOutOfStock = new ArrayList<>();

        productsId.forEach(p -> {
            int quantityOfBatchAvailable = batchProductService.getQuantityOfBatchAvailableByCountryAndProductId(userCountry, p);
            Integer newValue = map.get(p).getNewValue();
            Integer oldValue = map.get(p).getOldValue();
            if(newValue > oldValue && quantityOfBatchAvailable < newValue ){
                productsOutOfStock.add(p);
            }
        });

        if(!productsOutOfStock.isEmpty()){
            throw new ProductsOutOfStockException(ML002, productsOutOfStock);
        }
    }

}
