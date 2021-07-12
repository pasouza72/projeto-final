package com.mercadolibre.projetofinal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PurchaseOrderProductResponseDTO {
    private String id;
    private String name;
    private Integer quantity;
}
