package ru.yandex.practicum.account.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

    public RequestFilter() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Enumeration<String> headerNames = httpRequest.getHeaderNames(); //print this to see all headers
        headerNames.asIterator().forEachRemaining(header -> {
            log.info("HEADERS!!!");
            log.info(header);
            log.info(httpRequest.getHeader(header));
        });
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}