package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.ProductController;
import com.mercadolibre.projetofinal.dtos.response.ProductResponseResponseDTO;
import com.mercadolibre.projetofinal.exceptions.ApiException;
import com.mercadolibre.projetofinal.exceptions.NotFoundException;
import com.mercadolibre.projetofinal.service.impl.ProductService;
import com.mercadolibre.projetofinal.service.impl.WarehouseService;
import com.mercadolibre.projetofinal.util.CreateTestEntities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController controller;

    @Test
    void getAllProducts() {

        when(productService.getAllProductsByCountry(any())).thenReturn(CreateTestEntities.products);

        ResponseEntity<List<ProductResponseResponseDTO>> responseResponseDTOList = controller.getAllProducts("123213");

        assertEquals(HttpStatus.OK, responseResponseDTOList.getStatusCode());

    }

    @Test
    void getProductsByCategory() {
        when(productService.getAllProductsByCategory(any(), any())).thenReturn(CreateTestEntities.products);

        ResponseEntity<List<ProductResponseResponseDTO>> listResponseEntity = controller.getProductsByCategory("my-token", "FF");

        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
    }
}
