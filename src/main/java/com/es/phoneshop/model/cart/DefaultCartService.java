package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public void add(Cart cart, long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        Optional<CartItem> optionalCartItem = getCartItemFromCart(productId, cart);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            if (!checkFullStock(cart, product, quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
        } else {
            cart.getCartItems().add(new CartItem(product, quantity));
        }
    }

    private boolean checkFullStock(Cart cart, Product product, int quantity) {
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
