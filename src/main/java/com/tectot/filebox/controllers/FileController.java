package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.OrganisationDTO;
import com.tectot.filebox.dtos.Roles;
import com.tectot.filebox.security.CustomUserDetails;
import com.tectot.filebox.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/upload")
    public String fileUploadPage(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("files") MultipartFile[] files,
            Model model,
            Authentication authentication) {
        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        for (MultipartFile file : files) {
            try {
                String uploadStatus = fileService.uploadFile(file,userDetails.getOrgName());
                model.addAttribute("uploadStatus", uploadStatus);
            } catch (Exception e) {
                logger.error("Errow while upload the files, {}", e.getMessage());
            }
        }
        return "redirect:/files/upload";
    }

}
