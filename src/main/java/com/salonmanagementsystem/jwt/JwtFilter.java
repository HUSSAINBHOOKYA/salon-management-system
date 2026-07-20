//package UseProjectAuthenticationandAuthorization.simple.jwt;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import UseProjectAuthenticationandAuthorization.simple.service.CustomUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;

//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            		throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authHeader.substring(7);
//        String username = jwtUtil.extractUsername(token);
//        
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//
//    }
    
    
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        String username = jwtUtil.extractUsername(token);
//
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//
//        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities());
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
    
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//        System.out.println("===== JWT FILTER CALLED =====");
//
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("Authorization Header: " + authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            System.out.println("Bearer Token Missing");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        System.out.println("Token: " + token);
//
//        String username = jwtUtil.extractUsername(token);
//        System.out.println("Username: " + username);
//
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//        System.out.println("User Loaded: " + userDetails.getUsername());
//
//        boolean valid = jwtUtil.validateToken(token, userDetails.getUsername());
//        System.out.println("Token Valid: " + valid);
//
//        if (valid) {
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities());
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("Authentication Set Successfully");
//        }
//
//        filterChain.doFilter(request, response);
//    }

//}


//package UseProjectAuthenticationandAuthorization.simple.jwt;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import UseProjectAuthenticationandAuthorization.simple.service.CustomUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//        System.out.println("========== JWT FILTER START ==========");
//
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("Authorization Header: " + authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            System.out.println("Bearer Token Not Found");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//
//            String token = authHeader.substring(7);
//            System.out.println("Token: " + token);
//
//            String username = jwtUtil.extractUsername(token);
//            System.out.println("Username: " + username);
//
//            UserDetails userDetails =
//                    customUserDetailsService.loadUserByUsername(username);
//
//            System.out.println("Database Username: "
//                    + userDetails.getUsername());
//
//            boolean valid =
//                    jwtUtil.validateToken(token, userDetails.getUsername());
//
//            System.out.println("Token Valid: " + valid);
//
//            if (valid
//                    && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities());
//
//                SecurityContextHolder.getContext()
//                        .setAuthentication(authentication);
//
//                System.out.println("Authentication Set Successfully");
//            }
//
//        } catch (Exception e) {
//
//            System.out.println("JWT ERROR: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        filterChain.doFilter(request, response);
//
//        System.out.println("========== JWT FILTER END ==========");
//    }
//}





package com.salonmanagementsystem.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.salonmanagementsystem.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);

            String username = jwtUtil.extractUsername(token);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails.getUsername())
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Invalid token or expired token
        }

        filterChain.doFilter(request, response);
    }
}