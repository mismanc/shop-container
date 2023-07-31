package com.simple.webshop.services;

import com.simple.webshop.domain.Order;

import java.util.Map;

public interface EmailService {

    Map<String, String> adminOrderMail(Order order);

}
