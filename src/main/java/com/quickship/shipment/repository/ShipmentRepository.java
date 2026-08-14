package com.quickship.shipment.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.quickship.auth.entity.User;
import com.quickship.shipment.entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    boolean existsByTrackingNumber(String trackingNumber);
    
    Page<Shipment> findByCustomer(User customer, Pageable pageable);

}