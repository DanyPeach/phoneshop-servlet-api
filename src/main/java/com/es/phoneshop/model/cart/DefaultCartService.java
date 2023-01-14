package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService{

    private Cart cart = new Cart();
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingeltonHelper{
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static synchronized DefaultCartService getInstance() {
        return DefaultCartService.SingeltonHelper.INSTANCE;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(long productId, int quantity) {
        Product product = productDao.getProduct(productId);
        cart.getCartItems().add(new CartItem(product, quantity));
    }
}
