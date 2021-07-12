package com.mercadolibre.projetofinal.dtos.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.mercadolibre.projetofinal.model.BatchProduct;
import com.mercadolibre.projetofinal.model.Product;
import com.mercadolibre.projetofinal.model.WarehousesSections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchProductRequestDTO {

    @NotBlank
    private String productId;
    @NotNull
    private Integer batchNumber;
    @NotNull
    private Double currentTemperature;
    @NotNull
    private Double minimumTemperature;
    @NotNull
    @Positive
    private Integer initialQuantity;
    @NotNull
    @PositiveOrZero
    private Integer currentQuantity;
    @NotNull
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate manufacturingDate;
    @NotNull
    @Past
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime manufacturingTime;
    @NotNull
    @Future
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;

    public BatchProduct toModel(Product product, WarehousesSections warehousesSections){
        return new BatchProduct(null, batchNumber, currentTemperature, minimumTemperature, initialQuantity,
                currentQuantity, manufacturingDate, manufacturingTime, dueDate, product, warehousesSections, null);
    }
}
