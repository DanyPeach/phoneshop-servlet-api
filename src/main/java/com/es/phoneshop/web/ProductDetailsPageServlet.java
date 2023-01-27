package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.viewedProduct.ViewedProductServiceImpl;
import com.es.phoneshop.model.viewedProduct.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private ViewedProductsService viewedProductService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedProductService = ViewedProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        viewedProductService.addProductToViewedList(req.getSession(), productDao.get(parseProductId(req)));
        req.setAttribute("product", productDao.get(parseProductId(req)));
        req.setAttribute("cart", cartService.getCart(req));
        req.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quantityString = req.getParameter("quantity");
        Long id = parseProductId(req);
        Cart cart = cartService.getCart(req);
        int quantity;
        try {
            req.getLocale();
            NumberFormat format = NumberFormat.getInstance(req.getLocale());
            quantity = format.parse(quantityString).intValue();
            cartService.add(cart, id, quantity);
        } catch (ParseException e) {
            req.setAttribute("error", "Not a number");
            resp.sendRedirect(req.getContextPath() + "/products/" + id + "?error=Not a number");
            doGet(req, resp);
            return;
        } catch (OutOfStockException e) {
            req.setAttribute("error", "Out of stock, max available: " + e.getStockAvailable());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/products/" + id + "?message=Product added to your cart successfully");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo().substring(1);
        return Long.valueOf(productId);
    }
}
