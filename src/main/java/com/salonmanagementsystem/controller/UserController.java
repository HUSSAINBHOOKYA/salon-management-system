package com.salonmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.LoginResponse;
import com.salonmanagementsystem.dto.PartnerRegisterRequest;
import com.salonmanagementsystem.dto.RegisterResponse;
import com.salonmanagementsystem.dto.UserResponse;
import com.salonmanagementsystem.dto.UserUpdateRequest;
import com.salonmanagementsystem.entity.UserEntity;
import com.salonmanagementsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5179")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }


    // =========================================================
    // CUSTOMER REGISTER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody UserEntity user) {

        log.info("Customer Register API Called");

        UserEntity savedUser =
                userService.registerUser(user);

        UserResponse userResponse =
                new UserResponse(

                        savedUser.getId(),

                        savedUser.getFullName(),

                        savedUser.getEmail(),

                        savedUser.getPhoneNumber(),

                        savedUser.getGender(),

                        savedUser.getAge(),

                        savedUser.getRole()
                );

        RegisterResponse response =
                new RegisterResponse();

        response.setMessage(
                "User Registered Successfully"
        );

        response.setUser(userResponse);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // PARTNER REGISTER
    // =========================================================

    @PostMapping("/partner/register")
    public ResponseEntity<RegisterResponse> registerPartner(
            @RequestBody PartnerRegisterRequest request) {

        log.info("Partner Register API Called");

        UserEntity savedPartner =
                userService.registerPartner(request);

        UserResponse userResponse =
                new UserResponse(

                        savedPartner.getId(),

                        savedPartner.getFullName(),

                        savedPartner.getEmail(),

                        savedPartner.getPhoneNumber(),

                        savedPartner.getGender(),

                        savedPartner.getAge(),

                        savedPartner.getRole()
                );

        RegisterResponse response =
                new RegisterResponse();

        response.setMessage(
                "Partner Registered Successfully"
        );

        response.setUser(userResponse);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserEntity user) {

        log.info("Login API Called");

        return ResponseEntity.ok(
                userService.loginUser(user)
        );
    }


    // =========================================================
    // GET PROFILE
    // =========================================================

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.getProfile(email)
        );
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @RequestBody UserUpdateRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.updateProfile(
                        email,
                        request
                )
        );
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.deleteUser(id)
        );
    }


    // =========================================================
    // USER TEST
    // =========================================================

    @GetMapping("/hello")
    public ResponseEntity<String> userHello() {

        return ResponseEntity.ok(
                "Welcome User"
        );
    }


    // =========================================================
    // ADMIN TEST
    // =========================================================

    @GetMapping("/admin/hello")
    public ResponseEntity<String> adminHello() {

        return ResponseEntity.ok(
                "Welcome Admin"
        );
    }
    
 // =========================================================
 // ADMIN - GET ALL CUSTOMERS
 // =========================================================

 @GetMapping("/admin/customers")
 public ResponseEntity<java.util.List<UserResponse>> getAllCustomersForAdmin() {

     return ResponseEntity.ok(
             userService.getAllCustomersForAdmin()
     );
 }
}











//package com.salonmanagementsystem.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.salonmanagementsystem.entity.LoginResponse;
//import com.salonmanagementsystem.entity.RegisterResponse;
//import com.salonmanagementsystem.entity.UserEntity;
//import com.salonmanagementsystem.service.UserService;
//
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:5179/")
//@Slf4j
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public RegisterResponse register(@RequestBody UserEntity user) {
//
//        log.info("Register API Called");
//
//        UserEntity savedUser = userService.registerUser(user);
//
//        RegisterResponse response = new RegisterResponse();
//        response.setMessage("User Registered Successfully");
//        response.setUser(savedUser);
//
//        return response;
//    }
//
////    @PostMapping("/login")
////    public String login(@RequestBody UserEntity user) {
////
////        log.info("Login API Called");
////
////        return userService.loginUser(user);
////    }
//    
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody UserEntity user) {
//    	  log.info("Login API Called");
//        return userService.loginUser(user);
//    }
//    
//    @DeleteMapping("/delete/{id}")
//    public String deleteUser(@PathVariable Integer id) {
//
//        return userService.deleteUser(id);
//    }
////    @GetMapping("/admin/hello")
////    public String adminHello() {
////
////        return "Welcome Admin";
////    }
//    @GetMapping("/user/hello")
//    public String userHello() {
//
//        return "Welcome User";
//    }
//}