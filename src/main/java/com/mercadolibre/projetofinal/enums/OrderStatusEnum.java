package com.mercadolibre.projetofinal.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatusEnum {
    NEW(1),
    RECEIVED(2),
    CANCELED(3),
    CLOSED(4);

    private final Integer id;
}
