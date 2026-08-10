package com.quickship.shipment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickship.auth.entity.User;
import com.quickship.shipment.enums.PackageType;
import com.quickship.shipment.enums.ShipmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shipment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String trackingNumber;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private User customer;

	@ManyToOne
	@JoinColumn(name = "sender_address_id")
	private Address senderAddress;

	@ManyToOne
	@JoinColumn(name = "recipient_address_id")
	private Address recipientAddress;
	
	@Enumerated(EnumType.STRING)
	private PackageType packageType;
	
    private BigDecimal weight;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;
    
    private BigDecimal declaredValue;
    
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
    
    private BigDecimal estimatedPrice;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
