package com.simple.webshop.services;

import com.simple.webshop.controller.errorhandler.CardNotValidException;
import com.simple.webshop.domain.Order;
import com.simple.webshop.model.OrderDTO;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.model.ShippingOption;
import com.simple.webshop.repository.OrderRepository;
import com.simple.webshop.util.CardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        validateCard(orderDTO.getCard().getCardNumber(), orderDTO.getCard().getExpirationDate());
        Order order = Order.builder()
                .totalProductValue(calculateTotalProductValue(orderDTO.getProducts()))
                .totalShippingValue(calculateTotalShippingValue(orderDTO.getShippingOption()))
                .clientAddress(orderDTO.getAddress())
                .clientName(orderDTO.getName())
                .build();
        Order savedOrder = orderRepository.save(order);
        orderDTO.setId(savedOrder.getId());
        emailService.adminOrderMail(savedOrder);
        return orderDTO;
    }

    public boolean validateCard(String number, String expirationDate) {
        number = number.replaceAll("\\s", "");
        expirationDate = expirationDate.replaceAll("\\s", "");
        LocalDate cardExpirationDate = LocalDate.parse("01/" + expirationDate, df);
        if (!CardValidator.check(number) || cardExpirationDate.isBefore(LocalDate.now())) {
            throw new CardNotValidException();
        }
        return true;
    }

    public BigDecimal calculateTotalProductValue(List<ProductDTO> products) {
        BigDecimal kdv = new BigDecimal("1.18");
        // Add KDV and calculate total
        return products.stream().map(p -> p.price().multiply(kdv)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalShippingValue(ShippingOption shippingOption) {
        return shippingOption.equals(ShippingOption.STANDARD_FREE) ? BigDecimal.ZERO : BigDecimal.valueOf(10);
    }
}
