package com.melon.service;

import com.melon.entity.Cart;
import com.melon.entity.Item;
import reactor.core.publisher.Mono;

public interface ItemService {

    Mono<Boolean> saveItemToCart(Item item, String cart);
    Mono<Item> getItemFromCart(String itemId, String cartId);
    Mono<Long> removeItemFromCart(String itemId, String cartId);
    Mono<Cart> getCart(String cartId);
    Mono<Cart> createCart(Cart cart);
}
