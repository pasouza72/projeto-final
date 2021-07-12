package com.mercadolibre.projetofinal.enums;

import org.springframework.http.HttpStatus;

public enum ErrorsEnum {
    // 000 - 099 - Products
    ML000("ML-000", "Product not found", HttpStatus.NOT_FOUND),
    ML001("ML-001", "At least one of the given products was not found", HttpStatus.NOT_FOUND),
    ML002("ML-002", "Products out of stock", HttpStatus.BAD_REQUEST),

    // 100 - 199 - Section and categories
    ML101("ML-101", "Category not found", HttpStatus.NOT_FOUND),
    ML102("ML-102","There is not enough room in this Section to store all products", HttpStatus.CONFLICT),
    ML103("ML-103","The given section does not exist in this warehouse", HttpStatus.NOT_FOUND),
    ML104("ML-104", "At least one product cannot be stored in this Section", HttpStatus.BAD_REQUEST),

    // 200 - 299 - Warehouse
    ML200("ML-200", "Warehouse not found", HttpStatus.NOT_FOUND),
    ML201("ML-201", "Warehouse doesn't exist in your Country", HttpStatus.NOT_FOUND),
    ML202("ML-202", "Representative does not belong to given Warehouse", HttpStatus.FORBIDDEN),

    // 300 - 399 - Country
    ML301("ML-301", "Country not found", HttpStatus.NOT_FOUND),

    // 401 - 500 - Purchase orders
    ML401("ML-401", "Purchase Order not found", HttpStatus.NOT_FOUND),

    // 501 - 600 - User
    ML501("ML-501", "Buyer not found", HttpStatus.NOT_FOUND),

    // 600 - 699 - Batch Product
    ML601("ML-601","Batch Stock for given Product not found for the Representative Warehouse", HttpStatus.NOT_FOUND),
    ML602("ML-602","There is at least one duplicate Product Batch", HttpStatus.BAD_REQUEST),
    ML603("ML-603","There are no batches of this product left", HttpStatus.NOT_FOUND),

    // 700 - 799 - InsertOrder
    ML701("ML-701","Order not found", HttpStatus.NOT_FOUND),
    ML702("ML-702","An order with the given orderNumber already exists", HttpStatus.CONFLICT),

    // 800 - 899 - Account
    ML800("ML-800", "Username and/or password incorrect", HttpStatus.NOT_FOUND),
    ML801("ML-801", "Account with id %s not found", HttpStatus.NOT_FOUND),

    // Global
    ML1001("ML-1001", "Route %s not found", HttpStatus.NOT_FOUND),
    ML1002("ML-1002", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ML1003("ML-1003", "Invalid JSON", HttpStatus.BAD_REQUEST),
    ML1004("ML-1004", "Missing parameter", HttpStatus.BAD_REQUEST),
    ML1005("ML-1005", "Resource not found", HttpStatus.NOT_FOUND),
    ML1006("ML-1006", "Bad request", HttpStatus.BAD_REQUEST),
    ML1007("ML-1007", "Invalid type for given parameter", HttpStatus.BAD_REQUEST),
    ML1008("ML-1008", "Validation error", HttpStatus.BAD_REQUEST),
    ML1009("ML-1009", "Page out of bounds", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorsEnum(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
