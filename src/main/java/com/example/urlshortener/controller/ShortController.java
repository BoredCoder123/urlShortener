package com.example.urlshortener.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ShortController {
    @GetMapping("/")

    public Date heartbeat(){
        return new Date(System.currentTimeMillis());
    }
}
