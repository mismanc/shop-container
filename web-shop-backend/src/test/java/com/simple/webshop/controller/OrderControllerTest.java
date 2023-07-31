package com.simple.webshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.webshop.domain.Order;
import com.simple.webshop.model.CardDTO;
import com.simple.webshop.model.OrderDTO;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.model.ShippingOption;
import com.simple.webshop.services.EmailService;
import com.simple.webshop.services.OrderService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldFailProductsNull() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .address("address").name("John Doe")
                .card(createCard())
                .shippingOption(ShippingOption.EXPRESS)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .content(objectMapper.writeValueAsBytes(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(400));

    }

    @Test
    public void shouldFailShippingNotExist() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .address("address").name("John Doe")
                .card(createCard())
                .products(Collections.singletonList(createProduct()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .content(objectMapper.writeValueAsBytes(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void shouldFailIdNotNull() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .address("address").name("John Doe")
                .card(createCard())
                .shippingOption(ShippingOption.EXPRESS)
                .id(4L)
                .products(Collections.singletonList(createProduct()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .content(objectMapper.writeValueAsBytes(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void shouldBeCreated() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .address("address").name("John Doe")
                .shippingOption(ShippingOption.EXPRESS)
                .card(createCard())
                .products(Collections.singletonList(createProduct()))
                .build();

        OrderDTO returned = OrderDTO.builder()
                .address("address").name("John Doe")
                .id(4L)
                .shippingOption(ShippingOption.EXPRESS)
                .products(Collections.singletonList(createProduct()))
                .build();


        when(orderService.saveOrder(any(OrderDTO.class))).thenReturn(returned);
        when(emailService.adminOrderMail(any(Order.class))).thenReturn(Collections.singletonMap("success", "true"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .content(objectMapper.writeValueAsBytes(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingOption", Matchers.is(ShippingOption.EXPRESS.name())));
    }

    private ProductDTO createProduct() {
        return new ProductDTO(2L, "Macbook Pro", new BigDecimal("80000.00"), "Apple");
    }

    private CardDTO createCard() {
        return CardDTO.builder().cardNumber("5555555555554444")
                .expirationDate("10/24").ccv("123").build();
    }

}
