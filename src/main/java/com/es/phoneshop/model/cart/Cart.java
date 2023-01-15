package com.es.phoneshop.model.cart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cart {
    private Set<CartItem> cartItems;

    public Cart() {
        cartItems = new HashSet<>();
    }

    public Cart(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartItems=" + cartItems +
                '}';
    }
}
