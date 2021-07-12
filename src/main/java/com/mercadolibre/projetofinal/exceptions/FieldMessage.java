package com.mercadolibre.projetofinal.exceptions;

import lombok.Getter;

@Getter
public class FieldMessage {

    private final String field;
    private final String message;

    public FieldMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

}