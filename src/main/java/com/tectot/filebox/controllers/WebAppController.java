package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class WebAppController {

    private static Logger logger =  LoggerFactory.getLogger(WebAppController.class);

    @GetMapping(value = { "/", "/index" })
    public String home(){
        System.out.println("Index");
        return "index";
    }



    @GetMapping("/admin")
    public String showAdminPage() {
        System.out.println("Admin");
        return "admin";
    }

    @GetMapping("/login")
    public String login(){
        System.out.println("Admin");
        return "login";
    }


}
