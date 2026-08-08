package com.quickship.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
	
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String onlyAdmin()
	{
		return "Welcome To Admin";
	}
	
	@GetMapping("/customer")
	@PreAuthorize("hasRole('CUSTOMER')")
	public String onlyCustomer()
	{
		return "Welcome To Customer";
	}
	
	@GetMapping("/delivery")
	@PreAuthorize("hasRole('DELIVERY_AGENT')")
	public String onlyDelivery()
	{
		return "Welcome To Delivery_Agent";
	}

	
}
