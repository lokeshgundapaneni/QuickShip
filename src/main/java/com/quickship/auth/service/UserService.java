package com.quickship.auth.service;

import com.quickship.auth.dto.request.RegisterRequest;
import com.quickship.auth.dto.response.UserResponse;

public interface UserService {

	UserResponse register(RegisterRequest request);
}
