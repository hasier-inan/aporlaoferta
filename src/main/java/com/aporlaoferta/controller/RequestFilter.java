package com.aporlaoferta.controller;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 22/12/2014
 * Time: 16:40
 */
@Component
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        chain.doFilter(req, res);
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (!request.isSecure()) {
                // generate full URL to https
                StringBuilder newUrl = new StringBuilder("https://");
                newUrl.append(request.getServerName());
                if (httpRequest.getRequestURI() != null) {
                    newUrl.append(httpRequest.getRequestURI());
                }
                if (httpRequest.getQueryString() != null) {
                    newUrl.append("?").append(httpRequest.getQueryString());
                }

                httpResponse.sendRedirect(newUrl.toString());
            } else {
                // already a secure connection, no redirect to https required.
                if (chain != null) {
                    chain.doFilter(request, response);
                }
            }
        }
    }

    @Override
    public void destroy() {

    }
}
