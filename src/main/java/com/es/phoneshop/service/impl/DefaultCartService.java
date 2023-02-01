package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Product product = productDao.get(productId);
        Optional<CartItem> optionalCartItem = getCartItemFromCart(productId, cart);
        if (!checkFullStock(cart, product, quantity)) {
            throw new OutOfStockException(product, quantity, product.getStock());
        } else {
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
        }
        recalculateCart(cart);
        recalculateTotalCost(cart);
    }

    @Override
    public void update(Cart cart, long productId, int quantity) throws OutOfStockException {
        if(quantity < 0){
            throw new IllegalArgumentException();
        } else if(quantity == 0){
            delete(cart, productId);
        } else {
            Product product = productDao.get(productId);
            Optional<CartItem> optionalCartItem = getCartItemFromCart(productId, cart);
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                if (!checkFullStock(cart, product, quantity)) {
                    throw new OutOfStockException(product, quantity, product.getStock());
                } else {
                    cartItem.setQuantity(quantity);
                }
            } else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
            recalculateCart(cart);
            recalculateTotalCost(cart);
        }
    }

    @Override
    public void delete(Cart cart, long productId) {
        cart.getCartItems().removeIf(cartItem ->
                cartItem.getProduct().getId().equals(productId)
        );
        recalculateCart(cart);
        recalculateTotalCost(cart);
    }

    @Override
    public void clear(Cart cart) {
        cart.setCartItems(new HashSet<>());
        cart.setTotalQuantity(0);
        cart.setTotalCost(BigDecimal.ZERO);
    }

    private boolean checkFullStock(Cart cart, Product product, int quantity) {
        int productQuantity = product.getStock();
        int inCartProductQuantity = 0;
        Optional<CartItem> optionalCartItem = getCartItemFromCart(product.getId(), cart);
        if (optionalCartItem.isPresent()) {
            inCartProductQuantity = optionalCartItem.get().getQuantity();
        }
        return productQuantity > inCartProductQuantity + quantity;
    }

    private Optional<CartItem> getCartItemFromCart(long productId, Cart cart) {
        return cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny();
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getCartItems()
                .stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingInt(q -> q.intValue())));
    }

    private void recalculateTotalCost(Cart cart) {
        cart.setTotalCost(cart.getCartItems()
                .stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO));
    }

}
