package com.salonmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salonmanagementsystem.dto.LoginResponse;
import com.salonmanagementsystem.dto.RegisterResponse;
import com.salonmanagementsystem.dto.UserResponse;
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

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody UserEntity user) {

        log.info("Register API Called");

        UserEntity savedUser = userService.registerUser(user);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        RegisterResponse response = new RegisterResponse();
        response.setMessage("User Registered Successfully");
        response.setUser(userResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserEntity user) {

        log.info("Login API Called");

        return ResponseEntity.ok(userService.loginUser(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> userHello() {

        return ResponseEntity.ok("Welcome User");
    }

    @GetMapping("/admin/hello")
    public ResponseEntity<String> adminHello() {

        return ResponseEntity.ok("Welcome Admin");
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