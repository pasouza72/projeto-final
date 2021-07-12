package com.mercadolibre.projetofinal.config;

import com.mercadolibre.projetofinal.exceptions.*;
import com.newrelic.api.agent.NewRelic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import static com.mercadolibre.projetofinal.enums.ErrorsEnum.*;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
		ApiError apiError = new ApiError(
				ML1001.getCode(),
				String.format(ML1001.getDescription(), req.getRequestURI()),
				ML1001.getHttpStatus().value()
		);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { ApiException.class })
	protected ResponseEntity<ApiError> handleApiException(ApiException e) {
		Integer statusCode = e.getStatusCode();
		boolean expected = HttpStatus.INTERNAL_SERVER_ERROR.value() > statusCode;
		NewRelic.noticeError(e, expected);
		if (expected) {
			LOGGER.warn("Internal Api warn. Status Code: " + statusCode, e);
		} else {
			LOGGER.error("Internal Api error. Status Code: " + statusCode, e);
		}

		ApiError apiError = new ApiError(e.getCode(), e.getDescription(), statusCode);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
		LOGGER.error("Internal error", e);
		NewRelic.noticeError(e);

		ApiError apiError = new ApiError(ML1002);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = {HttpMessageNotReadableException.class})
	protected  ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
		ApiError apiError = new ApiError(ML1003);
		return ResponseEntity.status(apiError.getStatus()).body(apiError);
	}

	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	protected ResponseEntity<ApiError> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		ApiError apiError = new ApiError(ML1004.getCode(), e.getMessage(), ML1004.getHttpStatus().value());
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
		ApiError apiError = new ApiError(e);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { BadRequestException.class })
	protected ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
		ApiError apiError = new ApiError(e);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		ApiError apiError = new ApiError(ML1007);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ValidationError validationError = new ValidationError(ML1008);

		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity.status(validationError.getStatus())
				.body(validationError);
	}

	@ExceptionHandler(ProductsOutOfStockException.class)
	protected ResponseEntity<ProductsOutOfStockError> handleProductsOutOfStockException(ProductsOutOfStockException e){
		ProductsOutOfStockError productsOutOfStockError = new ProductsOutOfStockError(e.getCode(), e.getDescription(),
																			  e.getStatusCode(),e.getProductErrors());
		return ResponseEntity.status(e.getStatusCode()).body(productsOutOfStockError);
	}
}