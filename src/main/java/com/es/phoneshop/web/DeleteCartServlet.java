package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DeleteCartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String productIds = req.getPathInfo().substring(1);
        Cart cart = cartService.getCart(req);
        cartService.delete(cart, Long.parseLong(productIds));
        resp.sendRedirect(req.getContextPath() + "/cart?message=Cart item removed successfully");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }
}

