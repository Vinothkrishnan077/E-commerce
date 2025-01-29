package com.assessment.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.assessment.ecommerce.model.User;
import com.assessment.ecommerce.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public List<User> getUserByList() {
        return userRepository.findAll();
    }

    public List<User> searchAll(User user) {
        return userRepository.findAll(Example.of(user));
    }

    public User register(User user) {
        if (user.getRole().equals("Admin")) {
            throw new RuntimeException("Unauthorized request for role: " + user.getRole());
        }
        if (user.getRole().equals("Staff")) {
            throw new RuntimeException("Staff accounts can only be created by an admin.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exits with this email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User createStaff(User user, String adminEmail) {
        Optional<User> admin = userRepository.findByEmail(adminEmail);
        if (admin.isEmpty() || !admin.get().getRole().equals("Admin")) {
            throw new RuntimeException("Only an admin can create staff accounts.");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
            user.setRole("Staff");
            user.setCreatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        // Check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public User updateAll(Long userId, Map<String, Object> values) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with this id: " + userId));

        if (user.getRole().equals("Admin")) {
            throw new RuntimeException("Unauthorized request for role: " + user.getRole());
        }
        values.forEach((key, value) -> {
            switch (key) {
                case "userId":
                    user.getUserId();
                    break;
                case "userName":
                    user.setUserName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "password":
                    user.setPassword(passwordEncoder.encode((String) value));
                    break;
                case "createdAt":
                    user.setCreatedAt((LocalDateTime) value);
                    break;
                case "role":
                    user.setRole((String) value);
                    break;
                default:
                    break;
            }
        });
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.saveAndFlush(user);
    }

    public String deleteByUser(Long userId) {
        userRepository.findById(userId);
        return "User deleted successfully:" + userId;
    }
}
