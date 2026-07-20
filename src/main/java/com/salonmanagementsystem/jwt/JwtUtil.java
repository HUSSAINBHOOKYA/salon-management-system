package com.salonmanagementsystem.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Secret key (minimum 32 characters for HS256)
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey12";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate JWT Token
    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key)
                .compact();
    }

    // Extract Username (Email)
    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Extract Expiration Date
    public Date extractExpiration(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // Check if Token is Expired
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Validate Token
    public boolean validateToken(String token, String email) {

        String username = extractUsername(token);

        return username.equals(email) && !isTokenExpired(token);
    }
}




















//package UseProjectAuthenticationandAuthorization.simple.jwt;
//
//import java.security.Key;
//import java.util.Date;
//
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtUtil {
//	
//	
//
//    private static final String SECRET =
//            "mysecretkeymysecretkeymysecretkey12";
//
//    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
//
//    public String generateToken(String email) {
//
//        return Jwts.builder()
//                .subject(email)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
//                .signWith(key)
//                .compact();
//    }
//    
//    // 1. Generate JWT
////    public String generateToken(String email) {
////
////    }
//
//    // 2. Extract Username
////    public String extractUsername(String token) {
////		return "something";
////
////    }
//    
//    public String extractUsername(String token) {
//
//        return Jwts.parser()
//                .verifyWith((javax.crypto.SecretKey) key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    // 3. Extract Expiration
//    public Date extractExpiration(String token) {
//		return null;
//
//    }
//
//    // 4. Check Expired
////    private boolean isTokenExpired(String token) {
////		return false;
//
////    }
//    
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // 5. Validate Token
////    public boolean validateToken(String token, String email) {
////		return false;
////
////    }
//    
//    public boolean validateToken(String token, String email) {
//
//        String username = extractUsername(token);
//
//        return username.equals(email) && !isTokenExpired(token);
//    }
//}