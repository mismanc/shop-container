package com.simple.webshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardDTO {

    @NotBlank
    private String cardNumber;

    @NotBlank
    @Pattern(regexp = "(0[1-9]|10|11|12)/[0-9]{2}$")
    private String expirationDate;

    @NotBlank
    @Pattern(regexp = "^[0-9]{3,4}$")
    private String ccv;

}
