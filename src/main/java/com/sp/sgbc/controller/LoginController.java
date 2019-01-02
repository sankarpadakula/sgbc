package com.sp.sgbc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sp.sgbc.model.User;
import com.sp.sgbc.service.UserService;

@Controller
public class LoginController {

  @Autowired
  private UserService userService;

  @RequestMapping(value = { "/login" }, method = GET)
  public ModelAndView login() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("login");
    return modelAndView;
  }

  @RequestMapping(value = "/signup", method = GET)
  public ModelAndView registration() {
    ModelAndView modelAndView = new ModelAndView();
    User user = new User();
    modelAndView.addObject("user", user);
    modelAndView.setViewName("signup");
    return modelAndView;
  }

  @RequestMapping(value = "/signup", method = POST)
  public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    User userExists = userService.findUserByEmail(user.getEmail());
    if (userExists != null) {
      bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");
    }
    if (bindingResult.hasErrors()) {
      modelAndView.setViewName("signup");
    } else {
      userService.saveUser(user);
      modelAndView.addObject("successMessage", "User has been registered successfully");
      modelAndView.addObject("user", new User());
      modelAndView.setViewName("signup");

    }
    return modelAndView;
  }

  @RequestMapping(value = "/login", method = POST)
  public ModelAndView home() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
    modelAndView.setViewName("admin/home");
    return modelAndView;
  }

}
