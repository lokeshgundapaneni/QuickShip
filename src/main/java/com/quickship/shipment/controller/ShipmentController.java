package com.quickship.shipment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickship.common.response.ApiResponse;
import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.ShipmentResponse;
import com.quickship.shipment.enums.ShipmentStatus;
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
	
	@GetMapping("/{shipmentId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentById(
	        @PathVariable Long shipmentId) {

	    ShipmentResponse response =
	            shippingService.getShipmentById(shipmentId);

	    ApiResponse<ShipmentResponse> apiResponse =
	            new ApiResponse<>(
	                    true,
	                    "Shipment retrieved successfully",
	                    response
	            );

	    return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/track/{trackingNumber}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentByTrackingId(@PathVariable String trackingNumber)
	{
		ShipmentResponse response = shippingService.getShipmentByTrackingNumber(trackingNumber);
		ApiResponse<ShipmentResponse> apiResponse =
	            new ApiResponse<>(
	                    true,
	                    "Shipment retrieved successfully",
	                    response
	            );

	    return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/my")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<Page<ShipmentResponse>>> getMyShipments(
	        @RequestParam(required = false) ShipmentStatus status,
	        Pageable pageable) {

	    Page<ShipmentResponse> shipments =
	            shippingService.getMyShipments(
	                    status,
	                    pageable
	            );

	    ApiResponse<Page<ShipmentResponse>> response =
	            new ApiResponse<>(
	                    true,
	                    "Shipments retrieved successfully",
	                    shipments
	            );

	    return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{shipmentId}/cancel")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ApiResponse<Void>> cancelShipment(
	        @PathVariable Long shipmentId) {

	    shippingService.cancelShipment(shipmentId);

	    ApiResponse<Void> response =
	            new ApiResponse<>(
	                    true,
	                    "Shipment cancelled successfully",
	                    null
	            );
	    return ResponseEntity.ok(response);
	}
}
