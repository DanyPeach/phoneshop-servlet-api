package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DOSService;
import com.es.phoneshop.security.impl.DOSServiceImpl;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DOSFilter implements Filter {
    private DOSService dosService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosService = DOSServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(dosService.isAllowed(servletRequest.getRemoteAddr())){
            filterChain.doFilter(servletRequest, servletResponse);
        } else{
            ((HttpServletResponse)servletResponse).setStatus(429);
        }
    }

    @Override
    public void destroy() {
        dosService.cancel();
    }
}
