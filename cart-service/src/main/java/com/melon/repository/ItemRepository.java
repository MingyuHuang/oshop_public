package com.melon.repository;

import com.melon.entity.Cart;
import com.melon.entity.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository {

    Mono<Boolean> saveItemToCart(String itemId, String cartId, Item item);
    Mono<Item> getItemFromCart(String itemId, String cartId);
    Mono<Long> removeItemFromCart(String itemId, String cartId);
    Mono<Cart> createCart(String cartId);
    Mono<Void> clearCart(String cartId);
    Mono<Cart> getCart(String cartId);
//    @Query(value = "from Item i where i.id = :productId and i.cart = :cart")
//    Mono<Item> findItem(@Param("productId") Long productId, @Param("cart") ShoppingCart cart);
}
