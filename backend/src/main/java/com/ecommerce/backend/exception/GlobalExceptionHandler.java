package com.ecommerce.backend.exception;

import com.ecommerce.backend.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                )
        );
    }
}
