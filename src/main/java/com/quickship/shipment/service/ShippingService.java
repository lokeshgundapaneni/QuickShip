package com.quickship.shipment.service;

import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.ShipmentResponse;

public interface ShippingService {
	
	public ShipmentResponse createShipment(CreateShipmentRequest request);
	
}
