package com.tectot.filebox.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = "/files")
public class FileController {

    @GetMapping(value = "/")
    public String displayList(Model model){

        return null;
    }
}
