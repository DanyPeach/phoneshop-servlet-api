package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    @Mock
    private ProductDao productDao;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Product product;
    @Mock
    private Cart cart;
    @InjectMocks
    private CartService cartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(new CartItem(product, 4));
        when(cart.getCartItems()).thenReturn(cartItems);
        when(productDao.get(1L)).thenReturn(product);
        when(product.getId()).thenReturn(1L);
        when(product.getStock()).thenReturn(100);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(600));
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(null);
        cartService.getCart(request);
        verify(session).setAttribute(eq(CART_SESSION_ATTRIBUTE), any(Cart.class));
    }

    @Test
    public void testAddProduct() throws OutOfStockException {
        cartService.add(cart, 1L, 2);
        assertEquals(1, cart.getCartItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddOutOfStockProduct() throws OutOfStockException {
        cartService.add(cart, 1L, 500);
    }
}