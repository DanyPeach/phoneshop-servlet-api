package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.*;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest request = null;
        CartItem cartItem = new CartItem(product, 1);
        productDao.save(product);
        cartService.add(cartService.getCart(request), product.getId(), 1);
        assertTrue(cartService.getCart(request).getCartItems().contains(cartItem));
    }
}
