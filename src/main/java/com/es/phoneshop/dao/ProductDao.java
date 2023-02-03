package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    void delete(Long id);
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
}
