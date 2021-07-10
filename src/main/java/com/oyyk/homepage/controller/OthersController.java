package com.oyyk.homepage.controller;

import com.oyyk.homepage.req.OthersQueryReq;
import com.oyyk.homepage.req.OthersSaveReq;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.resp.OthersQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.service.OthersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/others")
public class OthersController {


    @Resource
    private OthersService othersService;

    @RequestMapping("/list")
    public CommonResp list(@Valid OthersQueryReq req){
        CommonResp<PageResp<OthersQueryResp>> resp = new CommonResp<>();
        PageResp<OthersQueryResp> list = othersService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping("/all/{outCateId}")
    public CommonResp all(@PathVariable Long outCateId){
        CommonResp<List<OthersQueryResp>> resp = new CommonResp<>();
        List<OthersQueryResp> list = othersService.all(outCateId);
        resp.setContent(list);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody OthersSaveReq req){
        CommonResp resp = new CommonResp<>();
        othersService.save(req);
        return resp;
    }
}
