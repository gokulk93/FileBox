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

    @Autowired
    private S3Client s3Client;

    @Autowired
    private FileService fileService;

    @GetMapping(value = { "/", "/index" })
    public String home(){
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
//        TODO boolean isAdmin = customUserDetailsService.hasRole("ROLE_ADMIN");
        boolean isAdmin = false;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("files", fileService.listFilesFromS3Bucket(s3Client,"mytestbucket"));
        return "dashboard";
    }

    @GetMapping("/download/{key}")
    public String downloadFile(@PathVariable String key, Model model, HttpServletResponse response) {
        logger.info("Key : "+ key);
        boolean isAdmin = false;
        model.addAttribute("isAdmin", isAdmin);
        fileService.downloadFileFromS3Bucket(s3Client, key, response);
        return "dashboard";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


}
