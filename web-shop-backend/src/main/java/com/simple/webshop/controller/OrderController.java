package com.simple.webshop.controller;

import com.simple.webshop.model.OrderDTO;
import com.simple.webshop.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("orders")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderDTO));
    }


}
