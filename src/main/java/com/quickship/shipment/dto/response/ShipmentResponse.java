package com.quickship.shipment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickship.shipment.enums.PackageType;
import com.quickship.shipment.enums.ShipmentStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {

    private Long id;

    private String trackingNumber;

    private AddressResponse sender;

    private AddressResponse recipient;

    private PackageType packageType;

    private BigDecimal weight;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal declaredValue;

    private ShipmentStatus status;

    private BigDecimal estimatedPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}