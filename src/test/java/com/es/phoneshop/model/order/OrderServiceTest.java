package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Mock
    private Order order;
    @Mock
    private Cart cart;
    @Mock
    private OrderDao orderDao;
    @Mock
    private CartItem firstItem;
    @Mock
    private CartItem secondItem;
    @Mock
    private Set<CartItem> items;
    @InjectMocks
    private OrderService orderService = DefaultOrderService.getInstance();

    @Before
    public void setup() {
        items.add(firstItem);
        items.add(secondItem);

        when(cart.getCartItems()).thenReturn(items);
        when(cart.getTotalCost()).thenReturn(BigDecimal.valueOf(105));
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);
        assertEquals(cart.getTotalCost(), order.getSubTotal());
    }


    @Test
    public void testPlaceOrder() {
        orderService.placeOrder(order);
        verify(order).setSecureId(anyString());
        verify(orderDao).save(order);
    }
}
