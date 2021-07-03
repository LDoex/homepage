package com.oyyk.homepage.controller;

import com.oyyk.homepage.domain.test;
import com.oyyk.homepage.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
public class TestController {

    @Value("${test.hello}")
    private String testHello;

    @Resource
    private TestService testService;

    @RequestMapping("/hello")
    public String hello(){
        return "hello1" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello!post"+name;
    }

    @RequestMapping("/list")
    public List<test> list(){
        return testService.list();
    }
}
