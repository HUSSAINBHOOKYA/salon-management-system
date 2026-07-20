package com.salonmanagementsystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.LoginResponse;
import com.salonmanagementsystem.entity.UserEntity;
import com.salonmanagementsystem.jwt.JwtUtil;
import com.salonmanagementsystem.repository.UserRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Data
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    
    
    public UserEntity registerUser(UserEntity user) {

        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
//        log.info("Email : " + user.getEmail());
//        log.info("User Found : " + dbUser);

        if(dbUser.isPresent()){
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
 // Login

    
//    public String loginUser(UserEntity user) {
//
//        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
//
//        if (dbUser.isEmpty()) {
//            return "Email Not Found";
//        }
//
//        if (!dbUser.get().getPassword().equals(user.getPassword())) {
//            return "Invalid Password";
//        }
//
//        return "Login Successful";
//    
//    }
    
//    public String loginUser(UserEntity user) {
//
//        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
//
//        if (dbUser.isEmpty()) {
//            return "Email Not Found";
//        }
//
//        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
//            return "Invalid Password";
//        }
//
////        return "Login Successful";
//
//        return jwtUtil.generateToken(user.getEmail());
//    }
    
    
    public LoginResponse loginUser(UserEntity user) {

        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());

        if (dbUser.isEmpty()) {
            throw new RuntimeException("Email Not Found");
        }

        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(dbUser.get().getEmail());

        return new LoginResponse(token);
    }
    
    
    
        public String deleteUser(Integer id) {

        if (!userRepository.existsById(id)) {
            return "User Not Found";
        }

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }

}