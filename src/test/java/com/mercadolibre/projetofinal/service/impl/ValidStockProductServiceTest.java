package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.ProductDTO;
import com.mercadolibre.projetofinal.exceptions.ProductsOutOfStockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidStockProductServiceTest {

    @InjectMocks private ValidStockProductService service;
    @Mock private BatchProductService batchProductService;

    private final static Integer USER_COUNTRY = 2;
    private final static Integer OLD_VALUE = 20;
    private final static Integer NEW_VALUE = 10;
    private final static String PRODUCT_ID = "5";

    @Test
    void shouldPassWhenHasStockOfProductsAndNewValueIsLessThanOldValue(){
        //Mock
        ProductDTO productDTO = ProductDTO.builder()
                                            .id(PRODUCT_ID)
                                            .newValue(NEW_VALUE)
                                            .oldValue(OLD_VALUE)
                                            .build();
        Map<String, ProductDTO> map = new HashMap<>();
        map.put(PRODUCT_ID, productDTO);

        service.hasStockOfProducts(USER_COUNTRY, map, Collections.singletonList(PRODUCT_ID));

        //verify
        Mockito.verify(batchProductService).getQuantityOfBatchAvailableByCountryAndProductId(USER_COUNTRY, PRODUCT_ID);
    }

    @Test
    void shouldPassWhenHasStockOfProductsAndOldValueIsLessThanNewValue(){
        //Mock
        ProductDTO productDTO = ProductDTO.builder()
                .id(PRODUCT_ID)
                .newValue(NEW_VALUE + 20)
                .oldValue(OLD_VALUE)
                .build();
        Map<String, ProductDTO> map = new HashMap<>();
        map.put(PRODUCT_ID, productDTO);

        Mockito.when(batchProductService.getQuantityOfBatchAvailableByCountryAndProductId(USER_COUNTRY, PRODUCT_ID))
                .thenReturn(35);

        service.hasStockOfProducts(USER_COUNTRY, map, Collections.singletonList(PRODUCT_ID));

        //verify
        Mockito.verify(batchProductService).getQuantityOfBatchAvailableByCountryAndProductId(USER_COUNTRY, PRODUCT_ID);
    }

    @Test
    void shouldReturnExceptionWhenHasNotStockOfProducts(){
        //Mock
        ProductDTO productDTO = ProductDTO.builder()
                .id(PRODUCT_ID)
                .newValue(NEW_VALUE + 20)
                .oldValue(OLD_VALUE)
                .build();
        Map<String, ProductDTO> map = new HashMap<>();
        map.put(PRODUCT_ID, productDTO);

        assertThrows(ProductsOutOfStockException.class, () ->
                    service.hasStockOfProducts(USER_COUNTRY, map, Collections.singletonList(PRODUCT_ID))
                );

    }
}