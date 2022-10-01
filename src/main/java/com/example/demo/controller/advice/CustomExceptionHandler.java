package com.example.demo.controller.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.ErrorWhileProcessingRequest;
import com.example.demo.response.ErrorResponse;
import com.example.demo.response.RequestStatus;

@RestControllerAdvice
public class CustomExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<Object> handleErrorWhileProcessingRequestException(HttpServletRequest request,
			ErrorWhileProcessingRequest exception) {
		LOGGER.error(exception.getMessage(), exception);
		ErrorResponse baseResponse = new ErrorResponse(exception.getMessage(), request.getRequestURI(),
				RequestStatus.ERROR);
		LOGGER.info("Returning: {}", baseResponse);
		
		if (exception.isBadRequest()) {
			return new ResponseEntity<Object>(baseResponse, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Object>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
