package com.es.phoneshop.model.cart;

public interface CartService {
    Cart getCart();
    void add(long productId, int Quantity);
}
