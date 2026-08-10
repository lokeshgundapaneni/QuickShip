package com.quickship.shipment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickship.auth.entity.User;
import com.quickship.shipment.enums.PackageType;
import com.quickship.shipment.enums.ShipmentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "shipments",
    indexes = {
        @Index(name = "idx_shipment_tracking_number", columnList = "tracking_number"),
        @Index(name = "idx_shipment_customer_id", columnList = "customer_id"),
        @Index(name = "idx_shipment_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "tracking_number",
        nullable = false,
        unique = true,
        length = 50
    )
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "customer_id",
        nullable = false
    )
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "sender_address_id",
        nullable = false
    )
    private Address senderAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "recipient_address_id",
        nullable = false
    )
    private Address recipientAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PackageType packageType;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal length;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal width;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal height;

    @Column(precision = 15, scale = 2)
    private BigDecimal declaredValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status;

    @Column(precision = 15, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = ShipmentStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}