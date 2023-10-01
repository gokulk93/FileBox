package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.Roles;
import com.tectot.filebox.security.CustomUserDetails;
import com.tectot.filebox.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final S3Client s3Client;
    private final FileService fileService;


    @Autowired
    public DashboardController(S3Client s3Client, FileService fileService) {
        this.s3Client = s3Client;
        this.fileService = fileService;
    }

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("isAdmin", userDetails.getRole().equals(Roles.ROLE_ADMIN.name()));
        model.addAttribute("files", fileService.listFilesFromS3Bucket(userDetails.getOrgName()));

        return "dashboard";
    }


    @GetMapping("/download/{key}")
    public ResponseEntity<InputStreamResource> downloadContent(@PathVariable String key, Model model, Authentication authentication) {

        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        try {
            ResponseBytes<GetObjectResponse> responseBytes = fileService.downloadFileBytesFromS3Bucket(key, userDetails.getOrgName());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(responseBytes.response().contentType()));
            headers.setContentLength(responseBytes.response().contentLength());
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"");

            InputStreamResource resource = new InputStreamResource(responseBytes.asInputStream());
            model.addAttribute("message", "File Downloaded successfully.");
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            model.addAttribute("message", "File download failed.");
            logger.error("Error while processing S3 request: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
