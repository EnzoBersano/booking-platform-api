package com.enzobersano.booking_platform_api.auth.infrastructure.security;

import com.enzobersano.booking_platform_api.auth.application.port.TokenPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenPort          tokenPort;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(TokenPort tokenPort,
                                   UserDetailsService userDetailsService) {
        this.tokenPort          = tokenPort;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest  request,
                                    HttpServletResponse response,
                                    FilterChain         chain)
            throws ServletException, IOException {

        extractBearerToken(request).ifPresent(token -> {
            if (tokenPort.validateToken(token)) {
                var email       = tokenPort.extractEmail(token);
                var userDetails = userDetailsService.loadUserByUsername(email);
                var auth        = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        });

        chain.doFilter(request, response);
    }

    private java.util.Optional<String> extractBearerToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return java.util.Optional.of(header.substring(BEARER_PREFIX.length()));
        }
        return java.util.Optional.empty();
    }
}