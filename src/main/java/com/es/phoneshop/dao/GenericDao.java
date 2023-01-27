package com.es.phoneshop.dao;

public interface GenericDao<T> {
    T get(Long id);
    void save(T t);
}
