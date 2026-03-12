package com.javanauta.ts.taskscheduler.infrastructure.security;

import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Defines the JwtRequestFilter class, which extends OncePerRequestFilter
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Defines properties to store instances of JwtUtil and UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Constructor that initializes the properties with provided instances
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Method called once per request to process the filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // Gets the value of the "Authorization" header from the request
        final String authorizationHeader = request.getHeader("Authorization");

        // Checks whether the header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extracts the JWT token from the header
            final String token = authorizationHeader.substring(7);
            String username;

            try {
                // Extracts the username from the JWT token
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                throw new AuthenticationServiceException("Invalid JWT token");
            }

            // If the username is not null and the user is not yet authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Validate the token
                if (!jwtUtil.validateToken(token, username)) {
                    throw new AuthenticationServiceException("JWT token validation failed");
                }

                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserDetails(username, authorizationHeader);
                } catch (FeignException e) {
                    throw new AuthenticationServiceException("Failed to load user info from User service", e);
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continues the filter chain, allowing the request to proceed
        chain.doFilter(request, response);
    }
}
