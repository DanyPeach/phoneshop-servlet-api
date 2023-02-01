package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.impl.DOSServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class DOSFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private DOSServiceImpl DOSServiceImpl;
    @InjectMocks
    private DOSFilter filter = new DOSFilter();

    private final static String IP = "1.1.1.1";

    @Before
    public void setup() {
        when(request.getRemoteAddr()).thenReturn(IP);
    }

    @Test
    public void testFilter() throws ServletException, IOException {
        when(DOSServiceImpl.isAllowed(IP)).thenReturn(true);
        filter.doFilter(request,response,filterChain);
        verify(filterChain, times(1)).doFilter(request,response);
    }

    @Test
    public void testFilterError() throws ServletException, IOException {
        when(DOSServiceImpl.isAllowed(IP)).thenReturn(false);
        filter.doFilter(request, response, filterChain);
        verify(filterChain, never()).doFilter(request, response);
    }
}
