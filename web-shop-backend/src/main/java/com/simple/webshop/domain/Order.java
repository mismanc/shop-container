package com.simple.webshop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
    @GenericGenerator(
            name = "order_id_gen",
            type = org.hibernate.id.enhanced.SequenceStyleGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5"),
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled")
            }
    )
    private Long id;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal totalProductValue;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal totalShippingValue;

    @Column(nullable = false, length = 200)
    private String clientName;

    @Column(nullable = false, length = 5000)
    private String clientAddress;

}
