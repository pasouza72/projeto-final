package com.mercadolibre.projetofinal.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "warehouses_sections")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarehousesSections {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    @NotNull
    private Warehouse warehouse;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private Section section;

    @NotNull
    private Integer stockLimit;

    @OneToMany(mappedBy = "warehousesSections")
    private Set<BatchProduct> batchProducts;
}
