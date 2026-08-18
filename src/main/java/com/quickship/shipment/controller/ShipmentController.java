package com.quickship.shipment.controller;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name="Shipment Management", description="APIs for creating, retrieving, tracking and cancelling shipments")
public class ShipmentController {
	
	private final ShippingService shippingService;

	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(summary="Creates a Shipment",description="Creates a new shipment for the authenticated customer")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "201",
		        description = "Shipment created successfully",
		        content = @Content(
		        		      mediaType = "application/json",
		        		      schema = @Schema(
		        		      implementation = ShipmentResponse.class
		        		        )
		        		    )
		    ),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	        responseCode = "400",
	        description = "Invalid shipment data"
	    ),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	        responseCode = "401",
	        description = "Authentication required"
	    ),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	        responseCode = "403",
	        description = "Customer role required"
	    )
	})
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
	@Operation(
			summary="Get the shipment by id",
			description="Retrieves a shipment belonging to the authenticated user"
			)
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="200",
				description="Shipment retrieved successfully",
				content = @Content(
						     mediaType = "application/json",
						     schema = @Schema(
						     implementation = ShipmentResponse.class
						        )
						    )
				),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="401",
				description="Authentication required"
				),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="403",
				description="Access denied"
				),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="404",
				description="Shipment  not found"
				)
	})
	public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentById(
	        @Parameter(
	        		description="unique identifier of the shipment",
	        		example="101",
	        		required=true
	        		)
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
	@Operation(
			summary="track the shipment",
			description="Retireves shipment information using the tracking number"
			)
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="200",
				description="Shipment tracking information retrieved"
				),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode="404",
				description="Shipment Not Found"
				)
	})
	public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentByTrackingId(
			@Parameter(
					description="unique shipment number",
					example="QS202608170001",
					required=true
					)
			@PathVariable String trackingNumber)
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
	@Operation(
		    summary = "Get my shipments",
		    description = "Retrieves shipments belonging to the authenticated customer with optional filtering, pagination and sorting"
		)
		@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "200",
		        description = "Shipments retrieved successfully"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "401",
		        description = "Authentication required"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "403",
		        description = "Customer role required"
		    )
		})
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
	@Operation(
		    summary = "Cancel a shipment",
		    description = "Cancels a shipment when its current status allows cancellation"
		)
		@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "200",
		        description = "Shipment cancelled successfully"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "401",
		        description = "Authentication required"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "403",
		        description = "Access denied"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "404",
		        description = "Shipment not found"
		    ),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "409",
		        description = "Shipment cannot be cancelled in its current state"
		    )
		})
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
