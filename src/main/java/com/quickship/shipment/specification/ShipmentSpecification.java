package com.quickship.shipment.specification;

import org.springframework.data.jpa.domain.Specification;

import com.quickship.shipment.entity.Shipment;
import com.quickship.shipment.enums.ShipmentStatus;

public class ShipmentSpecification {

    private ShipmentSpecification() {
    }
    
    public static Specification<Shipment> hasStatus(
            ShipmentStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }
    public static Specification<Shipment> belongsToCustomer(
            Long customerId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("customer").get("id"),
                        customerId
                );
    }
}