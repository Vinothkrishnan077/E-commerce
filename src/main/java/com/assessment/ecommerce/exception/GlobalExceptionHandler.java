package com.assessment.ecommerce.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException exception, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("Error", exception.getMessage()); // Corrected typo: "message" instead of "message: "
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}