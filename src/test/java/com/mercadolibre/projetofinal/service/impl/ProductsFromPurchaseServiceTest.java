package com.mercadolibre.projetofinal.service.impl;

import com.mercadolibre.projetofinal.dtos.AccountDTO;
import com.mercadolibre.projetofinal.dtos.request.ProductsRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseOrderRequestDTO;
import com.mercadolibre.projetofinal.dtos.request.PurchaseRequestDTO;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductsFromPurchaseServiceTest {

    @InjectMocks private ProductsFromPurchaseService service;
    @Mock private ProductService productService;
    @Mock private BatchProductService batchProductService;

    private final static String PRODUCT_ID = "3";

    @Test
    void shouldReturnExceptionWhenThereAreProductInRequestDifferentFromDatabase(){
        //Mock
        var accountDTO = AccountDTO.builder().country(0).build();
        var productsRequestDTO = ProductsRequestDTO.builder().productId(PRODUCT_ID).build();
        List<ProductsRequestDTO> productsRequestDTOList = Collections.singletonList(productsRequestDTO);
        var purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().products(productsRequestDTOList).build();
        var purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        Mockito.when(productService.findAllByIdAndCountry(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        //Method call
        assertThrows(NotFoundException.class, () ->
            service.getProductsFromRequestByAccount(purchaseRequestDTO, accountDTO)
        );

    }

    @Test
    void shouldReturnProductsWhenProductInRequestIsEqualsFromDatabase(){
        //Mock
        var accountDTO = AccountDTO.builder().country(0).build();
        var productsRequestDTO = ProductsRequestDTO.builder().productId(PRODUCT_ID).build();
        List<ProductsRequestDTO> productsRequestDTOList = Collections.singletonList(productsRequestDTO);
        var purchaseOrderRequestDTO = PurchaseOrderRequestDTO.builder().products(productsRequestDTOList).build();
        var purchaseRequestDTO = PurchaseRequestDTO.builder().purchaseOrder(purchaseOrderRequestDTO).build();

        Mockito.when(productService.findAllByIdAndCountry(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(new Product(PRODUCT_ID)));

        //Method call
        List<Product> responses = service.getProductsFromRequestByAccount(purchaseRequestDTO, accountDTO);

        //Assert
        assertEquals(1, responses.size());

        //Verify
        Mockito.verify(productService).findAllByIdAndCountry(Mockito.any(), Mockito.any());
    }
}