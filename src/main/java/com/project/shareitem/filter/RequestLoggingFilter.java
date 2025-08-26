package com.project.shareitem.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        log.info("Входящий запрос: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

        var queryString = httpRequest.getQueryString();

        if (queryString != null) {
            log.info("Параметры запроса: {}", queryString);
        }

        filterChain.doFilter(request, response);
    }
}
