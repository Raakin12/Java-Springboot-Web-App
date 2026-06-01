package com.group20.controller;

import com.group20.model.User;
import com.group20.service.UserService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestParam String paymentType) {
        User registeredUser = userService.registerUser(user, paymentType);
        if (registeredUser == null) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();

    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean emailExists = userService.findByEmail(email) != null;
        return ResponseEntity.ok(emailExists);
    }

    @GetMapping
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/personal")
    public ResponseEntity<?> updatePersonalInfo(@RequestParam String email, @RequestBody User updatedInfo) {
        User existingUser = userService.findByEmail(email);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        existingUser.setFirstName(updatedInfo.getFirstName());
        existingUser.setLastName(updatedInfo.getLastName());
        existingUser.setAddress(updatedInfo.getAddress());

        if (!existingUser.getEmail().equals(updatedInfo.getEmail())) {
            if (userService.findByEmail(updatedInfo.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An account already exists with that email.");
            }
            existingUser.setEmail(updatedInfo.getEmail());
        }

        User updatedUser = userService.saveUser(existingUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update/payment")
    public ResponseEntity<User> updatePaymentInfo(@RequestParam String email,
            @RequestBody Map<String, Object> updatedInfo) {
        String paymentMethod = (String) updatedInfo.get("payment");
        Long cardNumber = Long.valueOf(updatedInfo.get("cardNumber").toString()); 


        boolean success = userService.updatePaymentInfo(email, paymentMethod, cardNumber);
        return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam String email) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!email.equals(currentUsername)) {
            return ResponseEntity.status(403).body("You can only delete your own account.");
        }

        boolean isDeleted = userService.deleteUserAccount(email);

        if (isDeleted) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().body("Account deleted successfully. You have been logged out.");
        } else {
            return ResponseEntity.status(404).body("User not found or deletion failed.");
        }
    }

}