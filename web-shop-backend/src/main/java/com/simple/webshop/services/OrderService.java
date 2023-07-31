package com.simple.webshop.services;

import com.simple.webshop.model.OrderDTO;

public interface OrderService {

    OrderDTO saveOrder(OrderDTO orderDTO);

}
