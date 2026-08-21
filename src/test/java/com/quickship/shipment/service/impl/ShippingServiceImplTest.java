package com.quickship.shipment.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.quickship.shipment.dto.request.AddressRequest;
import com.quickship.shipment.dto.request.CreateShipmentRequest;
import com.quickship.shipment.dto.response.ShipmentResponse;
import com.quickship.shipment.entity.Address;
import com.quickship.shipment.entity.Shipment;
import com.quickship.shipment.enums.PackageType;
import com.quickship.auth.entity.User;
import com.quickship.auth.repository.UserRepository;
import com.quickship.common.exception.UserNotFoundException;
import com.quickship.shipment.repository.AddressRepository;
import com.quickship.shipment.repository.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
class ShippingServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShippingServiceImpl shippingService;
    
    @Test
    void createShipment_shouldCreateShipmentSuccessfully()
    {
    	User customer = User.builder()
    	        .id(1L)
    	        .email("customer@gmail.com")
    	        .build();
    	
    	AddressRequest senderRequest=AddressRequest.builder()
    			.name("ABC Electronics")
    	        .phone("9876543210")
    	        .addressLine1("12 Main Road")
    	        .city("Hyderabad")
    	        .state("Telangana")
    	        .country("India")
    	        .postalCode("500001")
    	        .build();
    	AddressRequest recipientRequest = AddressRequest.builder()
    	        .name("Rahul Kumar")
    	        .phone("9123456780")
    	        .addressLine1("45 MG Road")
    	        .city("Bengaluru")
    	        .state("Karnataka")
    	        .country("India")
    	        .postalCode("560001")
    	        .build();
    	CreateShipmentRequest request =
    	        CreateShipmentRequest.builder()
    	                .sender(senderRequest)
    	                .recipient(recipientRequest)
    	                .packageType(PackageType.ELECTRONICS)
    	                .weight(new BigDecimal("2.5"))
    	                .length(new BigDecimal("40"))
    	                .width(new BigDecimal("30"))
    	                .height(new BigDecimal("10"))
    	                .declaredValue(new BigDecimal("45000"))
    	                .build();
    	
    	Authentication authentication =
    	        Mockito.mock(Authentication.class);

    	when(authentication.getName())
    	        .thenReturn("customer@gmail.com");

    	SecurityContextHolder.getContext()
    	        .setAuthentication(authentication);
    	
    	when(userRepository.findByEmail("customer@gmail.com"))
        .thenReturn(Optional.of(customer));
    	
    	Address senderAddress = Address.builder()
                .id(1L)
                .name("ABC Electronics")
                .build();

        Address recipientAddress = Address.builder()
                .id(2L)
                .name("Rahul Kumar")
                .build();
        
        when(addressRepository.save(any(Address.class)))
        .thenReturn(senderAddress)
        .thenReturn(recipientAddress);
        
        Shipment savedShipment = Shipment.builder()
                .id(100L)
                .trackingNumber("QS123456")
                .customer(customer)
                .senderAddress(senderAddress)
                .recipientAddress(recipientAddress)
                .packageType(PackageType.ELECTRONICS)
                .weight(new BigDecimal("2.5"))
                .length(new BigDecimal("40"))
                .width(new BigDecimal("30"))
                .height(new BigDecimal("10"))
                .declaredValue(new BigDecimal("45000"))
                .build();
        
        when(shipmentRepository.save(any(Shipment.class)))
        .thenReturn(savedShipment);

        ShipmentResponse response =
        shippingService.createShipment(request);
        
        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("QS123456", response.getTrackingNumber());
        
        
        verify(userRepository)
        .findByEmail("customer@gmail.com");

        verify(addressRepository, times(2))
        .save(any(Address.class));

        verify(shipmentRepository)
        .save(any(Shipment.class));	
    }
    
    @Test
    void createShipment_shouldThrowExceptionWhenCustomerNotFound()
    {
    	Authentication authentication=Mockito.mock(Authentication.class);
    	
    	when(authentication.getName())
    					.thenReturn("unknown@gmail.com");
    	SecurityContextHolder.getContext()
         				.setAuthentication(authentication);
    	 
    	 when(userRepository.findByEmail("unknown@gmail.com"))
            			.thenReturn(Optional.empty());
    	 
    	 CreateShipmentRequest request=CreateShipmentRequest.builder().build();
    	 
    	  assertThrows(
    	            UserNotFoundException.class,
    	            () -> shippingService.createShipment(request)
    	  );
    }
}