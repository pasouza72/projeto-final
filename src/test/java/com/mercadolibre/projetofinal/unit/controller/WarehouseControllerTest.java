package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.WarehouseController;
import com.mercadolibre.projetofinal.dtos.WarehouseDTO;
import com.mercadolibre.projetofinal.dtos.response.ProductCountListDTO;
import com.mercadolibre.projetofinal.dtos.response.WarehouseResponseDTO;
import com.mercadolibre.projetofinal.service.impl.WarehouseService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarehouseControllerTest {

    @Mock
    WarehouseService warehouseService;
    
    @InjectMocks
    WarehouseController controller;


    @Test
    void createFailedTest(){
        when(warehouseService.create(any())).thenReturn(null);
        ResponseEntity<WarehouseResponseDTO> responseEntity = controller.create("token", null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /*@Test
    void createTest(){
        WarehouseDTO warehouseDTO = new WarehouseDTO(1, "name", "country", List.of());
        when(warehouseService.create(any())).thenReturn(warehouseDTO);
        ResponseEntity<WarehouseResponseDTO> responseEntity = controller.create("token", warehouseDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }*/

    @Test
    void getProductCountByWarehouseTest(){
        when(warehouseService.getProductCountByWarehouse(any())).thenReturn(new ProductCountListDTO("id", List.of()));
        ResponseEntity<ProductCountListDTO> listDTOResponseEntity = controller.getProductCountByWarehouse("id");
        assertEquals(HttpStatus.OK, listDTOResponseEntity.getStatusCode());
    }


}
