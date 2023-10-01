package com.tectot.filebox.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAppController {

    @GetMapping(value = {"/", "/index"})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/header","/users/header", "/organisations/header", "/files/header"})
    public String header() {
        return "components/header";
    }

    @GetMapping({"/footer","/users/footer", "/organisations/footer", "/files/footer"})
    public String footer() {
        return "components/footer";
    }

}
