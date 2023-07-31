package com.simple.webshop.model;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, BigDecimal price, String brandName) {
}
