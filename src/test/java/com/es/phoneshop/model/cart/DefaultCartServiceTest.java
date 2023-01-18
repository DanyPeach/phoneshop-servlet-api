package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.junit.Assert.assertTrue;

public class DefaultCartServiceTest {
    private ProductDao productDao;
    private CartService cartService;
    private final Cart cart = new Cart();

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
        cartService.add(cart, product.getId(), 1);
        assertTrue(cart.getCartItems().contains(cartItem));
    }

    @Test
    public void testAddSameProductToCart() throws OutOfStockException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", new ArrayList<>());
        productDao.save(product);
        cartService.add(cart, product.getId(), 1);
        cartService.add(cart, product.getId(), 1);
        assertTrue(cart.getCartItems().size()==1);
    }
}
