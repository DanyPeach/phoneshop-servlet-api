package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {

    private List<Order> orders;
    private long orderId;
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    private static class SingeltonHelper{
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }


    public static synchronized OrderDao getInstance() {
       return SingeltonHelper.INSTANCE;
    }

}
