package com.quickship.auth.dto.response;

import java.time.LocalDateTime;

import com.quickship.auth.enums.UserRole;
import com.quickship.auth.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
	
	private Long id;
	
    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private UserRole role;

    private UserStatus status;

    private boolean emailVerified;

    private LocalDateTime createdAt;
}
