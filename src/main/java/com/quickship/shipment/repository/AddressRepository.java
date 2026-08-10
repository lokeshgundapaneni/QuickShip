package com.quickship.shipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickship.shipment.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}