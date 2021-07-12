package com.mercadolibre.projetofinal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsRequestDTO {
    @NotNull
    @NotBlank
    private String productId;
    @NotNull
    @Positive
    private Integer quantity;
}
