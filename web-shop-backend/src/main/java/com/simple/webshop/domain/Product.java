package com.simple.webshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_gen")
    @SequenceGenerator(name = "product_id_gen", sequenceName = "product_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;
}
