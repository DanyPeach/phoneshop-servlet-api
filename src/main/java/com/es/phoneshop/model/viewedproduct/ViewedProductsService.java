package com.es.phoneshop.model.viewedproduct;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ViewedProductsService {
    List<Product> getViewedProducts(HttpSession session);
    void addProductToViewedList(HttpSession session, Product product);
}
