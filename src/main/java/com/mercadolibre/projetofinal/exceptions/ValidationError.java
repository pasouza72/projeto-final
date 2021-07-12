package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends ApiError {

    private final static Integer HTTP_STATUS = HttpStatus.BAD_REQUEST.value();
    private final List<FieldMessage> messages = new ArrayList<>();

    public ValidationError(String code, String description, Integer statusCode) {
        super(code, description, statusCode);

    }

    public ValidationError(ErrorsEnum errorsEnum) {
        super(errorsEnum);
    }

    public void addError(String fieldName, String message) {
        this.messages.add(new FieldMessage(fieldName, message));
    }
}
