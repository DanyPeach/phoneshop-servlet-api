package com.es.phoneshop.model.viewedproduct;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ViewedProductsService {
    List<Product> getViewedProducts(HttpServletRequest req);
    void addProductToViewedList(HttpServletRequest req, Product product);
}
