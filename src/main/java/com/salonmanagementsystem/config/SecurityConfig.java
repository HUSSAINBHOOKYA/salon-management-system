package com.salonmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.salonmanagementsystem.jwt.JwtFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    // =========================================
    // PASSWORD ENCODER
    // =========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================================
    // SECURITY FILTER CHAIN
    // =========================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // =====================================
            // CSRF
            // =====================================

            .csrf(csrf -> csrf.disable())


            // =====================================
            // CORS
            // =====================================

            .cors(cors -> {})


            // =====================================
            // SESSION
            // =====================================

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )


            // =====================================
            // AUTHORIZATION
            // =====================================

            .authorizeHttpRequests(auth -> auth


                // =================================
                // PUBLIC AUTH APIs
                // =================================

                .requestMatchers(
                    "/api/users/register",
                    "/api/users/partner/register",
                    "/api/users/login",
                    "/uploads/**"
                )
                .permitAll()


                // =================================
                // AI RECOMMENDATION
                // USER ONLY
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/ai/recommend"
                )
                .hasRole("USER")


                // =================================
                // PARTNER APPLICATION STATUS
                // =================================
                // Must come BEFORE /api/salons/**

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/salons/partner/status"
                )
                .hasRole("PARTNER")


                // =================================
                // PARTNER SALON APPLICATION
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/salons/partner"
                )
                .hasRole("PARTNER")


                // =================================
                // PARTNER SALON UPDATE
                // =================================

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/salons/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )


                // =================================
                // CATEGORY - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/categories/**"
                )
                .permitAll()


                // =================================
                // CATEGORY - ADMIN ONLY
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/categories/**"
                )
                .hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/categories/**"
                )
                .hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/categories/**"
                )
                .hasRole("ADMIN")


                // =================================
                // SALON - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/salons/**"
                )
                .permitAll()


                // =================================
                // SALON - ADMIN + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/salons/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/salons/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )


                // =================================
                // EMPLOYEE - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/employees/**"
                )
                .permitAll()


                // =================================
                // EMPLOYEE - PARTNER + ADMIN
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/employees/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/employees/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/employees/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )


                // =================================
                // SERVICE - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/services/**"
                )
                .permitAll()


                // =================================
                // SERVICE - PARTNER + ADMIN
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/services/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/services/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/services/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )


                // =================================
                // PRODUCT - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/products/**"
                )
                .permitAll()


                // =================================
                // PRODUCT - PARTNER + ADMIN
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/products/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/products/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/products/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "PARTNER"
                )


                // =================================
                // APPOINTMENT - GET
                // USER + ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/appointments/**"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // APPOINTMENT - CREATE
                // USER + ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/appointments/add"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // APPOINTMENT - UPDATE
                // ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/appointments/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // APPOINTMENT - DELETE
                // ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/appointments/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // TIME SLOT - AVAILABLE
                // PUBLIC
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/timeslots/available/**"
                )
                .permitAll()


                // =================================
                // TIME SLOT - PUBLIC GET
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/timeslots/**"
                )
                .permitAll()


                // =================================
                // TIME SLOT MANAGEMENT
                // ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/timeslots/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/timeslots/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/timeslots/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // PRODUCT ORDER - CREATE
                // USER + ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/product-orders/add"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // PRODUCT ORDER - GET
                // USER + ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/product-orders/**"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // PRODUCT ORDER - UPDATE
                // ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/product-orders/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // PRODUCT ORDER - DELETE
                // ADMIN + RECEPTIONIST + PARTNER
                // =================================

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/product-orders/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =====================================================
                // BILLS - CUSTOMER MY BILLS
                // =====================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/bills/my"
                )
                .hasRole("USER")


                // =====================================================
                // BILLS - PARTNER MY BILLS
                // =====================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/bills/partner"
                )
                .hasRole("PARTNER")


                // =====================================================
                // BILLS - CUSTOMER VIEW ONE BILL
                // =====================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/bills/{id:[0-9]+}"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =====================================================
                // BILLS - ADMIN / RECEPTIONIST / PARTNER
                // =====================================================

                .requestMatchers(
                    "/api/bills/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RECEPTIONIST",
                    "PARTNER"
                )


                // =================================
                // PARTNER DASHBOARD
                // =================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/dashboard/partner"
                )
                .hasRole("PARTNER")


                // =================================
                // ADMIN DASHBOARD
                // =================================

                .requestMatchers(
                    "/api/dashboard/**"
                )
                .hasRole("ADMIN")


                // =================================
                // USER HELLO
                // =================================

                .requestMatchers(
                    "/api/users/hello"
                )
                .hasAnyRole(
                    "USER",
                    "ADMIN"
                )


                // =================================
                // ADMIN USER APIs
                // =================================

                .requestMatchers(
                    "/api/users/admin/**"
                )
                .hasRole("ADMIN")


                // =================================
                // EVERYTHING ELSE
                // =================================

                .anyRequest()
                .authenticated()
            );


        // =========================================
        // HTTP BASIC DISABLED
        // =========================================

        http.httpBasic(
            httpBasic -> httpBasic.disable()
        );


        // =========================================
        // JWT FILTER
        // =========================================

        http.addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter.class
        );


        // =========================================
        // BUILD
        // =========================================

        return http.build();
    }


    // =========================================
    // AUTHENTICATION MANAGER
    // =========================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}










//package com.salonmanagementsystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//
//        this.jwtFilter = jwtFilter;
//    }
//
//
//    // =========================================
//    // PASSWORD ENCODER
//    // =========================================
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//
//    // =========================================
//    // SECURITY FILTER CHAIN
//    // =========================================
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//
//            // =====================================
//            // CSRF
//            // =====================================
//
//            .csrf(csrf -> csrf.disable())
//
//
//            // =====================================
//            // CORS
//            // =====================================
//
//            .cors(cors -> {})
//
//
//            // =====================================
//            // SESSION
//            // =====================================
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            )
//
//
//            // =====================================
//            // AUTHORIZATION
//            // =====================================
//
//            .authorizeHttpRequests(auth -> auth
//
//
//                // =================================
//                // PUBLIC AUTH APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/register",
//                    "/api/users/partner/register",
//                    "/api/users/login",
//                    "/uploads/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // PARTNER APPLICATION STATUS
//                // =================================
//                // Must come BEFORE /api/salons/**
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/salons/partner/status"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // PARTNER SALON APPLICATION
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/salons/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // PARTNER SALON UPDATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/salons/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // CATEGORY - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/categories/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // CATEGORY - ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SALON - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/salons/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // SALON - ADMIN + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/salons/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/salons/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // EMPLOYEE - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/employees/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // EMPLOYEE - PARTNER + ADMIN
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/employees/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/employees/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/employees/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // SERVICE - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/services/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // SERVICE - PARTNER + ADMIN
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/services/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/services/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/services/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PRODUCT - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/products/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // PRODUCT - PARTNER + ADMIN
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/products/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/products/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/products/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // APPOINTMENT - GET
//                // USER + ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // APPOINTMENT - CREATE
//                // USER + ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/appointments/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // APPOINTMENT - UPDATE
//                // ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // APPOINTMENT - DELETE
//                // ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // TIME SLOT - AVAILABLE
//                // PUBLIC
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/timeslots/available/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // TIME SLOT - PUBLIC GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/timeslots/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // TIME SLOT MANAGEMENT
//                // ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - CREATE
//                // USER + ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/product-orders/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - GET
//                // USER + ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - UPDATE
//                // ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - DELETE
//                // ADMIN + RECEPTIONIST + PARTNER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =====================================================
//                // BILLS - CUSTOMER MY BILLS
//                // =====================================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/bills/my"
//                )
//                .hasRole("USER")
//
//
//                // =====================================================
//                // BILLS - PARTNER MY BILLS
//                // =====================================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/bills/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =====================================================
//                // BILLS - CUSTOMER VIEW ONE BILL
//                //
//                // IMPORTANT:
//                // {id:[0-9]+} means ONLY numeric bill IDs.
//                //
//                // Example:
//                // /api/bills/1
//                // /api/bills/2
//                //
//                // It will NOT match:
//                // /api/bills/all
//                // /api/bills/search
//                // /api/bills/pagination
//                // /api/bills/payment/PAID
//                // =====================================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/bills/{id:[0-9]+}"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =====================================================
//                // BILLS - ADMIN / RECEPTIONIST / PARTNER
//                // =====================================================
//
//                .requestMatchers(
//                    "/api/bills/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST",
//                    "PARTNER"
//                )
//
//
//                // =================================
//                // PARTNER DASHBOARD
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/dashboard/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // ADMIN DASHBOARD
//                // =================================
//
//                .requestMatchers(
//                    "/api/dashboard/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // USER HELLO
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/hello"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN"
//                )
//
//
//                // =================================
//                // ADMIN USER APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/admin/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EVERYTHING ELSE
//                // =================================
//
//                .anyRequest()
//                .authenticated()
//            );
//
//
//        // =========================================
//        // HTTP BASIC DISABLED
//        // =========================================
//
//        http.httpBasic(
//            httpBasic -> httpBasic.disable()
//        );
//
//
//        // =========================================
//        // JWT FILTER
//        // =========================================
//
//        http.addFilterBefore(
//            jwtFilter,
//            UsernamePasswordAuthenticationFilter.class
//        );
//
//
//        // =========================================
//        // BUILD
//        // =========================================
//
//        return http.build();
//    }
//
//
//    // =========================================
//    // AUTHENTICATION MANAGER
//    // =========================================
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//    }
//}













//package com.salonmanagementsystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//
//        this.jwtFilter = jwtFilter;
//
//    }
//
//
//    // =========================================
//    // PASSWORD ENCODER
//    // =========================================
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//
//    }
//
//
//    // =========================================
//    // SECURITY FILTER CHAIN
//    // =========================================
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//
//            // =====================================
//            // CSRF
//            // =====================================
//
//            .csrf(csrf -> csrf.disable())
//
//
//            // =====================================
//            // CORS
//            // =====================================
//
//            .cors(cors -> {})
//
//
//            // =====================================
//            // SESSION
//            // =====================================
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            )
//
//
//            // =====================================
//            // AUTHORIZATION
//            // =====================================
//
//            .authorizeHttpRequests(auth -> auth
//
//
//                // =================================
//                // PUBLIC APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/register",
//                    "/api/users/partner/register",
//                    "/api/users/login",
//                    "/uploads/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // PARTNER APPLICATION STATUS
//                // PARTNER ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/salons/partner/status"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // PARTNER
//                // SUBMIT SALON APPLICATION
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/salons/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // PUBLIC GET APIs
//                // WEBSITE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/categories/**",
//                    "/api/salons/**",
//                    "/api/services/**",
//                    "/api/products/**",
//                    "/api/employees/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // CATEGORY
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SALON
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/salons/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EMPLOYEE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/employees/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SERVICE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/services/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // PRODUCT
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/products/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // APPOINTMENT - GET
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - CREATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/appointments/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - UPDATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - DELETE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // TIME SLOT - AVAILABLE
//                // PUBLIC
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/timeslots/available/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // TIME SLOT MANAGEMENT
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - CREATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/product-orders/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - GET
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - UPDATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - DELETE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // BILLS
//                // =================================
//
//                .requestMatchers(
//                    "/api/bills/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PARTNER REPORTS
//                // PARTNER ONLY
//                //
//                // IMPORTANT:
//                // MUST COME BEFORE
//                // /api/dashboard/**
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/dashboard/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // DASHBOARD
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/dashboard/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // USER HELLO
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/hello"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN"
//                )
//
//
//                // =================================
//                // ADMIN APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/admin/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EVERYTHING ELSE
//                // =================================
//
//                .anyRequest()
//                .authenticated()
//
//            );
//
//
//        // =========================================
//        // HTTP BASIC DISABLED
//        // =========================================
//
//        http.httpBasic(
//            httpBasic -> httpBasic.disable()
//        );
//
//
//        // =========================================
//        // JWT FILTER
//        // =========================================
//
//        http.addFilterBefore(
//            jwtFilter,
//            UsernamePasswordAuthenticationFilter.class
//        );
//
//
//        // =========================================
//        // BUILD
//        // =========================================
//
//        return http.build();
//    }
//
//
//    // =========================================
//    // AUTHENTICATION MANAGER
//    // =========================================
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//
//    }
//
//}
























//package com.salonmanagementsystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//
//    public SecurityConfig(
//            JwtFilter jwtFilter) {
//
//        this.jwtFilter = jwtFilter;
//    }
//
//
//    // =========================================
//    // PASSWORD ENCODER
//    // =========================================
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//
//    // =========================================
//    // SECURITY FILTER CHAIN
//    // =========================================
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//
//            // =====================================
//            // CSRF
//            // =====================================
//
//            .csrf(csrf -> csrf.disable())
//
//
//            // =====================================
//            // CORS
//            // =====================================
//
//            .cors(cors -> {})
//
//
//            // =====================================
//            // SESSION
//            // =====================================
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            )
//
//
//            // =====================================
//            // AUTHORIZATION
//            // =====================================
//
//            .authorizeHttpRequests(auth -> auth
//
//
//                // =================================
//                // PUBLIC APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/register",
//                    "/api/users/partner/register",
//                    "/api/users/login",
//                    "/uploads/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // PARTNER APPLICATION STATUS
//                // PARTNER ONLY
//                //
//                // IMPORTANT:
//                // This must be BEFORE
//                // /api/salons/**
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/salons/partner/status"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // ADMIN - PENDING PARTNER APPLICATIONS
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/salons/pending"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // ADMIN - APPROVE PARTNER SALON
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/salons/*/approve"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // ADMIN - REJECT PARTNER SALON
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/salons/*/reject"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // PUBLIC GET APIs
//                // WEBSITE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/categories/**",
//                    "/api/salons/**",
//                    "/api/services/**",
//                    "/api/products/**",
//                    "/api/employees/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // CATEGORY
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // PARTNER
//                // SUBMIT SALON APPLICATION
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/salons/partner"
//                )
//                .hasRole("PARTNER")
//
//
//                // =================================
//                // SALON
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/salons"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/salons/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/salons/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EMPLOYEE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/employees/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SERVICE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/services/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // PRODUCT
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/products/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // APPOINTMENT - GET
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - CREATE
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/appointments/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - UPDATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - DELETE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // TIME SLOT - AVAILABLE
//                // PUBLIC
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/timeslots/available/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // TIME SLOT MANAGEMENT
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - CREATE
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/product-orders/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - GET
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - UPDATE
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - DELETE
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // BILLS
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    "/api/bills/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // DASHBOARD
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/dashboard/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // USER HELLO
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/hello"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN"
//                )
//
//
//                // =================================
//                // ADMIN APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/admin/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EVERYTHING ELSE
//                // =================================
//
//                .anyRequest()
//                .authenticated()
//            );
//
//
//        // =========================================
//        // HTTP BASIC DISABLED
//        // =========================================
//
//        http.httpBasic(
//            httpBasic -> httpBasic.disable()
//        );
//
//
//        // =========================================
//        // JWT FILTER
//        // =========================================
//
//        http.addFilterBefore(
//            jwtFilter,
//            UsernamePasswordAuthenticationFilter.class
//        );
//
//
//        // =========================================
//        // BUILD
//        // =========================================
//
//        return http.build();
//    }
//
//
//    // =========================================
//    // AUTHENTICATION MANAGER
//    // =========================================
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//    }
//
//}













//package com.salonmanagementsystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//
//        this.jwtFilter = jwtFilter;
//
//    }
//
//
//    // =========================================
//    // PASSWORD ENCODER
//    // =========================================
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//
//    }
//
//
//    // =========================================
//    // SECURITY FILTER CHAIN
//    // =========================================
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//
//            // =====================================
//            // CSRF
//            // =====================================
//
//            .csrf(csrf -> csrf.disable())
//
//
//            // =====================================
//            // CORS
//            // =====================================
//
//            .cors(cors -> {})
//
//
//            // =====================================
//            // SESSION
//            // =====================================
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            )
//
//
//            // =====================================
//            // AUTHORIZATION
//            // =====================================
//
//            .authorizeHttpRequests(auth -> auth
//
//
//                // =================================
//                // PUBLIC APIs
//                // =================================
//
//            		.requestMatchers(
//            			    "/api/users/register",
//            			    "/api/users/partner/register",
//            			    "/api/users/login",
//            			    "/uploads/**"
//            			)
//            			.permitAll()
//
//
//                // =================================
//                // PUBLIC GET APIs
//                // WEBSITE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/categories/**",
//                    "/api/salons/**",
//                    "/api/services/**",
//                    "/api/products/**",
//                    "/api/employees/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // CATEGORY
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/categories/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SALON
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/salons/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EMPLOYEE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/employees/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // SERVICE
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/services/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // PRODUCT
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/products/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // APPOINTMENT - GET
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - CREATE
//                // LOGGED-IN CUSTOMER
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/appointments/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - UPDATE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // APPOINTMENT - DELETE
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/appointments/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // TIME SLOT - AVAILABLE
//                // PUBLIC
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/timeslots/available/**"
//                )
//                .permitAll()
//
//
//                // =================================
//                // TIME SLOT MANAGEMENT
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    "/api/timeslots/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - CREATE
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/product-orders/add"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - GET
//                // USER + ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - UPDATE
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // PRODUCT ORDER - DELETE
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/product-orders/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // BILLS
//                // ADMIN + RECEPTIONIST
//                // =================================
//
//                .requestMatchers(
//                    "/api/bills/**"
//                )
//                .hasAnyRole(
//                    "ADMIN",
//                    "RECEPTIONIST"
//                )
//
//
//                // =================================
//                // DASHBOARD
//                // ADMIN ONLY
//                // =================================
//
//                .requestMatchers(
//                    "/api/dashboard/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // USER HELLO
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/hello"
//                )
//                .hasAnyRole(
//                    "USER",
//                    "ADMIN"
//                )
//
//
//                // =================================
//                // ADMIN APIs
//                // =================================
//
//                .requestMatchers(
//                    "/api/users/admin/**"
//                )
//                .hasRole("ADMIN")
//
//
//                // =================================
//                // EVERYTHING ELSE
//                // =================================
//
//                .anyRequest()
//                .authenticated()
//            );
//
//
//        // =========================================
//        // HTTP BASIC DISABLED
//        // =========================================
//
//        http.httpBasic(
//            httpBasic -> httpBasic.disable()
//        );
//
//
//        // =========================================
//        // JWT FILTER
//        // =========================================
//
//        http.addFilterBefore(
//            jwtFilter,
//            UsernamePasswordAuthenticationFilter.class
//        );
//
//
//        // =========================================
//        // BUILD
//        // =========================================
//
//        return http.build();
//
//    }
//
//
//    // =========================================
//    // AUTHENTICATION MANAGER
//    // =========================================
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//
//    }
//
//}











//package com.salonmanagementsystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//            throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//
//            .cors(cors -> {})
//
//            .sessionManagement(session ->
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//            .authorizeHttpRequests(auth -> auth
//
//                /*
//                 * Public APIs
//                 */
//                .requestMatchers(
//                        "/api/users/register",
//                        "/api/users/login",
//                        "/uploads/**"
//                ).permitAll()
//
//                /*
//                 * Public GET APIs (Website)
//                 */
//                .requestMatchers(
//                        HttpMethod.GET,
//                        "/api/categories/**",
//                        "/api/salons/**",
//                        "/api/services/**",
//                        "/api/products/**",
//                        "/api/employees/**"
//                ).permitAll()
//
//                /*
//                 * Category
//                 */
//                .requestMatchers(HttpMethod.POST, "/api/categories/**")
//                .hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.PUT, "/api/categories/**")
//                .hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.DELETE, "/api/categories/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * Salon
//                 */
//                .requestMatchers("/api/salons/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * Employee
//                 */
//                .requestMatchers("/api/employees/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * Service
//                 */
//                .requestMatchers("/api/services/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * Product
//                 */
//                .requestMatchers("/api/products/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * Appointment
//                 */
//                .requestMatchers("/api/appointments/**")
//                .hasAnyRole("ADMIN", "RECEPTIONIST")
//
//                /*
//                 * Time Slot
//                 */
//                /*
//                 * Time Slot - Available Slots
//                 * Customers can view available slots
//                 */
//                .requestMatchers(
//                        HttpMethod.GET,
//                        "/api/timeslots/available/**"
//                )
//                .permitAll()
//
//                /*
//                 * Time Slot - Management
//                 * Only Admin and Receptionist
//                 */
//                .requestMatchers("/api/timeslots/**")
//                .hasAnyRole("ADMIN", "RECEPTIONIST")
//
//                /*
//                 * Bills
//                 */
//                .requestMatchers("/api/bills/**")
//                .hasAnyRole("ADMIN", "RECEPTIONIST")
//
//                /*
//                 * Dashboard
//                 */
//                .requestMatchers("/api/dashboard/**")
//                .hasRole("ADMIN")
//
//                /*
//                 * User
//                 */
//                .requestMatchers("/api/users/hello")
//                .hasAnyRole("USER", "ADMIN")
//
//                /*
//                 * Admin
//                 */
//                .requestMatchers("/api/users/admin/**")
//                .hasRole("ADMIN")
//
//                .anyRequest()
//                .authenticated()
//
//            )
//
//            .httpBasic(httpBasic -> httpBasic.disable());
//
//        http.addFilterBefore(
//                jwtFilter,
//                UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//    }
//}













