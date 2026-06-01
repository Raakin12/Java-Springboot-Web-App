package com.group20.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	 @GetMapping("/status")
	public boolean isAuthenticated() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        return authentication != null && authentication.isAuthenticated()
            && authentication.getAuthorities().stream().noneMatch(
                authority -> authority.getAuthority().equals("ROLE_ANONYMOUS"));
	 }
}
