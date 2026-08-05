package com.quickship.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quickship.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistException(EmailAlreadyExistsException ex)
	{
		ApiResponse<Void> response=ApiResponse.<Void>builder()
				.success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	 
	@ExceptionHandler(PhoneAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<Void>> handlePhoneAlreadyExistsException(
	         PhoneAlreadyExistsException ex) {
		ApiResponse<Void> response=ApiResponse.<Void>builder()
				.success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(
            UserNotFoundException ex) {
    	ApiResponse<Void> response=ApiResponse.<Void>builder()
				.success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response =
                ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation Failed")
                        .data(errors)
                        .build();

        return ResponseEntity.badRequest().body(response);
    }

}
