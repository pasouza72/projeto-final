package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.InsertOrderController;
import com.mercadolibre.projetofinal.dtos.response.BatchProductResponseDTO;
import com.mercadolibre.projetofinal.service.impl.InsertOrderService;
import com.mercadolibre.projetofinal.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mercadolibre.projetofinal.util.CreateTestEntities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InsertOrderControllerTest{

    @Mock
    InsertOrderService service;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    InsertOrderController controller;

    @Test
    void testCreateOrderWorks() throws Exception {

        String nowDate = LocalDate.now().toString();
        String  nowDateTime = LocalDateTime.now().toString();
        //given
        List<BatchProductResponseDTO> response = new ArrayList(List.of(
                new BatchProductResponseDTO(1,1.0,1.0,1,1, nowDate, nowDateTime, nowDate)
        ));

        //when
        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(service.createOrder(any(),any())).thenReturn(response);

        ResponseEntity<List<BatchProductResponseDTO>> responseEntity = controller.create(representativeToken, orderRequest);

        //when
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertEquals(responseEntity.getBody().size(), 1);
        assertEquals(responseEntity.getBody().get(0).getBatchNumber(),1);
    }

    @Test
    void testUpdateOrderWorks() throws Exception {

        String nowDate = LocalDate.now().toString();
        String  nowDateTime = LocalDateTime.now().toString();
        //given
        List<BatchProductResponseDTO> response = new ArrayList(List.of(
                new BatchProductResponseDTO(1,1.0,1.0,1,1, nowDate, nowDateTime, nowDate)
        ));

        //when
        when(jwtUtil.getAccountDTO(any())).thenReturn(accountDTO);
        when(service.updateOrder(any(),any())).thenReturn(response);

        ResponseEntity<List<BatchProductResponseDTO>> responseEntity = controller.update(representativeToken, orderRequest);

        //when
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertEquals(responseEntity.getBody().size(), 1);
        assertEquals(responseEntity.getBody().get(0).getBatchNumber(),1);
    }
}
