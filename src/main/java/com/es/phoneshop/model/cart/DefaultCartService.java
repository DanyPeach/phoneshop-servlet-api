package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.Optional;

public class DefaultCartService implements CartService {

    private Cart cart = new Cart();
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingeltonHelper {
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
    public void add(long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        Optional<CartItem> optionalCartItem = getCartItemFromCart(productId, cart);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            if (!checkFullStock(product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            } else {
                if (cart.getCartItems().contains(cartItem)) {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                }
            }
        } else{
            cart.getCartItems().add(new CartItem(product, quantity));
        }
    }

    private boolean checkFullStock(Product product, int quantity){
        int productQuantity = product.getStock();
        int inCartProductQuantity = 0;
        Optional<CartItem> optionalCartItem = getCartItemFromCart(product.getId(), cart);
        if (optionalCartItem.isPresent()) {
            inCartProductQuantity = optionalCartItem.get().getQuantity();
        }
        return productQuantity - inCartProductQuantity > quantity;
    }

    private Optional<CartItem> getCartItemFromCart(long productId, Cart cart) {
        return cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny();
    }
}
