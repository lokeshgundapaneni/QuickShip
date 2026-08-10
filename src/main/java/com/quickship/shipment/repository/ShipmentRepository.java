package com.quickship.shipment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.quickship.shipment.entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    boolean existsByTrackingNumber(String trackingNumber);

}