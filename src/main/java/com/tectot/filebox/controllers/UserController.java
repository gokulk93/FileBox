package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO only admin can access it
    @GetMapping("/register")
    public ModelAndView showRegistrationForm(Model model){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userDTO", new UserDTO());
        return modelAndView;
    }

    // TODO only admin can access it
    @PostMapping("/register")
    public String createUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "User creation has failed due to a validation error. Please try again.");
        } else if (userService.createUser(userDTO)) {
            model.addAttribute("message", "User created successfully!");
            model.addAttribute("userDTO", new UserDTO());
        } else {
            model.addAttribute("message", "User creation has failed. Please try again.");
        }

        return "register";
    }

}
