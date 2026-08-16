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
                // PUBLIC APIs
                // =================================

                .requestMatchers(
                        "/api/users/register",
                        "/api/users/login",
                        "/uploads/**"
                )
                .permitAll()


                // =================================
                // PUBLIC GET APIs
                // WEBSITE
                // =================================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/categories/**",
                        "/api/salons/**",
                        "/api/services/**",
                        "/api/products/**",
                        "/api/employees/**"
                )
                .permitAll()


                // =================================
                // CATEGORY
                // ADMIN ONLY
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
                // SALON
                // ADMIN ONLY
                // =================================

                .requestMatchers(
                        "/api/salons/**"
                )
                .hasRole("ADMIN")


                // =================================
                // EMPLOYEE
                // ADMIN ONLY
                // =================================

                .requestMatchers(
                        "/api/employees/**"
                )
                .hasRole("ADMIN")


                // =================================
                // SERVICE
                // ADMIN ONLY
                // =================================

                .requestMatchers(
                        "/api/services/**"
                )
                .hasRole("ADMIN")


                // =================================
                // PRODUCT
                // ADMIN ONLY
                // =================================

                .requestMatchers(
                        "/api/products/**"
                )
                .hasRole("ADMIN")


                // =================================
                // APPOINTMENT - GET
                // USER + ADMIN + RECEPTIONIST
                // =================================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/**"
                )
                .hasAnyRole(
                        "USER",
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // APPOINTMENT - CREATE
                // CUSTOMER CAN BOOK
                // =================================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/appointments/add"
                )
                .hasAnyRole(
                        "USER",
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // APPOINTMENT - UPDATE
                // =================================

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/appointments/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // APPOINTMENT - DELETE
                // =================================

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/appointments/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
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
                // TIME SLOT MANAGEMENT
                // ADMIN + RECEPTIONIST
                // =================================

                .requestMatchers(
                        "/api/timeslots/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // PRODUCT ORDER - CREATE
                // USER + ADMIN + RECEPTIONIST
                // =================================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/product-orders/add"
                )
                .hasAnyRole(
                        "USER",
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // PRODUCT ORDER - GET
                // =================================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/product-orders/**"
                )
                .hasAnyRole(
                        "USER",
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // PRODUCT ORDER - UPDATE
                // =================================

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/product-orders/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // PRODUCT ORDER - DELETE
                // =================================

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/product-orders/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // BILLS
                // ADMIN + RECEPTIONIST
                // =================================

                .requestMatchers(
                        "/api/bills/**"
                )
                .hasAnyRole(
                        "ADMIN",
                        "RECEPTIONIST"
                )


                // =================================
                // DASHBOARD
                // ADMIN ONLY
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
                // ADMIN APIs
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
            )

            // =====================================
            // HTTP BASIC DISABLED
            // =====================================

            .httpBasic(httpBasic ->
                    httpBasic.disable()
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