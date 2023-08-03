package com.melon.service.impl;

import com.melon.dto.CartInQueue;
import com.melon.dto.Product;
import com.melon.entity.Cart;
import com.melon.entity.Item;
import com.melon.exception.CartServiceExceptionMessage;
import com.melon.repository.CartRepository;
import com.melon.service.CartService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private final String exchangeName = "direct_exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private CartRepository cartRepository;


    public CartServiceImpl(@Autowired CartRepository cartRepository) {
        this.cartRepository = cartRepository;

    }

    @Override
    public Mono<Cart> saveItemToCart(Item item, String cartId) {

        Cart cart = getCartById(cartId);
        if (cart == null) {
            return Mono.empty();
        }

        Map<String, Item> items = cart.getItems();
        if (items == null) {
            items = new HashMap<>();
        }
        if (item.getQuantity() > 0){

            items.put(item.getId(), item);
        }else{
            items.remove(item.getId());
        }
        cart.setItems(items);
        Cart updatedCart = cartRepository.save(cart);
        rabbitTemplate.convertAndSend(exchangeName, "routingKey2", updatedCart);
        return Mono.just(updatedCart);
    }

    @Override
    public Mono<Item> getItemFromCart(String itemId, String cartId) {

        Cart cart = getCartById(cartId);
        if (cart == null) {
            return Mono.empty();
        }
        Map<String, Item> items = cart.getItems();
        if (items == null || items.isEmpty()) {
            return Mono.empty();
        }
        Item item = items.get(itemId);
        return item == null ? Mono.empty() : Mono.just(item);
    }

    @Override
    public Mono<Cart> createCart(Cart cart) {

        if (cart == null || cart.getId() == null ){

            Cart newCart = cartRepository.save(cart);
            return Mono.justOrEmpty(newCart);
        }
        Cart existedCart = getCartById(cart.getId());
        if (existedCart == null){

            Cart newCart = cartRepository.save(cart);
            return Mono.justOrEmpty(newCart);
        }
        return Mono.just(existedCart);
    }

    @Override
    public Cart getCartById(String cartId) {

        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException(CartServiceExceptionMessage.NO_CART.getMessage()));
    }

    @Override
    public void clearCart(String cartId) {

        Cart cart = getCartById(cartId);
        if (cart != null){

            if (cart.getItems() == null){
                return;
            }
            cart.setItems(null);
        }
        Cart updatedCart = cartRepository.save(cart);
        rabbitTemplate.convertAndSend(exchangeName, "routingKey2", updatedCart);
    }

    @Override
    public Mono<Void> pushCartToQueue(String cartId) {

        Cart cart = getCartById(cartId);
        if (cart != null) {
            rabbitTemplate.convertAndSend(exchangeName, "routingKey2", cart);
        }
        return Mono.empty();
    }


    @RabbitListener(queues = "queue_cartId")
    public void pullCartIdFromQueue(CartInQueue cartInQueue) {

        Cart cart = getCartById(cartInQueue.getId());
        rabbitTemplate.convertAndSend(exchangeName, "routingKey2", cart);
    }

    @RabbitListener(queues = "queue_product")
    public void updateItems(Product product){

        Iterator<Cart> iterator = cartRepository.findAll().iterator();
        while (iterator.hasNext()){
            Cart cart = iterator.next();
            String itemId = product.getId().toString();
            if (itemId == null)
                break;
            Map<String, Item> items = cart.getItems();
            if (items == null)
                break;
            if (!items.containsKey(itemId)) {
                break;
            }
            Item item = items.get(itemId);
            item.setPrice(product.getPrice());
            item.setTitle(product.getTitle());
            item.setImageUrl(product.getImageUrl());
            items.put(itemId, item);
            cart.setItems(items);
            Cart updatedCart = cartRepository.save(cart);
            rabbitTemplate.convertAndSend(exchangeName, "routingKey2", updatedCart);
        }
    }
}
