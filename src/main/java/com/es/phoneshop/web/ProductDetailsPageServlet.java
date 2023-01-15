package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
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

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("product", productDao.getProduct(parseProductId(req)));
        req.setAttribute("cart", cartService.getCart());
        req.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quantityString = req.getParameter("quantity");
        Long id = parseProductId(req);

        int quantity;
        try {
          quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e){
            req.setAttribute("error", "Not a number");
            doGet(req, resp);
            return;
        }
        try {
            cartService.add(id, quantity);
        } catch (OutOfStockException e) {
            req.setAttribute("error", "Out of stock, max available: " + e.getStockAvailable());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/products/" + id + "?message=Product added to your cart successfully");
    }

    private Long parseProductId(HttpServletRequest request){
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}
