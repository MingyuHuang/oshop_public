package com.melon.service;

import com.melon.entity.Order;
import reactor.core.publisher.Mono;
import java.util.*;

public interface OrderService {

    Order saveOrder(Order order);
    void updateOrderStatus(Long orderId, String orderStatus);
    void updateOrderPaymentMethod(Long orderId, String paymentMethod);
    Mono<List<Order>> getOrderByUserId(String userId);
    Mono<List<Order>> getAllOrders();
    Order getOrderById(Long id);
    Mono<Void> deleteOrder(Long orderId);
}
