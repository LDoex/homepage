package com.oyyk.homepage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.domain.MemberinfoExample;
import com.oyyk.homepage.mapper.MemberinfoMapper;
import com.oyyk.homepage.req.MemberinfoReq;
import com.oyyk.homepage.req.MemberinfoResp;
import com.oyyk.homepage.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberinfoService {
    private static final Logger LOG = LoggerFactory.getLogger(MemberinfoService.class);

    @Resource
    private MemberinfoMapper memberinfoMapper;

    public List<MemberinfoResp> list(MemberinfoReq req){

        MemberinfoExample memberinfoExample = new MemberinfoExample();
        MemberinfoExample.Criteria criteria = memberinfoExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(2, 3);
        List<Memberinfo> memberinfoList = memberinfoMapper.selectByExample(memberinfoExample);

        PageInfo<Memberinfo> pageInfo = new PageInfo<>(memberinfoList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<MemberinfoResp> list = CopyUtil.copyList(memberinfoList, MemberinfoResp.class);

        return list;

    }
}
