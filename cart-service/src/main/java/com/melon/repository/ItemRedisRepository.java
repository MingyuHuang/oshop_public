package com.melon.repository;

import com.melon.entity.Cart;
import com.melon.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRedisRepository implements ItemRepository {

    private final ReactiveHashOperations<String, String, Item> hashOperations;

    public ItemRedisRepository(@Autowired ReactiveRedisOperations<String, Item> redisOperations) {

        this.hashOperations = redisOperations.opsForHash();
    }

    @Override
    public Mono<Boolean> saveItemToCart(String itemId, String cartId, Item item) {

        return hashOperations.putIfAbsent("carts:" + cartId, "items:" + itemId, item);
    }

    @Override
    public Mono<Item> getItemFromCart(String itemId, String cartId) {

        return hashOperations.get("carts:" + cartId, "items:" + itemId);
    }

    @Override
    public Mono<Long> removeItemFromCart(String itemId, String cartId) {

        return hashOperations.remove("carts:" + cartId, "items:" + itemId);
    }

    @Override
    public Mono<Cart> createCart(String cartId) {
        Map<String, Item> cart = new HashMap<>();
        cart.put("test", new Item());
        Cart newCart = new Cart();
        newCart.setId(cartId);
        newCart.setItems(cart);
        return hashOperations.putAll(cartId, cart).then(Mono.just(newCart));
    }

    @Override
    public Mono<Void> clearCart(String cartId) {

        return hashOperations.delete(cartId).then();
    }

    @Override
    public Mono<Cart> getCart(String cartId) {

        Cart cart = new Cart();
        hashOperations.entries(cartId).toStream().forEach(entry -> cart.getItems().put(entry.getKey(), entry.getValue()));
        return Mono.just(cart);
    }
}
