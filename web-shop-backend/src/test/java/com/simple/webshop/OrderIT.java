package com.simple.webshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.simple.webshop.domain.Order;
import com.simple.webshop.model.CardDTO;
import com.simple.webshop.model.OrderDTO;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.model.ShippingOption;
import com.simple.webshop.repository.OrderRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIT {

    @Value("${shop.mail-receiver}")
    String mailReceiver;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderRepository orderRepository;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @Test
    public void shouldBeCreated() throws Exception {
        ResponseEntity<ProductDTO[]> responseProducts = testRestTemplate.getForEntity("/api/v1/products", ProductDTO[].class);
        assertNotNull(responseProducts.getBody());
        assertNotEquals(0, responseProducts.getBody().length);

        ProductDTO p1 = responseProducts.getBody()[0];
        ProductDTO p2 = responseProducts.getBody()[1];

        OrderDTO orderDTO = OrderDTO.builder()
                .address("address").name("John Doe")
                .shippingOption(ShippingOption.EXPRESS)
                .card(createCard())
                .products(Arrays.asList(p1,p2))
                .build();

        ResponseEntity<OrderDTO> responseOrder = testRestTemplate.postForEntity("/api/v1/orders", orderDTO, OrderDTO.class);
        OrderDTO returnOrderDTO = responseOrder.getBody();

        assertNotNull(returnOrderDTO);
        Optional<Order> orderOptional = orderRepository.findById(returnOrderDTO.getId());
        assertTrue(orderOptional.isPresent());
        Order order = orderOptional.get();

        assertEquals(order.getId(), returnOrderDTO.getId());
        assertEquals(order.getClientName(), returnOrderDTO.getName());
        assertEquals(p1.price().add(p2.price()).multiply(BigDecimal.valueOf(1.18))
                .setScale(2, RoundingMode.HALF_UP), order.getTotalProductValue());

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(1, receivedMessage.getAllRecipients().length);
        assertEquals(mailReceiver, receivedMessage.getAllRecipients()[0].toString());
        assertEquals("New Order", receivedMessage.getSubject());
    }


    private CardDTO createCard() {
        return CardDTO.builder().cardNumber("5555555555554444")
                .expirationDate("10/24").ccv("123").build();
    }

}
