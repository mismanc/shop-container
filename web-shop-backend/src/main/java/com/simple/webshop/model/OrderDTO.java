package com.simple.webshop.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderDTO {

    @Null
    private Long id;

    @NotBlank(message = "The name is required")
    @Size(min = 2)
    private String name;

    @NotBlank(message = "The address is required")
    @Size(min = 2)
    private String address;

    @NotNull(message = "Shipping option can not be empty")
    private ShippingOption shippingOption;

    @NotEmpty(message = "Products can not be empty")
    private List<ProductDTO> products;

    @Valid
    @NotNull
    private CardDTO card;

}
