package com.oyyk.homepage.service;

import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.mapper.MemberinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberinfoService {

    @Resource
    private MemberinfoMapper memberinfoMapper;

    public List<Memberinfo> list(){
        return memberinfoMapper.selectByExample(null);
    }
}
