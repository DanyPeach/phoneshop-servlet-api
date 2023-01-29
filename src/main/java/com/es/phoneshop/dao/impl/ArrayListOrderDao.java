package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {

    private static class SingletonHelper{
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    private ArrayListOrderDao() {
        this.items = new ArrayList<>();
    }


    public static synchronized OrderDao getInstance() {
       return SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrderBySecureId(String securId) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try{
            return items.stream()
                    .filter(order -> securId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        }finally {
            lock.unlock();
        }
    }

}
