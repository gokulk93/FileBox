package com.tectot.filebox.controllers;

import com.tectot.filebox.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private S3Client s3Client;

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String dashboard(Model model) {
//        TODO Need to handle authorization
        boolean isAdmin = false;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("files", fileService.listFilesFromS3Bucket(s3Client,"mytestbucket"));
        return "dashboard";
    }

    @GetMapping("/download/{key}")
    public void downloadFile(@PathVariable String key, Model model, HttpServletResponse response) {
        boolean isAdmin = false;
        model.addAttribute("isAdmin", isAdmin);
        if(fileService.downloadFileFromS3Bucket(s3Client, key, response)){
            model.addAttribute("message", "File Downloaded successfully.");
        }else{
            model.addAttribute("message", "File download failed.");
        }
    }

}
