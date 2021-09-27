package com.miw.server.security;

import com.miw.server.domain.User;
import com.miw.server.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authHeader)) {
            System.out.println("Auth Header is null");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (JwtUtils.isRegisteredUser(authHeader)) {
                User user = JwtUtils.getUser(authHeader);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        filterChain.doFilter(request, response);
        return;
    }
}
