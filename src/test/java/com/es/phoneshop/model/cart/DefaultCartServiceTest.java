package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class DefaultCartServiceTest {
    private ProductDao productDao;
    private CartService cartService;

    @Before
    public void setup() {
        productDao =  ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Test
    public void testFindProductsNoResults() throws OutOfStockException {
        Product product = new Product();
        CartItem cartItem = new CartItem(product, 1);
        productDao.save(product);
        cartService.add(product.getId(), 1);
        assertTrue(cartService.getCart().getCartItems().contains(cartItem));
    }
}
