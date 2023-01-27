package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.viewedProduct.ViewedProductServiceImpl;
import com.es.phoneshop.model.viewedProduct.ViewedProductsService;
import com.es.phoneshop.web.utils.ExceptionHandler;
import com.es.phoneshop.web.utils.ItemPropertyParsing;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AddItemToCartServlet extends HttpServlet {
    private static final String CATALOG_PAGE = "/WEB-INF/pages/productList.jsp";
    private ProductDao productDao;
    private CartService cartService;
    private ViewedProductsService viewedProductsService;
    private ExceptionHandler exceptionHandler;
    private ItemPropertyParsing itemPropertyParsing;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        String sortField = req.getParameter("sort");
        String sortOrder = req.getParameter("order");

        req.setAttribute("viewedProducts", viewedProductsService.getViewedProducts(req.getSession()));
        req.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        req.getRequestDispatcher(CATALOG_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String quantitie = req.getParameter("quantity");
        Map<Long, String> errors = new HashMap<>();
        Long parsedProductId = Long.valueOf(productId);
        int quantity;
            try{
                quantity = itemPropertyParsing.getQuantity(quantitie, req);
                cartService.add(cartService.getCart(req), parsedProductId, quantity);
            } catch (ParseException | OutOfStockException e){
                exceptionHandler.handleError(errors, parsedProductId, e);
            }
        if(errors.isEmpty()){
            resp.sendRedirect(req.getContextPath() + "/cart?message=Successfull added to your cart");
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
        viewedProductsService = ViewedProductServiceImpl.getInstance();
        exceptionHandler = new ExceptionHandler();
        itemPropertyParsing = new ItemPropertyParsing();
    }
}
