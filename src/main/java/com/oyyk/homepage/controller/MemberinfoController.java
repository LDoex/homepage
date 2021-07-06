package com.oyyk.homepage.controller;

import com.oyyk.homepage.req.MemberinfoQueryReq;
import com.oyyk.homepage.req.MemberinfoSaveReq;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.resp.MemberinfoQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.service.MemberinfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/memberinfo")
public class MemberinfoController {


    @Resource
    private MemberinfoService memberinfoService;

    @RequestMapping("/list")
    public CommonResp list(MemberinfoQueryReq req){
        CommonResp<PageResp<MemberinfoQueryResp>> resp = new CommonResp<>();
        PageResp<MemberinfoQueryResp> list = memberinfoService.list(req);
        resp.setContent(list);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@RequestBody MemberinfoSaveReq req){
        CommonResp resp = new CommonResp<>();
        memberinfoService.save(req);
        return resp;
    }
}
