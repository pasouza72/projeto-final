package com.mercadolibre.projetofinal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductCountListDTO {

    private String productId;
    private List<ProductCountResponseDTO> warehouses;
}
