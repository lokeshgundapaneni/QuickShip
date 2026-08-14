package com.quickship.shipment.exception;

public class ShipmentNotFoundException extends RuntimeException {

	public ShipmentNotFoundException(String message)
	{
		super(message);
	}
}
