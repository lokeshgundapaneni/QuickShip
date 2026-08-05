package com.quickship.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickship.auth.dto.request.RegisterRequest;
import com.quickship.auth.dto.response.UserResponse;
import com.quickship.auth.service.UserService;
import com.quickship.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
	
	private final UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponse>> register(
	        @Valid @RequestBody RegisterRequest request){

	    UserResponse response = userService.register(request);

	    ApiResponse<UserResponse> apiResponse =
	            ApiResponse.<UserResponse>builder()
	                    .success(true)
	                    .message("User registered successfully")
	                    .data(response)
	                    .build();
	    
	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(apiResponse);

	}

}
