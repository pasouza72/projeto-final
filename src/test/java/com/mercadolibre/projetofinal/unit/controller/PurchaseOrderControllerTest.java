package com.mercadolibre.projetofinal.unit.controller;

import com.mercadolibre.projetofinal.controller.PurchaseOrderController;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderProductResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseOrderResponseDTO;
import com.mercadolibre.projetofinal.dtos.response.PurchaseValueResponseDTO;
import com.mercadolibre.projetofinal.service.impl.PurchaseOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.purchaseRequestDTO;
import static com.mercadolibre.projetofinal.util.CreateTestEntities.purchaseOrderResponseDTO;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderControllerTest {

    @Mock
    PurchaseOrderService purchaseOrderService;

    @InjectMocks
    PurchaseOrderController purchaseOrderController;

    @Test
    void getAllPurchaseOrderProducts() {
        List<PurchaseOrderProductResponseDTO> purchaseOrderProductResponseDTOS = List.of(
                new PurchaseOrderProductResponseDTO("productId", "productName", 100));

        when(purchaseOrderService.getProductsOfPurchase(any())).thenReturn(purchaseOrderProductResponseDTOS);

        ResponseEntity<List<PurchaseOrderProductResponseDTO>> responseResponseDTOList =
                purchaseOrderController.getProductsOfPurchase("orderId", "token");

        assertEquals(HttpStatus.OK, responseResponseDTOList.getStatusCode());

    }

    @Test
    void createPurchaseOrder() {
        when(purchaseOrderService.create(any(), any())).thenReturn(new PurchaseValueResponseDTO(100.0));

        ResponseEntity<PurchaseValueResponseDTO> responseResponseDTOList =
                purchaseOrderController.create("token", purchaseRequestDTO);

        assertEquals(HttpStatus.CREATED, responseResponseDTOList.getStatusCode());
    }

    @Test
    void updatePurchaseOrder() {
        when(purchaseOrderService.update(any(), any())).thenReturn(purchaseOrderResponseDTO);

        ResponseEntity<PurchaseOrderResponseDTO> responseResponseDTOList =
                purchaseOrderController.update("orderId", purchaseRequestDTO, "token");

        assertEquals(HttpStatus.OK, responseResponseDTOList.getStatusCode());
    }
}
