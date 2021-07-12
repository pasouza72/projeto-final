package com.mercadolibre.projetofinal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDTO {

    private String id;

    private Integer oldValue;
    private Integer newValue;
    private boolean isToRemoveFromPurchase;

}
