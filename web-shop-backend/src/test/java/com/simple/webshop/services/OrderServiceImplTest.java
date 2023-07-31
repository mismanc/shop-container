package com.simple.webshop.services;

import com.simple.webshop.controller.errorhandler.CardNotValidException;
import com.simple.webshop.domain.Order;
import com.simple.webshop.model.CardDTO;
import com.simple.webshop.model.OrderDTO;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.model.ShippingOption;
import com.simple.webshop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    EmailServiceImpl emailService;

    @Test
    void saveOrder() {
        Order order = createOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(emailService.adminOrderMail(any(Order.class)))
                .thenReturn(Collections.singletonMap("success","true"));
        OrderDTO orderDTO = OrderDTO.builder()
                .name(order.getClientName())
                .address(order.getClientAddress())
                .card(createCard())
                .products(productDTOList())
                .shippingOption(ShippingOption.STANDARD_FREE)
                .build();
        OrderDTO savedOrder = orderService.saveOrder(orderDTO);
        assertNotNull(savedOrder.getId());
        assertEquals(savedOrder.getName(), orderDTO.getName());
        assertEquals(savedOrder.getAddress(), orderDTO.getAddress());
    }

    @Test
    void calculateTotalProductValue() {
        BigDecimal totalProduct = orderService.calculateTotalProductValue(productDTOList());
        assertEquals(totalProduct, new BigDecimal("354.00"));
    }

    @Test
    void cardValidator() {
        assertTrue(orderService.validateCard("5555555555554444", "10/24"));
        Exception exception = assertThrows(CardNotValidException.class, () -> {
            orderService.validateCard("5555555555554444", "10/19");
        });
        String expectedMessage = "Card information is not valid";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void calculateTotalShippingValue() {
        OrderDTO orderDTO = OrderDTO.builder()
                .name("John Doe")
                .address("Türkiye")
                .shippingOption(ShippingOption.STANDARD_FREE)
                .build();

        assertEquals(orderService.calculateTotalShippingValue(orderDTO.getShippingOption()), BigDecimal.ZERO);

        orderDTO.setShippingOption(ShippingOption.EXPRESS);
        assertEquals(orderService.calculateTotalShippingValue(orderDTO.getShippingOption()), BigDecimal.valueOf(10));
    }

    private Order createOrder() {
        return Order.builder()
                .id(1L)
                .clientName("John Doe")
                .clientAddress("Türkiye")
                .totalProductValue(BigDecimal.valueOf(200))
                .totalShippingValue(BigDecimal.ZERO)
                .build();
    }

    private List<ProductDTO> productDTOList(){
        ProductDTO p1 = new ProductDTO(1L, "Macbook Pro", new BigDecimal("100"), "Apple");
        ProductDTO p2 = new ProductDTO(2L, "Yoğurt", new BigDecimal("200"), "Torku");
        return Arrays.asList(p1, p2);
    }

    private CardDTO createCard() {
        return CardDTO.builder().cardNumber("5555555555554444")
                .expirationDate("10/24").build();
    }

}
