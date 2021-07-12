package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    private final static Integer HTTP_STATUS = HttpStatus.NOT_FOUND.value();

    public NotFoundException(String code, String description, Integer statusCode) {
        super(code, description, statusCode);
        checkValidHttpStatusCode(HTTP_STATUS);
    }

    public NotFoundException(ErrorsEnum errorsEnum){
        super(errorsEnum);
        checkValidHttpStatusCode(HTTP_STATUS);
    }
}
