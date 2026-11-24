package com.salonmanagementsystem.service;

import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.LoginResponse;
import com.salonmanagementsystem.dto.PartnerRegisterRequest;
import com.salonmanagementsystem.dto.UserResponse;
import com.salonmanagementsystem.dto.UserUpdateRequest;
import com.salonmanagementsystem.entity.UserEntity;
import com.salonmanagementsystem.enums.Role;
import com.salonmanagementsystem.exception.DuplicateResourceException;
import com.salonmanagementsystem.jwt.JwtUtil;
import com.salonmanagementsystem.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    // =========================================================
    // CONSTRUCTOR 
    // ================================================
    
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // =========================================================
    // CUSTOMER REGISTER
    // =========================================================

    public UserEntity registerUser(UserEntity user) {

        Optional<UserEntity> dbUser =
                userRepository.findByEmail(user.getEmail());

        if (dbUser.isPresent()) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }


    // =========================================================
    // PARTNER REGISTER
    // =========================================================

    public UserEntity registerPartner(
			PartnerRegisterRequest request) {

        // =========================================
        // CHECK EMAIL
        // =========================================

        Optional<UserEntity> existingEmail =
                userRepository.findByEmail(request.getEmail());

        if (existingEmail.isPresent()) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }


        // =========================================
        // CHECK PHONE
        // =========================================

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().trim().isEmpty()) {

            Optional<UserEntity> existingPhone =
                    userRepository.findByPhoneNumber(
                            request.getPhoneNumber()
                    );

            if (existingPhone.isPresent()) {

                throw new DuplicateResourceException(
                        "Phone number already exists"
                );
            }
        }


        // =========================================
        // CREATE PARTNER
        // =========================================

        UserEntity partner = new UserEntity();

        partner.setFullName(
                request.getFullName()
        );

        partner.setEmail(
                request.getEmail()
        );

        partner.setPhoneNumber(
                request.getPhoneNumber()
        );

        partner.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        // =========================================
        // IMPORTANT
        // ROLE IS SET BY BACKEND
        // =========================================

        partner.setRole(Role.PARTNER);


        // =========================================
        // PARTNER DOES NOT NEED CUSTOMER DETAILS
        // =========================================

        partner.setGender(null);

        partner.setAge(null);


        return userRepository.save(partner);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse loginUser(UserEntity user) {

        Optional<UserEntity> dbUser =
                userRepository.findByEmail(user.getEmail());

        if (dbUser.isEmpty()) {

            throw new RuntimeException(
                    "Email Not Found"
            );
        }

        if (!passwordEncoder.matches(
                user.getPassword(),
                dbUser.get().getPassword())) {

            throw new RuntimeException(
                    "Invalid Password"
            );
        }

        String token =
                jwtUtil.generateToken(
                        dbUser.get().getEmail()
                );


        // =========================================
        // RETURN TOKEN + ACTUAL USER ROLE
        // =========================================

        return new LoginResponse(
                token,
                dbUser.get().getRole().name()
        );
    }


    // =========================================================
    // GET PROFILE
    // =========================================================

    public UserResponse getProfile(String email) {

        UserEntity user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                ));

        return mapToUserResponse(user);
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    public UserResponse updateProfile(
            String email,
            UserUpdateRequest request) {

        UserEntity user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                ));

        user.setFullName(
                request.getFullName()
        );

        user.setPhoneNumber(
                request.getPhoneNumber()
        );

        user.setGender(
                request.getGender()
        );

        user.setAge(
                request.getAge()
        );

        UserEntity updatedUser =
                userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }


    // =========================================================
    // MAP RESPONSE
    // =========================================================

    private UserResponse mapToUserResponse(
            UserEntity user) {

        return new UserResponse(

                user.getId(),

                user.getFullName(),

                user.getEmail(),

                user.getPhoneNumber(),

                user.getGender(),

                user.getAge(),

                user.getRole()
        );
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public String deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            return "User Not Found";
        }

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }
 // =========================================================
 // GET ALL CUSTOMERS FOR ADMIN
 // =========================================================

 public java.util.List<UserResponse> getAllCustomersForAdmin() {

     return userRepository.findAll()
             .stream()
             .filter(user -> user.getRole() == Role.USER)
             .map(this::mapToUserResponse)
             .toList();
 }
}













//package com.salonmanagementsystem.service;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.salonmanagementsystem.entity.LoginResponse;
//import com.salonmanagementsystem.entity.UserEntity;
//import com.salonmanagementsystem.jwt.JwtUtil;
//import com.salonmanagementsystem.repository.UserRepository;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//@Data
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private JwtUtil jwtUtil;
//    
//    
//    
//    public UserEntity registerUser(UserEntity user) {
//
//        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
////        log.info("Email : " + user.getEmail());
////        log.info("User Found : " + dbUser);
//
//        if(dbUser.isPresent()){
//            throw new RuntimeException("Email already exists");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        return userRepository.save(user);
//    }
// // Login
//
//    
////    public String loginUser(UserEntity user) {
////
////        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
////
////        if (dbUser.isEmpty()) {
////            return "Email Not Found";
////        }
////
////        if (!dbUser.get().getPassword().equals(user.getPassword())) {
////            return "Invalid Password";
////        }
////
////        return "Login Successful";
////    
////    }
//    
////    public String loginUser(UserEntity user) {
////
////        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
////
////        if (dbUser.isEmpty()) {
////            return "Email Not Found";
////        }
////
////        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
////            return "Invalid Password";
////        }
////
//////        return "Login Successful";
////
////        return jwtUtil.generateToken(user.getEmail());
////    }
//    
//    
//    public LoginResponse loginUser(UserEntity user) {
//
//        Optional<UserEntity> dbUser = userRepository.findByEmail(user.getEmail());
//
//        if (dbUser.isEmpty()) {
//            throw new RuntimeException("Email Not Found");
//        }
//
//        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
//            throw new RuntimeException("Invalid Password");
//        }
//
//        String token = jwtUtil.generateToken(dbUser.get().getEmail());
//
//        return new LoginResponse(token);
//    }
//    
//    
//    
//        public String deleteUser(Integer id) {
//
//        if (!userRepository.existsById(id)) {
//            return "User Not Found";
//        }
//
//        userRepository.deleteById(id);
//
//        return "User Deleted Successfully";
//    }
//
//}