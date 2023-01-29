package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_OVERVIEW_PAGE = "/WEB-INF/pages/orderOverview.jsp";
    private OrderService orderService;
    private OrderDao orderDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String secureId = req.getPathInfo().substring(1);
        req.setAttribute("order", orderDao.getOrderBySecureId(secureId));
        req.getRequestDispatcher(ORDER_OVERVIEW_PAGE).forward(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }
}
