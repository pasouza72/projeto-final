package com.mercadolibre.projetofinal.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<BatchProduct> batchProducts;

    public Product(String id) {
        this.id = id;
    }
}
