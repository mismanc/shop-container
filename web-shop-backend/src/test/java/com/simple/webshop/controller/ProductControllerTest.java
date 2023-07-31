package com.simple.webshop.controller;

import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.services.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    @DisplayName("Should List All Products Get Request to /api/v1/products")
    public void shouldListAllProducts() throws Exception {
        ProductDTO p1 = new ProductDTO(1L, "Moon Çikolata",new BigDecimal("35.22"),"Torku");
        ProductDTO p2 = new ProductDTO(2L, "Macbook Pro",new BigDecimal("80000.00"),"Apple");
        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1,p2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Macbook Pro")))
        ;


    }

}
