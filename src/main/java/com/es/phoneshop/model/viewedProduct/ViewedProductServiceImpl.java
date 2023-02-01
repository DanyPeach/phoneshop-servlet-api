package com.es.phoneshop.model.viewedproduct;

import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

public class ViewedProductServiceImpl implements ViewedProductsService{
    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".viewed";

    private ViewedProductServiceImpl() {
    }

    private static class SingeltonHelper {
        private static final ViewedProductServiceImpl INSTANCE = new ViewedProductServiceImpl();
    }

    public static synchronized ViewedProductServiceImpl getInstance() {
        return ViewedProductServiceImpl.SingeltonHelper.INSTANCE;
    }

    @Override
    public List<Product> getViewedProducts(HttpServletRequest req) {
        List<Product> viewedProducts = (LinkedList<Product>) req.getSession().getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
        if (viewedProducts == null) {
            viewedProducts = new LinkedList<>();
            req.getSession().setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedProducts);
        }
        return viewedProducts;
    }

    @Override
    public void addProductToViewedList(HttpServletRequest req, Product product) {
        LinkedList<Product> viewedProduts = (LinkedList<Product>) getViewedProducts(req);
        if (viewedProduts.contains(product)) {
            viewedProduts.remove(product);
            viewedProduts.addFirst(product);
        } else {
            if (viewedProduts.size() >= 3) {
                viewedProduts.removeLast();
            }
            viewedProduts.addFirst(product);
        }
        req.getSession().setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedProduts);
    }
}
