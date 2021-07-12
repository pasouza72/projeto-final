package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.dtos.request.OrderStatusRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseOrderResponseDTO {
    private String id;
    private LocalDate date;
    private OrderStatusRequestDTO orderStatus;
    private List<PurchaseOrderProductResponseDTO> products;
}
