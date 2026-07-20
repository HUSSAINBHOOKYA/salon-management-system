package com.salonmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.entity.LoginResponse;
import com.salonmanagementsystem.entity.RegisterResponse;
import com.salonmanagementsystem.entity.UserEntity;
import com.salonmanagementsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173/")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody UserEntity user) {

        log.info("Register API Called");

        UserEntity savedUser = userService.registerUser(user);

        RegisterResponse response = new RegisterResponse();
        response.setMessage("User Registered Successfully");
        response.setUser(savedUser);

        return response;
    }

//    @PostMapping("/login")
//    public String login(@RequestBody UserEntity user) {
//
//        log.info("Login API Called");
//
//        return userService.loginUser(user);
//    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserEntity user) {
    	  log.info("Login API Called");
        return userService.loginUser(user);
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {

        return userService.deleteUser(id);
    }
//    @GetMapping("/admin/hello")
//    public String adminHello() {
//
//        return "Welcome Admin";
//    }
    @GetMapping("/user/hello")
    public String userHello() {

        return "Welcome User";
    }
}