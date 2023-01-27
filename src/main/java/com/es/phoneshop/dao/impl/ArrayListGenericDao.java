package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.model.genericitem.GenericItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ArrayListGenericDao<T extends GenericItem> implements GenericDao<T> {
    protected List<T> items = new ArrayList<>();
    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected long id;

    @Override
    public T get(Long id) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
        return items.stream()
                .filter(item -> id.equals(item.getId()))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void save(T t) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            if (t.getId() == null) {
                ++id;
                t.setId(id);
                items.add(t);
            } else {
                if (items.contains(t)) {
                    int currentItem = items.indexOf(t);
                    items.set(currentItem, t);
                } else {
                    items.add(t);
                }
            }
        }finally {
            lock.unlock();
        }
    }
}
