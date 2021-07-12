package com.mercadolibre.projetofinal.dtos.response;

import com.mercadolibre.projetofinal.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponseResponseDTO {

    public ProductResponseResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }

    private String id;
    private String name;
    private Double price;
    private String description;
}
