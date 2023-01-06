package com.es.phoneshop.model.product.util;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.Comparator;

public class ProductComparator {
    private static Comparator<Product> comparator;

    public static Comparator<Product> compare(SortField sortField, SortOrder sortOrder){
        comparator = Comparator.comparing(product -> {
            if (sortField != null && SortField.description.equals(sortField)) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if(SortOrder.desc.equals(sortOrder)){
            return comparator.reversed();
        } else {
            return comparator;
        }
    }
}
