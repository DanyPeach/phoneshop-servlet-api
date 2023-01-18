package com.es.phoneshop.model.viewedProduct;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface ViewedProductsService {
    List<Product> getViewedProducts(HttpSession session);
    void addProductToViewedList(HttpSession session, Product product);
}
