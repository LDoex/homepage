package com.oyyk.homepage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello!post"+name;
    }
}
