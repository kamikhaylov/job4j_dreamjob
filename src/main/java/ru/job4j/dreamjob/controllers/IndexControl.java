package ru.job4j.dreamjob.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControl {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}