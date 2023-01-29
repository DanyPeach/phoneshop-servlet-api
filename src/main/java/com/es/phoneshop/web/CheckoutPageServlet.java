package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.web.utils.ExceptionHandler;
import com.es.phoneshop.web.utils.ItemPropertyParsing;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {
    private static final String CHECKOUT_PAGE = "/WEB-INF/pages/checkout.jsp";
    private ProductDao productDao;
    private CartService cartService;
    private OrderService orderService;
    private ExceptionHandler exceptionHandler;
    private ItemPropertyParsing itemPropertyParsing;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        req.setAttribute("order", orderService.getOrder(cart));
        req.setAttribute("paymentMethods", orderService.getPayment());
        req.getRequestDispatcher(CHECKOUT_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        String firstname = checkRequiredParam(req, "firstName", errors);
        String lastname = checkRequiredParam(req, "lastName", errors);
        String phone = checkRequiredParam(req, "phone", errors);
        String deliveryAddress = checkRequiredParam(req, "deliveryAddress", errors);

        LocalDate deliveryDate = checkDeliveryDate(req, errors);
        PaymentMethod paymentMethod = readPaymentMethod(req, errors);

        if(errors.isEmpty()){
            setOrder(order, firstname, lastname, phone, deliveryDate, deliveryAddress, paymentMethod);
            orderService.placeOrder(order);
            cartService.clear(cart);
            resp.sendRedirect(req.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        exceptionHandler = new ExceptionHandler();
        itemPropertyParsing = new ItemPropertyParsing();
    }

    private String checkRequiredParam(HttpServletRequest request, String param, Map<String, String> errors) {
        String value = request.getParameter(param);
        if (value == null || value.isEmpty()) {
            errors.put(param, "value is required");
            return null;
        } else {
            return value;
        }
    }

    private LocalDate checkDeliveryDate(HttpServletRequest request, Map<String, String> errors) {
        String deliveryDate = checkRequiredParam(request, "deliveryDate", errors);
        if (deliveryDate != null) {
            try {
                return LocalDate.parse(deliveryDate);
            } catch (DateTimeParseException e) {
                errors.put("deliveryDate", "wrong date format(yyyy-mm-dd)");
            }
        }
        return null;
    }

    private PaymentMethod readPaymentMethod(HttpServletRequest request, Map<String, String> errors) {
        String paymentMethod = checkRequiredParam(request, "paymentMethod", errors);
        if (paymentMethod != null) {
            try {
                return PaymentMethod.valueOf(paymentMethod);
            } catch (IllegalArgumentException | NullPointerException e) {
                errors.put("paymentMethod", "wrong payment method");
            }
        }
        return null;
    }

    private void setOrder(Order order, String firstname, String lastname, String phone,
                               LocalDate deliveryDate, String deliveryAddress, PaymentMethod paymentMethod) {
        order.setFirstName(firstname);
        order.setLastName(lastname);
        order.setPhone(phone);
        order.setDeliveryDate(deliveryDate);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
    }
}
