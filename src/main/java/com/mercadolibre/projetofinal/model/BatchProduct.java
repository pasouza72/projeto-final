package com.mercadolibre.projetofinal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BatchProduct {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private Integer batchNumber;
    @NotNull
    private Double currentTemperature;
    @NotNull
    private Double minimumTemperature;
    @NotNull
    private Integer initialQuantity;
    @NotNull
    private Integer currentQuantity;
    @NotNull
    private LocalDate manufacturingDate;
    @NotNull
    private LocalDateTime manufacturingTime;
    @NotNull
    private LocalDate dueDate;
    @ManyToOne
    private Product product;
    @ManyToOne
    private WarehousesSections warehousesSections;
    @ManyToOne
    @JsonBackReference("batch_products")
    private InsertOrder insertOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BatchProduct)) return false;
        BatchProduct that = (BatchProduct) o;
        return Objects.equals(batchNumber, that.batchNumber)
                && Objects.equals(currentTemperature, that.currentTemperature)
                && Objects.equals(minimumTemperature, that.minimumTemperature)
                && Objects.equals(initialQuantity, that.initialQuantity)
                && Objects.equals(currentQuantity, that.currentQuantity)
                && Objects.equals(manufacturingDate, that.manufacturingDate)
                && Objects.equals(manufacturingTime, that.manufacturingTime)
                && Objects.equals(dueDate, that.dueDate)
                && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchNumber, currentTemperature, minimumTemperature, initialQuantity, currentQuantity, manufacturingDate, manufacturingTime, dueDate, product, warehousesSections, insertOrder);
    }
}
