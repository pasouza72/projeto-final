package com.mercadolibre.projetofinal.dtos.request;

import com.mercadolibre.projetofinal.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequestDTO {
    @NotNull
    private OrderStatusEnum statusCode;
}
