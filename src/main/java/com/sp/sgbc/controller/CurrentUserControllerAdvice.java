package com.sp.sgbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sp.sgbc.model.User;
import com.sp.sgbc.repository.UserRepository;

@ControllerAdvice
public class CurrentUserControllerAdvice {
  @Autowired
  UserRepository userRepository;

  @ModelAttribute("currentUser")
  public String getCurrentUser(Authentication authentication) {
    if (authentication != null) {
      User user = userRepository.findByEmail(((UserDetails) authentication.getPrincipal()).getUsername());
      if (user != null) // has to remove
        return user.getName();
    }
    return null;
  }
}