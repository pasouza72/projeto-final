package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.model.BatchProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchProductResponseDTO {

    private Integer batchNumber;
    private Double currentTemperature;
    private Double minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private String manufacturingDate;
    private String manufacturingTime;
    private String dueDate;

    public BatchProductResponseDTO(BatchProduct batchProduct){
        this.batchNumber = batchProduct.getBatchNumber();
        this.currentTemperature = batchProduct.getCurrentTemperature();
        this.minimumTemperature = batchProduct.getMinimumTemperature();
        this.initialQuantity = batchProduct.getInitialQuantity();
        this.currentQuantity = batchProduct.getCurrentQuantity();
        this.manufacturingDate = batchProduct.getManufacturingDate().toString();
        this.manufacturingTime = batchProduct.getManufacturingTime().toString();
        this.dueDate = batchProduct.getDueDate().toString();
    }
}
