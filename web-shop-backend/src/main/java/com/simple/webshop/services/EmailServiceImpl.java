package com.simple.webshop.services;

import com.simple.webshop.domain.Order;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${shop.mail-receiver}")
    private String mailReceiver;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    private static final String SENDER = "mismancelaleddin@gmail.com";

    private Map<String, String> sendMail(String to, String subject, String htmlBody) {
        Map<String, String> result = new HashMap<>();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
            result.put("success", "true");
        } catch (Exception e) {
            result.put("success", "false");
            result.put("error", e.getMessage());
        }
        return result;
    }

    public Map<String, String> sendMailWithTemplate(String to, String subject, String templateName, Map<String, Object> templateModel) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(templateName, thymeleafContext);
        return sendMail(to, subject, htmlBody);
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, String> adminOrderMail(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", order.getId());
        params.put("clientName", order.getClientName());
        params.put("clientAddress", order.getClientAddress());
        params.put("productValue", order.getTotalProductValue().toString());
        params.put("shippingValue", order.getTotalShippingValue().toString());
        return sendMailWithTemplate(mailReceiver, "New Order", "order-admin.html", params);
    }
}
