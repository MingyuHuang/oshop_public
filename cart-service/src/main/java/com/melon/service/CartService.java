package com.melon.service;

import com.melon.dto.CartInQueue;
import com.melon.entity.Cart;
import com.melon.entity.Item;
import reactor.core.publisher.Mono;

public interface CartService {

    Mono<Cart> saveItemToCart(Item item, String cart);
    Mono<Item> getItemFromCart(String itemId, String cartId);
    Mono<Cart> createCart(Cart cart);
    Cart getCartById(String cartId);
    Mono<Void> pushCartToQueue(String cartId);
    void clearCart(String cartId);
}
