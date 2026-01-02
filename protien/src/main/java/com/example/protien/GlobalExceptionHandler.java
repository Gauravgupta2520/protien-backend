package com.example.protien;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("duplicate")) {
            msg = "Duplicate entry: email already exists";
        }

        Map<String,Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", msg);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
        // Log full stacktrace for debugging
        logger.error("Unhandled exception caught: {}", ex.getMessage(), ex);

        Map<String,Object> body = new HashMap<>();
        body.put("success", false);
        // Return a small error message and the exception message to help debug (remove in production)
        body.put("message", "Internal server error");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
