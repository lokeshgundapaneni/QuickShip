package com.quickship.shipment.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.ShipmentResponse;
import com.quickship.shipment.entity.Shipment;
import com.quickship.shipment.enums.ShipmentStatus;

public interface ShippingService {
	
	ShipmentResponse createShipment(CreateShipmentRequest request);
	ShipmentResponse getShipmentById(Long shipmentId);
	ShipmentResponse getShipmentByTrackingNumber(String trackingNumber);
	Page<ShipmentResponse> getMyShipments(
            ShipmentStatus status,
            Pageable pageable);
	void cancelShipment(Long ShipmentId);
}
