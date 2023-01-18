package com.es.phoneshop.model.viewedProduct;

import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

public class ViewedProductServiceImpl implements ViewedProductsService {
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
    public synchronized List<Product> getViewedProducts(HttpSession session) {
        List<Product> viewedProducts = (List<Product>) session.getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
        if (viewedProducts == null) {
            viewedProducts = new LinkedList<>();
            session.setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedProducts);
        }
        return viewedProducts;
    }

    @Override
    public synchronized void addProductToViewedList(HttpSession session, Product product) {
        LinkedList<Product> viewedProduts = (LinkedList<Product>) getViewedProducts(session);
        if (viewedProduts.contains(product)) {
            viewedProduts.remove(product);
            viewedProduts.addFirst(product);
        } else {
            if (viewedProduts.size() >= 3) {
                viewedProduts.removeLast();
            }
            viewedProduts.addFirst(product);
        }
        session.setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedProduts);
    }
}
