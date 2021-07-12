package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.BatchProductController;
import com.mercadolibre.projetofinal.dtos.response.BatchStockArrayDueDateResponse;
import com.mercadolibre.projetofinal.dtos.response.BatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.ListBatchStockSimplifiedResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.SectionResponseDTO;
import com.mercadolibre.projetofinal.service.impl.BatchProductService;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mercadolibre.projetofinal.util.CreateTestEntities.newBatchProducts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatchProductControllerTest {

    @Mock
    BatchProductService service;

    @InjectMocks
    BatchProductController controller;

    @Test
    void testFindBatchProductsWork(){
        //given
        List<BatchStockSimplifiedResponseDTO> batchDtos = new ArrayList<>(List.of(
                new BatchStockSimplifiedResponseDTO("batch1",1, LocalDate.now()),
                new BatchStockSimplifiedResponseDTO("batch2",2, LocalDate.now())
        ));
        ListBatchStockSimplifiedResponseDTO response = new ListBatchStockSimplifiedResponseDTO(
                new SectionResponseDTO("section","warehouse"),"product1",batchDtos,1,1,0);

        //when
        when(service.findBatchesForRepresentative(any(), any(), any())).thenReturn(response);
        ResponseEntity<ListBatchStockSimplifiedResponseDTO> responseEntity = controller.
                findBatchProducts("token", "product1",0,1,"currentQuantity","ASC");

        //assert
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody().getBatchStock().get(0).getBatchId(),"batch1");
    }

    @Test
    void findBatchesProductsByDueDate() {
        Page page = new PageImpl(newBatchProducts, PageRequest.of(0, 1), 50);
        when(service.findBatchesByRepresentativeAndDueDate(any(), any(), any()))
                .thenReturn(page);

        BatchStockArrayDueDateResponse batchStockArrayDueDateResponse = new BatchStockArrayDueDateResponse(page);
        ResponseEntity<BatchStockArrayDueDateResponse> response = controller
                .findBatchesProductsByDueDate("", 0, 0, 1, "ASC");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(batchStockArrayDueDateResponse, response.getBody());
    }

    @Test
    void findBatchesProductsByDueDateAndCategory() {
        Page page = new PageImpl(newBatchProducts, PageRequest.of(0, 1), 50);
        when(service.findBatchesByRepresentativeAndDueDateAndCategory(any(), any(), any(), any()))
                .thenReturn(page);

        BatchStockArrayDueDateResponse batchStockArrayDueDateResponse = new BatchStockArrayDueDateResponse(page);
        ResponseEntity<BatchStockArrayDueDateResponse> response = controller
                .findBatchesProductsByDueDateAndCategory("", 0, "", 0, 1, "ASC");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }
}
