package com.oyyk.homepage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class TestController {

    @Value("${test.hello}")
    private String testHello;

    @RequestMapping("/hello")
    public String hello(){
        return "hello" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello!post"+name;
    }
}
