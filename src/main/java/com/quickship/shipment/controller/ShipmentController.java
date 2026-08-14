package com.quickship.shipment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickship.common.response.ApiResponse;
import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.ShipmentResponse;
import com.quickship.shipment.service.ShippingService;
import com.quickship.shipment.service.impl.ShippingServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {
	
	private final ShippingService shippingService;

	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request)
	{
		ShipmentResponse response = shippingService.createShipment(request);
		
		ApiResponse apiResponse = new ApiResponse<>( true,
                "Shipment created successfully",
                response);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}
}