//package com.salonmanagementsystem.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    // Password Encoder
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Security Configuration
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                // Enable CORS
//                .cors(cors -> {})
//
//                // Disable CSRF
//                .csrf(csrf -> csrf.disable())
//
//                // Stateless Session
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // Authorization
//                .authorizeHttpRequests(auth -> auth
//
//                        // Public URLs
//                        .requestMatchers(
//
//                                "/uploads/**",
//
//                                "/api/users/register",
//                                "/api/users/login",
//
//                                "/api/services/**",
//                                "/api/employees/**",
//                                "/api/appointments/**",
//                                "/api/timeslots/**",
//                                "/api/products/**",
//                                "/api/product-orders/**",
//                                "/api/bills/**",
//                                "/api/dashboard/**",
//                                "/api/categories/**"
//                                
//                   
//
//                        ).permitAll()
//
//                        // Admin APIs
//                        .requestMatchers("/api/admin/**")
//                        .hasAuthority("ROLE_ADMIN")
//
//                        // User APIs
//                        .requestMatchers("/api/user/**")
//                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//
//                        // Remaining APIs
//                        .anyRequest()
//                        .authenticated()
//
//                )
//
//                // Disable Browser Login Popup
//                .httpBasic(httpBasic -> httpBasic.disable());
//
//        // JWT Filter
//        http.addFilterBefore(
//                jwtFilter,
//                UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // Authentication Manager
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration configuration)
//            throws Exception {
//
//        return configuration.getAuthenticationManager();
//    }
//}























