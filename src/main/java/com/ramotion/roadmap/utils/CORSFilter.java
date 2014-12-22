package com.ramotion.roadmap.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin);

        String reqHead = request.getHeader("Access-Control-Request-Headers");
        if (!StringUtils.isEmpty(reqHead)) {
            response.addHeader("Access-Control-Allow-Headers", reqHead);
        } else {
            response.setHeader("Access-Control-Allow-Headers", "origin, x-requested-with, content-type, accept");
        }

        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
