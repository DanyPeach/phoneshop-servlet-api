package com.es.phoneshop.web;


import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @Mock
    private List<PaymentMethod> paymentMethods;
    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.getOrder(cart)).thenReturn(order);
        when(orderService.getPayment()).thenReturn(paymentMethods);
    }

    @Test
    public void testDoPostWithPassedValidation() throws ServletException, IOException {
        setupRequestParams("Name", "Surname", "Phone", "2022-02-01", "Address", String.valueOf(PaymentMethod.CASH));
        servlet.doPost(request, response);
        verify(orderService, times(1)).placeOrder(order);
        verify(cartService, times(1)).clear(cart);
        verify(response, times(1)).sendRedirect(any());
        verify(requestDispatcher, never()).forward(request, response);
    }

    @Test
    public void testDoPostWithNoPaymentMethod() throws ServletException, IOException {
        setupRequestParams("Name", "Surname", "Phone", "2021-02-22", "Address", "");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(any());
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoPostWithEmptyFields() throws ServletException, IOException {
        setupRequestParams(null, null, null, null, null, null);
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(any());
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    private void setupRequestParams(String firstName, String lastName, String phone, String date, String address, String paymentMethod) {
        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("lastName")).thenReturn(lastName);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("deliveryDate")).thenReturn(date);
        when(request.getParameter("deliveryAddress")).thenReturn(address);
        when(request.getParameter("paymentMethod")).thenReturn(paymentMethod);
    }

}

