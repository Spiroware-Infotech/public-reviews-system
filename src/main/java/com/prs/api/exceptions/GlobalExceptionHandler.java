package com.prs.api.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prs.api.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMsg = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "));

		ApiResponse<?> error = new ApiResponse<>(false, errorMsg, null, HttpStatus.BAD_REQUEST.value(),
				LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	 @ExceptionHandler(EntityNotFoundException.class)
	    public ResponseEntity<ApiResponse<String>> handleNotFound(EntityNotFoundException ex) {
	        return new ResponseEntity<>(
	                new ApiResponse<>(false, ex.getMessage(), null, HttpStatus.NOT_FOUND.value()),
	                HttpStatus.NOT_FOUND
	        );
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
	        return new ResponseEntity<>(
	                new ApiResponse<>(false, "Unexpected error: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
}
