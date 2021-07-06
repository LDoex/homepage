package com.oyyk.homepage.controller;

import com.oyyk.homepage.req.MemberinfoReq;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.resp.MemberinfoResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.service.MemberinfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/memberinfo")
public class MemberinfoController {


    @Resource
    private MemberinfoService memberinfoService;

    @RequestMapping("/list")
    public CommonResp list(MemberinfoReq req){
        CommonResp<PageResp<MemberinfoResp>> resp = new CommonResp<>();
        PageResp<MemberinfoResp> list = memberinfoService.list(req);
        resp.setContent(list);
        return resp;
    }
}
