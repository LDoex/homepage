package com.oyyk.homepage.service;

import com.oyyk.homepage.domain.test;
import com.oyyk.homepage.mapper.testMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

    @Resource
    private testMapper tm;

    public List<test> list(){
        return tm.list();
    }
}
