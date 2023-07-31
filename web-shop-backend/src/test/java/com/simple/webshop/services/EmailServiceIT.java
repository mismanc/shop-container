package com.simple.webshop.services;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.simple.webshop.domain.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailServiceIT {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @Autowired
    EmailService emailService;

    @Value("${shop.mail-receiver}")
    private String mailReceiver;

    @Test
    void shouldSendAdminOrderEmail() throws MessagingException {
        Map<String, String> result = emailService.adminOrderMail(createOrder());
        assertEquals("true", result.get("success"));
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(1, receivedMessage.getAllRecipients().length);
        assertEquals(mailReceiver, receivedMessage.getAllRecipients()[0].toString());
        assertEquals("New Order", receivedMessage.getSubject());
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

}
