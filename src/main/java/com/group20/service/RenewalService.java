package com.group20.service;

import com.group20.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenewalService {

    private final UserService userService;

    public RenewalService(UserService userService) {
        this.userService = userService;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndProcessRenewals() {
        List<User> usersToRenew = userService.findUsersWithRenewalToday();
        for (User user : usersToRenew) {
            userService.processRenewal(user);
        }
    }
    
}