package com.enel.platform.mepodlatam.controller.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.constants.Errors;
import com.enel.platform.mepodlatam.dto.RestErrorResponseDTO;


@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<RestErrorResponseDTO<String>> handleMethodArgumentNotValidException(HttpServletRequest request,
			MethodArgumentNotValidException e) {

		BindingResult result = e.getBindingResult();
		RestErrorResponseDTO<String> response = new RestErrorResponseDTO<>();

		StringBuilder errors = new StringBuilder();
		result.getFieldErrors().forEach(fieldError -> errors.append(", ")
				.append(String.format(fieldError.getDefaultMessage(), fieldError.getField())));

		response.setErrorMessages(this.buildErrorMap(Errors.VALIDATION.getCode(), String.format(Errors.VALIDATION.getMessage(), errors.toString().replaceFirst(",", "").trim())));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@ExceptionHandler(ApplicationFilterException.class)
	public ResponseEntity<RestErrorResponseDTO> handleApplicationFilterException(ApplicationFilterException e) {
		String errorMessage = null;
		if (e.getMessage() == null) {
			errorMessage = Errors.FILTER_NOT_SPECIFIED.getMessage();			
		} else {
			errorMessage = e.getMessage();
		}
		LOGGER.error(errorMessage);
		RestErrorResponseDTO<?> response = new RestErrorResponseDTO<>();
		response.setErrorMessages(
				this.buildErrorMap(Errors.FILTER_NOT_SPECIFIED.getCode(), errorMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<RestErrorResponseDTO> handleUnsupportedOperationException(UnsupportedOperationException e) {				
		LOGGER.error(Errors.UNSUPPORTED_OPERATION.getMessage());
		RestErrorResponseDTO<?> response = new RestErrorResponseDTO<>();
		response.setErrorMessages(
				this.buildErrorMap(Errors.UNSUPPORTED_OPERATION.getCode(), Errors.UNSUPPORTED_OPERATION.getMessage()));
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@Order(value = Ordered.LOWEST_PRECEDENCE)
	public ResponseEntity<RestErrorResponseDTO> onException(HttpServletRequest request, Exception e) {
				Throwable cause = e.getCause();
		e.printStackTrace();
	    if (cause != null) {
			if (cause instanceof ApplicationFilterException) {
				return handleApplicationFilterException((ApplicationFilterException) cause);
			} else if (cause instanceof UnsupportedOperationException) {
				return handleUnsupportedOperationException((UnsupportedOperationException) cause);
			}
		}
		
		LOGGER.error(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
		RestErrorResponseDTO<?> response = new RestErrorResponseDTO<>();
		response.setErrorMessages(this.buildErrorMap(Errors.GENERIC.getCode(),
				Errors.GENERIC.getMessage()));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

	}

	private Map<String, String> buildErrorMap(String code, String message) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(code, message);
		return errorMap;
	}

}
