package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    private final static Integer HTTP_STATUS = HttpStatus.BAD_REQUEST.value();

    public BadRequestException(String code, String description, Integer statusCode) {
        super(code, description, statusCode);
        checkValidHttpStatusCode(HTTP_STATUS);
    }

    public BadRequestException(ErrorsEnum errorsEnum){
        super(errorsEnum);
        checkValidHttpStatusCode(HTTP_STATUS);
    }
}
