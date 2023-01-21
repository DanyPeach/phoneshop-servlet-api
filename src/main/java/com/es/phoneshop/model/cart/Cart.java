package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cart implements Serializable {
    private Set<CartItem> cartItems;

    private BigDecimal totalCost;
    private int totalQuantity;

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

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
