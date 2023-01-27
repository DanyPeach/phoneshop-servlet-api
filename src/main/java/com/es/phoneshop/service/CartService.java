package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest servletRequest);
    void add(Cart cart, long productId, int Quantity) throws OutOfStockException;
    void update(Cart cart, long productId, int Quantity) throws OutOfStockException;
    void delete(Cart cart, long productId);
}
