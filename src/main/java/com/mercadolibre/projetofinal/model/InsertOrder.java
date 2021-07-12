package com.mercadolibre.projetofinal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertOrder {

    @Id
    private Integer id;

    @NotNull
    @ManyToOne
    private WarehousesSections warehousesSections;

    @NotNull
    @ManyToOne
    private Account representative;

    @NotNull
    @OneToMany(mappedBy = "insertOrder", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("batch_products")
    private List<BatchProduct> batchProducts;

    @NotNull
    private LocalDateTime localDateTime;
}
