package com.siscon.demo.employee.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
            "NOT_FOUND", 
            e.getMessage(), 
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException e) {
		log.error("Service error", e);
		
        ErrorResponse error = new ErrorResponse(
            "CONFLICT", 
            e.getMessage(), 
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        log.error("Service error", e);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR", 
            e.getMessage(), 
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Unexpected error", e);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR", 
            e.getMessage(), 
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String code;
        private String message;
        private LocalDateTime timestamp;
    }
}