package com.oyyk.homepage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.domain.MemberinfoExample;
import com.oyyk.homepage.mapper.MemberinfoMapper;
import com.oyyk.homepage.req.MemberinfoQueryReq;
import com.oyyk.homepage.req.MemberinfoSaveReq;
import com.oyyk.homepage.resp.MemberinfoQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.util.CopyUtil;
import com.oyyk.homepage.util.SnowFlake;
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

    @Resource
    private SnowFlake snowFlake;

    public PageResp<MemberinfoQueryResp> list(MemberinfoQueryReq req){

        MemberinfoExample memberinfoExample = new MemberinfoExample();
        MemberinfoExample.Criteria criteria = memberinfoExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Memberinfo> memberinfoList = memberinfoMapper.selectByExample(memberinfoExample);

        PageInfo<Memberinfo> pageInfo = new PageInfo<>(memberinfoList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<MemberinfoQueryResp> list = CopyUtil.copyList(memberinfoList, MemberinfoQueryResp.class);

        PageResp<MemberinfoQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    //保存
    public void save(MemberinfoSaveReq req){
        Memberinfo memberinfo = CopyUtil.copy(req, Memberinfo.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            memberinfo.setId(snowFlake.nextId());
            memberinfoMapper.insert(memberinfo);
        }else{
            //更新
            memberinfoMapper.updateByPrimaryKey(memberinfo);
        }
    }
}
