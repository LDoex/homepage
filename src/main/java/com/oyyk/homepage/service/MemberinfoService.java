package com.oyyk.homepage.service;

import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.domain.MemberinfoExample;
import com.oyyk.homepage.mapper.MemberinfoMapper;
import com.oyyk.homepage.req.MemberinfoReq;
import com.oyyk.homepage.req.MemberinfoResp;
import com.oyyk.homepage.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberinfoService {

    @Resource
    private MemberinfoMapper memberinfoMapper;

    public List<MemberinfoResp> list(MemberinfoReq req){
        MemberinfoExample memberinfoExample = new MemberinfoExample();
        MemberinfoExample.Criteria criteria = memberinfoExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        List<Memberinfo> memberinfoList = memberinfoMapper.selectByExample(memberinfoExample);

        List<MemberinfoResp> list = CopyUtil.copyList(memberinfoList, MemberinfoResp.class);

        return list;

    }
}
