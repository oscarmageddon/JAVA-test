package com.example.userservice.exception;

import com.example.userservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse.ErrorDetail error = new ErrorResponse.ErrorDetail(HttpStatus.CONFLICT.value(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(Arrays.asList(error));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse.ErrorDetail error = new ErrorResponse.ErrorDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(Arrays.asList(error));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorResponse.ErrorDetail(HttpStatus.BAD_REQUEST.value(), error.getDefaultMessage()))
            .collect(Collectors.toList());
        
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse.ErrorDetail error = new ErrorResponse.ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        ErrorResponse response = new ErrorResponse(Arrays.asList(error));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}