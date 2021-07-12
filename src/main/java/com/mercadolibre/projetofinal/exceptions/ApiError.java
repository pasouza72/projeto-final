package com.mercadolibre.projetofinal.exceptions;

import com.mercadolibre.projetofinal.enums.ErrorsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiError {
	private String error;
	private String message;
	private Integer status;

	public ApiError() {
	}

	public ApiError(ApiException apiException) {
		this.error = apiException.getCode();
		this.message = apiException.getDescription();
		this.status = apiException.getStatusCode();
	}

	public ApiError(ErrorsEnum errorsEnum) {
		this.error = errorsEnum.getCode();
		this.message = errorsEnum.getDescription();
		this.status = errorsEnum.getHttpStatus().value();
	}
}
