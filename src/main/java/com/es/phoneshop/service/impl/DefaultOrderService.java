package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static class SingeltonHelper {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }

    public static synchronized DefaultOrderService getInstance() {
        return DefaultOrderService.SingeltonHelper.INSTANCE;
    }


    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setCartItems(cart.getCartItems().stream().map(cartItem -> new CartItem(cartItem.getProduct(), cartItem.getQuantity())).collect(Collectors.toSet()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getDeliveryCost().add(order.getSubTotal()));
        return order;
    }

    @Override
    public List<PaymentMethod> getPayment() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost(){
        return new BigDecimal(5);
    }
}
