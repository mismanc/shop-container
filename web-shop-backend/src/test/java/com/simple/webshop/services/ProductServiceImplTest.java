package com.simple.webshop.services;

import com.simple.webshop.domain.Brand;
import com.simple.webshop.domain.Product;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @DisplayName("Should Get All Products")
    void shouldGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(Product.builder().name("Yoğurt").price(new BigDecimal("30.00")).brand(Brand.builder().name("Torku").build()).build());
        productList.add(Product.builder().name("Short").price(new BigDecimal("424.99")).brand(Brand.builder().name("Mavi").build()).build());
        productList.add(Product.builder().name("TV OLED65G36 65 inch").brand(Brand.builder().name("LG").build()).price(new BigDecimal("96029.10")).build());
        when(productRepository.findWithBrandAllBy()).thenReturn(productList);

        List<ProductDTO> productDTOS = productService.getAllProducts();
        assertThat(productDTOS).isNotNull();
        assertThat(productDTOS.size()).isEqualTo(productList.size());
    }

}
