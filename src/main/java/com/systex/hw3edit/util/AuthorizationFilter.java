package com.systex.hw3edit.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("loggedIn");

        if(request.getRequestURI().contains("/h2-console")){
            filterChain.doFilter(request, response);
            return;
        }

        if(user == null && !request.getRequestURI().endsWith("/login")&&!request.getRequestURI().endsWith("/register")){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        filterChain.doFilter(request,response);
    }

}
