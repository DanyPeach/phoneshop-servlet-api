package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.util.ProductComparator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public List<Product> advancedSearch(String query, String searchOption,BigDecimal minPrice, BigDecimal maxPrice) {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            if (query == null
                    && searchOption == null
                    && minPrice == null
                    && maxPrice == null) {
                return new ArrayList<>();
            }
            if (SearchOption.valueOf(searchOption) == SearchOption.ANY_WORDS) {
                Stream<Product> productStream = filterIfValueNotNull(items.stream(), minPrice, item -> minPrice.compareTo(item.getPrice()) < 0);
                productStream = filterIfValueNotNull(productStream, minPrice, item -> maxPrice.compareTo(item.getPrice()) >0);
                return productStream.collect(Collectors.toList());
            } else {
                return findProducts(query, SortField.description, SortOrder.asc);
            }
        } finally {
            readLock.unlock();
        }
    }

    private Stream<Product> filterIfValueNotNull(Stream<Product> productStream, BigDecimal value, Predicate<? super Product> predicate) {
        if(value != null) {
            return productStream.filter(predicate);
        } else return productStream;
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
