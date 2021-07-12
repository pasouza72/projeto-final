package com.mercadolibre.projetofinal.dtos.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    @Valid
    private InboundOrderRequestDTO inboundOrder;
}
