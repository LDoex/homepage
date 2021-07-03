package com.oyyk.homepage.controller;

import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.service.MemberinfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/memberinfo")
public class MemberinfoController {


    @Resource
    private MemberinfoService memberinfoService;

    @RequestMapping("/list")
    public CommonResp list(){
        CommonResp<List<Memberinfo>> resp = new CommonResp<>();
        List<Memberinfo> list = memberinfoService.list();
        resp.setContent(list);
        return resp;
    }
}
