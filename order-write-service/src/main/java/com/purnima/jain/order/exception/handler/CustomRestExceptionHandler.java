package com.purnima.jain.order.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.purnima.jain.order.exception.OrderAlreadyExistsException;
import com.purnima.jain.order.exception.apierror.ApiError;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(OrderAlreadyExistsException.class)
	protected ResponseEntity<Object> handleOrderAlreadyExistsException(OrderAlreadyExistsException ex) {
		ApiError apiError = new ApiError(ex.getMessage());
		ex.printStackTrace();
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

}
