package com.assessment.ecommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.ecommerce.model.User;
import com.assessment.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
    public List<User> getUserByList() {
        return userService.getUserByList();
    }

    @GetMapping("/search")
    public List<User> search(User user) {
        return userService.searchAll(user);
    }

    @PostMapping("/sign-up")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/create-staff")
    public ResponseEntity<User> createStaff(@RequestBody User user, @RequestParam String adminEmail) {
        return ResponseEntity.ok(userService.createStaff(user, adminEmail));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        User user = userService.loginUser(email, password);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<User> updateAll(@PathVariable Long userId, @RequestBody Map<String, Object> values) {
        User user = userService.updateAll(userId, values);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("userId")
    public String deleteByUser(@PathVariable Long userId) {
        return userService.deleteByUser(userId);
    }

}
