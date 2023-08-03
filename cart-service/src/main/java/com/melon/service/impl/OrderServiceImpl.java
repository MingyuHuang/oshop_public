package com.melon.service.impl;

import com.melon.configuration.OrderStatus;
import com.melon.entity.Order;
import com.melon.entity.OrderItem;
import com.melon.entity.Shipping;
import com.melon.exception.OrderExceptionMessage;
import com.melon.repository.OrderItemRepository;
import com.melon.repository.OrderRepository;
import com.melon.repository.ShippingRepository;
import com.melon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository itemRepository;
    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public Order saveOrder(Order order) {

        Order newOrder = new Order();
        Set<OrderItem> items = order.getItems();
        for (OrderItem item: items){
            newOrder.addItem(item);
        }
        newOrder.setShipping(order.getShipping());
        newOrder.setOrderStatus(OrderStatus.UNPAID.getStatus());
        newOrder.setDatePlaced(order.getDatePlaced());
        newOrder.setUserId(order.getUserId());
        Order orderSaved = orderRepository.saveAndFlush(newOrder);
        return orderSaved;
    }

    @Override
    public void updateOrderStatus(Long orderId, String orderStatus) {

        try{

            orderRepository.updateOrderStatus(orderId, orderStatus);
        }catch (Exception exception){
//            log.error(exception.getLocalizedMessage());
        }
    }

    @Override
    public void updateOrderPaymentMethod(Long orderId, String paymentMethod) {

        try {
            orderRepository.updateOrderPaymentMethod(orderId, paymentMethod);
        }catch (Exception exception){

        }
    }

    @Override
    public Mono<List<Order>> getOrderByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId).orElse(null);
        return Mono.justOrEmpty(orders);
    }

    @Override
    public Mono<List<Order>> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return Mono.justOrEmpty(orders);
    }


    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException(OrderExceptionMessage.NO_ORDER.getMessage()));
    }

    @Override
    public Mono<Void> deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return Mono.empty();
    }
}
