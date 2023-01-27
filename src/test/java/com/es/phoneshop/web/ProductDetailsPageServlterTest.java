package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServlterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpSession session;

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Product product1 = new Product(1L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), null, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        productDao.save(product1);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);

        Product product = productDao.get(1L);
        verify(request).setAttribute("product", product);
    }

    @Test
    public void testDoPostWithEnoughStock() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn(1 +"");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        servlet.doPost(request,response);

        verify(response).sendRedirect(request.getContextPath() + "/products/" + 1 + "?message=Product added to your cart successfully");
    }

    @Test
    public void testDoPostWithNotANumberQuantity() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn("first");
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        servlet.doPost(request,response);

        verify(response).sendRedirect(request.getContextPath() + "/products/" + "1" + "?error=Not a number");
    }
}
