package com.simple.webshop.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_id_gen")
    @SequenceGenerator(name = "brand_id_gen", sequenceName = "brand_id_seq")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

}