//package com.salonmanagementsystem.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.salonmanagementsystem.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    // Password Encoder Bean
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Security Configuration
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                // Disable CSRF
//                 .cors(cors->{})
//                .csrf(csrf -> csrf.disable())
//
//                // Authorization Rules
//                .authorizeHttpRequests(auth -> auth
//
//                        // Public APIs
//                        .requestMatchers(
//                        		   "/uploads/**",
//                        	        "/api/users/register",
//                        	        "/api/users/login",
//                        	        "/api/services/**",
//                        	        "/api/employees/**",
//                        	        "/api/appointments/**",
//                        	        "/api/timeslots/**",
//                        	        "/api/products/**",
//                        	        "/api/product-orders/**",
//                        	        "/api/bills/**",
//                        	        "/api/dashboard/**",
//                        	        "/api/categories/**"
//                                
//                        ).permitAll()
//
//                        // Only ADMIN can access
////                        .requestMatchers("/api/admin/**")
////                        .hasRole("ADMIN")
////
////                        // USER or ADMIN can access
////                        .requestMatchers("/api/user/**")
////                        .hasAnyRole("USER", "ADMIN")
//                        
//                        .requestMatchers("/api/admin/**")
//                        .hasAuthority("ADMIN")
//
//                        .requestMatchers("/api/user/**")
//                        .hasAnyAuthority("USER", "ADMIN")
//
//                        // Any authenticated user
//                        .anyRequest()
//                        .authenticated())
//
//                // Stateless Session
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // Optional
//                .httpBasic(Customizer.withDefaults());
//
//        // JWT Filter
//        http.addFilterBefore(
//                jwtFilter,
//                UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // Authentication Manager
//    @Bean
//    AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//
//        return config.getAuthenticationManager();
//    }
//}

















//package UseProjectAuthenticationandAuthorization.simple.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import UseProjectAuthenticationandAuthorization.simple.jwt.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    @Bean
//  PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/api/users/register", "/api/users/login").permitAll()
//                    .anyRequest().authenticated())
//            .sessionManagement(session ->
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .httpBasic(Customizer.withDefaults());
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//   @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration config)
//            throws Exception {
//
//        return config.getAuthenticationManager();
//    } 
//}



















//package UseProjectAuthenticationandAuthorization.simple.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
//                .anyRequest().authenticated()
//            )
//
//            .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//}