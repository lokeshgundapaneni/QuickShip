package com.quickship.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
	
	@NotBlank(message="First Name is required")
	@Size(min=2,max=50,message="Last name must be 2 and 50 characters")
	private String firstName;
	
	@NotBlank(message="Last Name is required")
	@Size(min=2,max=50,message="Last name must be 2 and 50 characters")
	private String lastName;
	
	@NotBlank(message="email is required")
	@Email(message="please enter the valid email address")
	private String email;
	
	@NotBlank(message="password is required")
	@Size(min=8 , message="password must contain 8 characters")
	private String password;
	
	@NotBlank(message="phone is required")
	@Pattern(regexp="^[6-9]\\d{9}$",
			 message="please enter a valid number")
	private String phone;

}
