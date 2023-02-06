package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.SearchOption;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String minPriceString = request.getParameter("minPrice");
        String maxPriceString = request.getParameter("maxPrice");
        String searchOption = request.getParameter("searchOption");
        String query = request.getParameter("query");

        Map<String, String> errors = new HashMap<>();

        BigDecimal minPrice = parseBigDecimal(minPriceString, errors);
        BigDecimal maxPrice = parseBigDecimal(maxPriceString, errors);

        if (errors.isEmpty()) {
            request.setAttribute("products", productDao.advancedSearch(query, searchOption, minPrice, maxPrice));
        } else {
            request.setAttribute("errors", errors);
        }

        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);

    }

    private BigDecimal parseBigDecimal(String value, Map<String, String> errors) {
        try {
            return new BigDecimal(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            if(value == null){
                return null;
            }
            if(value.equals(" ")){
                errors.put("error", "Not a number");
            }
        }
        return null;
    }

}
