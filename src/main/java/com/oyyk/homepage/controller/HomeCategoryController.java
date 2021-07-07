package com.oyyk.homepage.controller;

import com.oyyk.homepage.req.HomeCategoryQueryReq;
import com.oyyk.homepage.req.HomeCategorySaveReq;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.resp.HomeCategoryQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.service.HomeCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/homeCategory")
public class HomeCategoryController {


    @Resource
    private HomeCategoryService homeCategoryService;

    @RequestMapping("/list")
    public CommonResp list(@Valid HomeCategoryQueryReq req){
        CommonResp<PageResp<HomeCategoryQueryResp>> resp = new CommonResp<>();
        PageResp<HomeCategoryQueryResp> list = homeCategoryService.list(req);
        resp.setContent(list);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody HomeCategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        homeCategoryService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        homeCategoryService.delete(id);
        return resp;
    }
}
