package com.melon.service.impl;

import com.melon.entity.Cart;
import com.melon.entity.Item;
import com.melon.repository.ItemRepository;
import com.melon.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    private RestTemplate restTemplate;
    private final String productServiceUrl = "http://localhost:8082/products/";

    public ItemServiceImpl(){
        this.restTemplate = new RestTemplate();
    }

        @Override
    public Mono<Boolean> saveItemToCart(Item item, String cartId) {

        return itemRepository.saveItemToCart(item.getId(), cartId, item);
    }

    @Override
    public Mono<Item> getItemFromCart(String itemId, String cartId) {

        return itemRepository.getItemFromCart(itemId, cartId);
    }

    @Override
    public Mono<Long> removeItemFromCart(String itemId, String cartId) {

        return itemRepository.removeItemFromCart(itemId, cartId);
    }

    @Override
    public Mono<Cart> getCart(String cartId) {
        return itemRepository.getCart(cartId);
    }

    @Override
    public Mono<Cart> createCart(Cart cart) {
        String cartId = UUID.randomUUID().toString();
        return itemRepository.createCart(cartId);
    }
}
