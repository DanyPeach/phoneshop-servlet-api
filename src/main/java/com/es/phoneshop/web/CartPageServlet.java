package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_PAGE = "/WEB-INF/pages/cart.jsp";
    private ProductDao productDao;
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        req.setAttribute("cart", cart);
        req.getRequestDispatcher(CART_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] productIds = req.getParameterValues("productId");
        String[] quantities = req.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity;
            try{
                quantity = getQuantity(quantities[i], req);
                cartService.update(cartService.getCart(req), productId, quantity);
            } catch (ParseException | OutOfStockException e){
                handleError(errors, productId, e);
            }
        }

        if(errors.isEmpty()){
            resp.sendRedirect(req.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    private int getQuantity(String quantity, HttpServletRequest req) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(req.getLocale());
        return numberFormat.parse(quantity).intValue();
    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e){
        if(e.getClass().equals(ParseException.class)){
            errors.put(productId, "Not a number");
        }else {
            if(((OutOfStockException) e).getStockAvailable() <= 0){
                errors.put(productId, "Can't be negative or zero");
            } else{
                errors.put(productId, "Out of stock" + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }
}
