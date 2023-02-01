package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.util.ProductComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

import java.util.stream.Collectors;

public class ArrayListProductDao extends ArrayListGenericDao<Product> implements ProductDao {

    private static class SingeltonHelper{
        private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }

    private ArrayListProductDao() {
        this.items = new ArrayList<>();
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return items.stream()
                    .filter(product -> query == null || isProductMatchingQuery(product, query))
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .sorted(ProductComparator.compare(sortField, sortOrder))
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    private boolean isProductMatchingQuery(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return true;
        }
        return Arrays.stream(query.toLowerCase().split(" "))
                .allMatch(word -> Arrays.asList(product.getDescription().toLowerCase().split(" ")).contains(word));
    }

    @Override
    public void delete(Long id) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            items.removeIf(product -> id.equals(product.getId()));
        } finally {
            lock.unlock();
        }
    }

    public static synchronized ProductDao getInstance() {
       return SingeltonHelper.INSTANCE;
    }

}
