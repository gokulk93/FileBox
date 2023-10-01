package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.OrganisationDTO;
import com.tectot.filebox.dtos.Roles;
import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.services.OrganisationService;
import com.tectot.filebox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;
    private final OrganisationService organisationService;

    @Autowired
    public UserController(UserService userService, OrganisationService organisationService) {
        this.userService = userService;
        this.organisationService = organisationService;
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm(@RequestParam(required = false) String message) {
        ModelAndView modelAndView = new ModelAndView("users/createUser");

        modelAndView.addObject("userDTO", new UserDTO());
        modelAndView.addObject("organisations", organisationService.findAllOrgs());
        modelAndView.addObject("roles", Roles.values());

        if (message != null) {
            modelAndView.addObject("message", message);
        }

        return modelAndView;
    }


    @PostMapping("/register")
    public String createUser(
            @ModelAttribute @Valid UserDTO userDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        String message;

        if (bindingResult.hasErrors()) {
            message = "User creation has failed due to a validation error. Please try again.";
        } else if (userService.createUser(userDTO)) {
            message = "User created successfully!";
        } else {
            message = "User creation has failed. Please try again later.";
        }

        redirectAttributes.addAttribute("message", message);
        return "redirect:/users/register";
    }


}
