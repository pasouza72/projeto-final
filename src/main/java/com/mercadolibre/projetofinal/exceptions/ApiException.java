package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String code;
	private final String description;
	private final Integer statusCode;

	public ApiException(String code, String description, Integer statusCode) {
		super(description);
		this.code = code;
		this.description = description;
		this.statusCode = statusCode;
	}

	public ApiException(String code, String description, Integer statusCode, Throwable cause) {
		super(description, cause);
		this.code = code;
		this.description = description;
		this.statusCode = statusCode;
	}

	public ApiException(ErrorsEnum errorEnum) {
		super(errorEnum.getDescription());
		this.code = errorEnum.getCode();
		this.description = errorEnum.getDescription();
		this.statusCode = errorEnum.getHttpStatus().value();
	}

	public void checkValidHttpStatusCode(Integer httpStatusCode) {
		if (!this.getStatusCode().equals(httpStatusCode)) {
			throw new RuntimeException("The HTTP status " + this.getStatusCode() + " must be set to " + httpStatusCode);
		}
	}
}
