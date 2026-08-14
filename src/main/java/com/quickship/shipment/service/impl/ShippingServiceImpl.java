package com.quickship.shipment.service.impl;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickship.auth.entity.User;
import com.quickship.auth.repository.UserRepository;
import com.quickship.common.exception.UserNotFoundException;
import com.quickship.shipment.dto.request.AddressRequest;
import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.AddressResponse;
import com.quickship.shipment.dto.response.ShipmentResponse;
import com.quickship.shipment.entity.Address;
import com.quickship.shipment.entity.Shipment;
import com.quickship.shipment.enums.ShipmentStatus;
import com.quickship.shipment.exception.ShipmentCancellationException;
import com.quickship.shipment.exception.ShipmentNotFoundException;
import com.quickship.shipment.repository.AddressRepository;
import com.quickship.shipment.repository.ShipmentRepository;
import com.quickship.shipment.service.ShippingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

	
	@Override
	@Transactional
	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		User customer = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User Not Found"));
		
		//sender Address
		Address senderAddress = addressRepository.save(mapToAddress(request.getSender()));
		
		//recipient Address 
		Address recipientAddress = addressRepository.save(mapToAddress(request.getRecipient()));
		
		Shipment shipment=Shipment.builder()
		.trackingNumber(generateTrackingNumber())
		.customer(customer)
		.senderAddress(senderAddress)
		.recipientAddress(recipientAddress)
		.packageType(request.getPackageType())
		.weight(request.getWeight())
        .length(request.getLength())
        .width(request.getWidth())
        .height(request.getHeight())
        .declaredValue(request.getDeclaredValue())
        .build();
		
		Shipment savedShipment = shipmentRepository.save(shipment);
		
		return mapToResponse(savedShipment);
	}
	
	
	 private ShipmentResponse mapToResponse(Shipment shipment) {
		
		 return ShipmentResponse.builder()
	                .id(shipment.getId())
	                .trackingNumber(shipment.getTrackingNumber())
	                .sender(mapToAddressResponse(
	                        shipment.getSenderAddress()))
	                .recipient(mapToAddressResponse(
	                        shipment.getRecipientAddress()))
	                .packageType(shipment.getPackageType())
	                .weight(shipment.getWeight())
	                .length(shipment.getLength())
	                .width(shipment.getWidth())
	                .height(shipment.getHeight())
	                .declaredValue(shipment.getDeclaredValue())
	                .status(shipment.getStatus())
	                .estimatedPrice(shipment.getEstimatedPrice())
	                .createdAt(shipment.getCreatedAt())
	                .updatedAt(shipment.getUpdatedAt())
	                .build();
	}


	 private String generateTrackingNumber() {
		return "QS"+System.currentTimeMillis();
	}


	 private Address mapToAddress(AddressRequest request) {

	        return Address.builder()
	                .name(request.getName())
	                .phone(request.getPhone())
	                .addressLine1(request.getAddressLine1())
	                .addressLine2(request.getAddressLine2())
	                .city(request.getCity())
	                .state(request.getState())
	                .country(request.getCountry())
	                .postalCode(request.getPostalCode())
	                .build();
	    }


	private AddressResponse mapToAddressResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .phone(address.getPhone())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }


	@Override
	@Transactional(readOnly = true)
	public ShipmentResponse getShipmentById(Long shipmentId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		
		User customer=userRepository.findByEmail(email).orElseThrow(
				()->new UserNotFoundException("Authenticated User Not Found"));
		
		Shipment shipment=shipmentRepository.findById(shipmentId).orElseThrow(
				()->new ShipmentNotFoundException("Shipment not found with id: " + shipmentId));
		
		if (!shipment.getCustomer().getId().equals(customer.getId()))
		{
			throw new AccessDeniedException("You are not allowed to access this shipment");
		}
		
		return mapToResponse(shipment);
		
	}


	@Override
	@Transactional(readOnly=true)
	public ShipmentResponse getShipmentByTrackingNumber(String trackingNumber)
	{
		Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber).orElseThrow(
				()->new ShipmentNotFoundException("Shipment not found with tracking number: "+ trackingNumber));
		return mapToResponse(shipment);
	}


	@Override
	@Transactional(readOnly = true)
	public Page<ShipmentResponse> getMyShipments(Pageable pageable) {

	    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User customer = userRepository.findByEmail(email)
	            .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));
	    Page<Shipment> shipments =
	            shipmentRepository.findByCustomer(
	                    customer,
	                    pageable
	            );
	    return shipments.map(this::mapToResponse);
	}


	@Override
	public void cancelShipment(Long ShipmentId) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User customer = userRepository.findByEmail(email)
	            .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));
	    Shipment shipment=shipmentRepository.findById(ShipmentId).orElseThrow(
	    		()->new ShipmentNotFoundException("Shipment not found with id: "+ ShipmentId));
	    if(!canCancel(shipment.getStatus()))
	    {
	    	throw new ShipmentCancellationException(
	                "Shipment cannot be cancelled in status: "
	                + shipment.getStatus());
	    }
	    shipment.setStatus(ShipmentStatus.CANCELLED);
	    shipmentRepository.save(shipment);
	}


	private boolean canCancel(ShipmentStatus status) {
		 return status == ShipmentStatus.CREATED
		            || status == ShipmentStatus.PENDING_PAYMENT
		            || status == ShipmentStatus.CONFIRMED
		            || status == ShipmentStatus.PICKUP_ASSIGNED;
	}

}
