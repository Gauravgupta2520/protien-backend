package com.example.protien;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // Log all headers for debugging (especially for Postman issues)
        String header = request.getHeader("Authorization");
        String userAgent = request.getHeader("User-Agent");
        String origin = request.getHeader("Origin");
        
        logger.info("=== JWT Filter Debug ===");
        logger.info("Request URI: {} | Method: {}", request.getRequestURI(), request.getMethod());
        logger.info("User-Agent: {}", userAgent != null ? userAgent : "Not provided");
        logger.info("Origin: {}", origin != null ? origin : "Not provided");
        logger.info("Authorization header present: {}", header != null);
        if (header != null) {
            logger.info("Authorization header length: {}", header.length());
            logger.info("Authorization header starts with 'Bearer': {}", header.trim().startsWith("Bearer ") || header.trim().startsWith("bearer "));
            logger.info("Authorization header preview: {}", header.length() > 50 ? header.substring(0, 50) + "..." : header);
        }

        if (header != null && header.trim().length() > 0) {
            // Handle case-insensitive "Bearer" and trim whitespace
            String trimmedHeader = header.trim();
            if (trimmedHeader.startsWith("Bearer ") || trimmedHeader.startsWith("bearer ")) {
                
                String token = trimmedHeader.substring(7).trim();

                if (token.isEmpty()) {
                    logger.warn("Empty token in Authorization header for request={}", request.getRequestURI());
                } else {
                    try {
                        if (jwtUtil.validateToken(token)) {

                            String email = jwtUtil.extractEmail(token);

                            UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                                );

                            auth.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext().setAuthentication(auth);
                            logger.info("✓ JWT authenticated user={} for request={}", email, request.getRequestURI());
                            
                            // Verify authentication was set
                            var context = SecurityContextHolder.getContext();
                            var authCheck = context.getAuthentication();
                            logger.info("SecurityContext authentication set: {} | Principal: {}", 
                                authCheck != null, 
                                authCheck != null ? authCheck.getPrincipal() : "null");
                        } else {
                            logger.warn("✗ JWT token validation failed - token expired or invalid for request={}", request.getRequestURI());
                            SecurityContextHolder.clearContext();
                        }
                    } catch (Exception e) {
                        logger.error("✗ JWT validation exception for request={}: {} | Exception: {}", 
                            request.getRequestURI(), e.getMessage(), e.getClass().getSimpleName(), e);
                        SecurityContextHolder.clearContext();
                    }
                }
            } else {
                logger.warn("Authorization header does not start with 'Bearer ' for request={}. Header: {}", 
                    request.getRequestURI(), trimmedHeader.length() > 30 ? trimmedHeader.substring(0, 30) + "..." : trimmedHeader);
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.warn("No Authorization header found for request={}", request.getRequestURI());
            SecurityContextHolder.clearContext();
        }

        // Log final authentication state before proceeding
        var finalAuth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Final authentication state: {} | Will proceed to next filter", 
            finalAuth != null ? "AUTHENTICATED" : "NOT AUTHENTICATED");
        logger.info("=== End JWT Filter Debug ===");

        chain.doFilter(request, response);
    }
}
