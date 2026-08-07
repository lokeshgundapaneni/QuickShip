package com.quickship.auth.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickship.auth.dto.request.LoginRequest;
import com.quickship.auth.dto.request.RegisterRequest;
import com.quickship.auth.dto.response.LoginResponse;
import com.quickship.auth.dto.response.UserResponse;
import com.quickship.auth.entity.User;
import com.quickship.auth.enums.UserRole;
import com.quickship.auth.enums.UserStatus;
import com.quickship.auth.repository.UserRepository;
import com.quickship.auth.service.UserService;
import com.quickship.common.exception.EmailAlreadyExistsException;
import com.quickship.common.exception.PhoneAlreadyExistsException;
import com.quickship.security.jwt.JwtService;
import com.quickship.security.model.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	@Override
	public UserResponse register(RegisterRequest request) {
		
		if(userRepository.existsByEmail(request.getEmail()))
		{
			throw new EmailAlreadyExistsException("Email already exists");
		}
		
		if(userRepository.existsByPhone(request.getPhone()))
		{
			throw new PhoneAlreadyExistsException("Phone number already exists. ");
		}
		
		User user=mapToUser(request);
		User savedUser=userRepository.save(user);
		return mapToUserResponse(user);
	}


	private User mapToUser(RegisterRequest request) {
		return User.builder()
				.firstName(request.getFirstName())
				.LastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.phone(request.getPhone())
				.role(UserRole.CUSTOMER)
				.status(UserStatus.ACTIVE)
				.emailVerified(false)
                .build();
	}
	
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
	}


	@Override
	public LoginResponse login(LoginRequest request) {
		Authentication authentication =
		        authenticationManager.authenticate(
		                new UsernamePasswordAuthenticationToken(
		                        request.getEmail(),
		                        request.getPassword()
		                )
		        );
		
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		
		String token=jwtService.generateToken(userDetails);
		return LoginResponse.builder()
		        .token(token)
		        .type("Bearer")
		        .email(userDetails.getUsername())
		        .role(userDetails.getUser().getRole().name())
		        .build();
	}
}
