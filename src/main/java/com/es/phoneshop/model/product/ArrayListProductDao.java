package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.util.ProductComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static ProductDao instance;

    private List<Product> products;
    private long maxId;
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(String.valueOf(id)));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return products.stream()
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
    public void save(Product product) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            if (product.getId() == null) {
                maxId++;
                product.setId(maxId);
                products.add(product);
            } else {
                int index = products.indexOf(product);
                products.set(index, product);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            products.removeIf(product -> id.equals(product.getId()));
        } finally {
            lock.unlock();
        }
    }

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

}
