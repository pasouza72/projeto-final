package com.mercadolibre.projetofinal.exceptions;


import lombok.Getter;

import java.util.List;

@Getter
public class ProductsOutOfStockError extends ApiError{
    private List<String> productsOutOfStock;

    public ProductsOutOfStockError(String error, String message, Integer status, List<String> productsOutOfStock) {
        super(error, message, status);
        this.productsOutOfStock = productsOutOfStock;
    }
}
