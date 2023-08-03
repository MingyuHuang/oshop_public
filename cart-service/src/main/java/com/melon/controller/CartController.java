package com.melon.controller;

import com.melon.entity.Cart;
import com.melon.entity.Item;
import com.melon.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/shopping-carts")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cart> createCart(@RequestBody Cart cart) {
        Mono<Cart> newCart = cartService.createCart(cart);

        return newCart;
    }

    @DeleteMapping("/{cartId}/items")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> deleteCart(@PathVariable("cartId") String cartId){

        cartService.clearCart(cartId);
        return Mono.empty();
    }
    @GetMapping("/{cartId}")
    public Mono<Void> pushCartToQueue(@PathVariable String cartId) {

        return cartService.pushCartToQueue(cartId);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public Mono<Cart> updateItem(@PathVariable("cartId") String cartId, @PathVariable("itemId") String itemId, @RequestBody Item item) {

        item.setId(itemId);
        return cartService.saveItemToCart(item, cartId);
    }

    @GetMapping("/{cartId}/items/{productId}")
    public Mono<Item> getItem(@PathVariable("cartId") String cartId, @PathVariable("productId") String productId) {

        return cartService.getItemFromCart(productId, cartId);
    }
}
