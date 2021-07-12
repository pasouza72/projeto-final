package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductsOutOfStockException extends ApiException{
    private List<String> productErrors;

    public ProductsOutOfStockException(ErrorsEnum errorEnum, List<String> productErrors) {
        super(errorEnum);
        this.productErrors = productErrors;
    }
}
