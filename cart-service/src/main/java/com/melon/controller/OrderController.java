package com.melon.controller;

import com.melon.entity.Order;
import com.melon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public Order addOrder(@RequestBody Order order){
        return orderService.saveOrder(order);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<Order>> getOrdersByUserId(@PathVariable("userId") String userId){

        return orderService.getOrderByUserId(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<Order>> getAllOrders(){

        return orderService.getAllOrders();
    }

    @GetMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Order getOrderById(@PathVariable("orderId") Long id){

        return orderService.getOrderById(id);
    }

    @DeleteMapping("{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> deleteOrder(@PathVariable("orderId") Long orderId){

        return orderService.deleteOrder(orderId);
    }
}
