package com.oyyk.homepage.controller;

import com.oyyk.homepage.req.DocQueryReq;
import com.oyyk.homepage.req.DocSaveReq;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.resp.DocQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.service.DocService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/doc")
public class DocController {


    @Resource
    private DocService docService;

    @RequestMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }
    @RequestMapping("/all/{outCateId}")
    public CommonResp all(@PathVariable Long outCateId){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(outCateId);
        resp.setContent(list);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp resp = new CommonResp<>();
        if(!ObjectUtils.isEmpty(idsStr)){
            List<String> list = Arrays.asList(idsStr.split(","));
            docService.delete(list);
        }
        return resp;
    }
}
