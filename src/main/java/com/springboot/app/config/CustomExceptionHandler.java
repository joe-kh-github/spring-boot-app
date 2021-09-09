package com.springboot.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.springboot.app.models.errors.ErrorDetailsVO;
import com.springboot.app.models.errors.ErrorResponseVO;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponseVO handleException(BindException ex) {

		List<FieldError> errors = ex.getBindingResult().getFieldErrors();

		List<ErrorDetailsVO> errorDetails = new ArrayList<>();
		for (FieldError fieldError : errors) {
			ErrorDetailsVO error = new ErrorDetailsVO();
			error.setFieldName(fieldError.getField());
			error.setMessage(fieldError.getDefaultMessage());
			errorDetails.add(error);
		}

		ErrorResponseVO errorResponse = new ErrorResponseVO();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrors(errorDetails);

		return errorResponse;
	}

}