package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.web.utils.ExceptionHandler;
import com.es.phoneshop.web.utils.ItemPropertyParsing;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private ItemPropertyParsing itemPropertyParsing;
    @Mock
    private ExceptionHandler exceptionHandler;
    @Mock
    private Cart cart;

    @InjectMocks
    private final CartPageServlet servlet = new CartPageServlet();


    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(new Locale("ru"));
        when(cartService.getCart(request)).thenReturn(cart);

    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(cartService).getCart(request);
        verify(request).setAttribute("cart",cart);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostParseException() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1", "2", "3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"qwerty", "10", "20"});

        servlet.doPost(request, response);

        Map<Long, String> errors = new HashMap<>();
        errors.put(1L, "not a number");
        verify(request).getParameterValues("quantity");
        verify(request).getParameterValues("productId");
        verify(request).setAttribute("errors", errors);
    }


}