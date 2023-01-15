package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;

public interface CartService {
    Cart getCart();
    void add(long productId, int Quantity) throws OutOfStockException;
}
