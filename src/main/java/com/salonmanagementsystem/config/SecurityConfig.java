package com.salonmanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

    @Autowired
    private JwtFilter jwtFilter;

    // Password Encoder Bean
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security Configuration
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF
                 .cors(cors->{})
                .csrf(csrf -> csrf.disable())

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/services/**",
                                "/api/employees/**",
                                "/api/appointments/**",
                                "/api/timeslots/**",
                                "/api/products/**",
                                "/api/product-orders/**",
                                "/api/bills/**",
                                "/api/dashboard/**"
                                
                        ).permitAll()

                        // Only ADMIN can access
//                        .requestMatchers("/api/admin/**")
//                        .hasRole("ADMIN")
//
//                        // USER or ADMIN can access
//                        .requestMatchers("/api/user/**")
//                        .hasAnyRole("USER", "ADMIN")
                        
                        .requestMatchers("/api/admin/**")
                        .hasAuthority("ADMIN")

                        .requestMatchers("/api/user/**")
                        .hasAnyAuthority("USER", "ADMIN")

                        // Any authenticated user
                        .anyRequest()
                        .authenticated())

                // Stateless Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Optional
                .httpBasic(Customizer.withDefaults());

        // JWT Filter
        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Authentication Manager
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}

















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