package com.quickship.shipment.dto.request;

import java.math.BigDecimal;

import com.quickship.shipment.enums.PackageType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShipmentRequest {

    @Valid
    @NotNull(message = "Sender address is required")
    private AddressRequest sender;

    @Valid
    @NotNull(message = "Recipient address is required")
    private AddressRequest recipient;

    @NotNull(message = "Package type is required")
    private PackageType packageType;

    @NotNull(message = "Weight is required")
    @DecimalMin(
        value = "0.001",
        message = "Weight must be greater than zero"
    )
    private BigDecimal weight;

    @NotNull(message = "Length is required")
    @DecimalMin(
        value = "0.1",
        message = "Length must be greater than zero"
    )
    private BigDecimal length;

    @NotNull(message = "Width is required")
    @DecimalMin(
        value = "0.1",
        message = "Width must be greater than zero"
    )
    private BigDecimal width;

    @NotNull(message = "Height is required")
    @DecimalMin(
        value = "0.1",
        message = "Height must be greater than zero"
    )
    private BigDecimal height;

    @DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Declared value cannot be negative"
    )
    private BigDecimal declaredValue;
}