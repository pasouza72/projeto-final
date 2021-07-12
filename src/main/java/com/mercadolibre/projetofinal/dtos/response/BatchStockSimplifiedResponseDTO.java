package com.mercadolibre.projetofinal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class BatchStockSimplifiedResponseDTO {

    private String batchId;
    private Integer currentQuantity;
    private String dueDate;

    public BatchStockSimplifiedResponseDTO(String batchId, Integer currentQuantity, LocalDate dueDate) {
        this.batchId = batchId;
        this.currentQuantity = currentQuantity;
        this.dueDate = dueDate.toString();
    }
}
