package com.group20.service;

import com.group20.Repository.UserRepository;
import com.group20.Strategy.Payment;
import com.group20.Strategy.PaymentFactory;
import com.group20.model.User;

import jakarta.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }


    @Transactional
    public void processRenewal(User user) {
        String paymentType = user.getPayment();
        Payment paymentMethod = PaymentFactory.getPaymentStrategy(paymentType);
        paymentMethod.pay(); 
        user.setRenewal(Date.valueOf(user.getRenewal().toLocalDate().plusYears(1)));
        userRepository.save(user);
    }

  
    public User registerUser(User user, String paymentType) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return null; 
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRenewal(Date.valueOf(LocalDate.now().plusYears(1)));
        user.setPayment(paymentType);

        Payment paymentMethod = PaymentFactory.getPaymentStrategy(paymentType);
        paymentMethod.pay();

        user = userRepository.save(user);

        notificationService.sendSignUpReceiptEmail(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRenewal().toString(),
                20.0 // registration fee
        );

        return user;
    }

  
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

 
    public List<User> findUsersWithRenewalToday() {
        LocalDate today = LocalDate.now();
        return userRepository.findByRenewal(Date.valueOf(today));
    }

    public User getAuthenticatedUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUsername);
    }

    public User updatePersonalInfo(String email, User updatedInfo) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setFirstName(updatedInfo.getFirstName());
            user.setLastName(updatedInfo.getLastName());
            user.setAddress(updatedInfo.getAddress());
            user.setEmail(updatedInfo.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public boolean updatePaymentInfo(String email, String payment, Long cardNumber) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; 
        }

        user.setPayment(payment);
        user.setCardNumber(cardNumber); 

        userRepository.save(user);

        return true; 
    }

    public boolean deleteUserAccount(String email) {
        var user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true; 
        }
        return false; 
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
