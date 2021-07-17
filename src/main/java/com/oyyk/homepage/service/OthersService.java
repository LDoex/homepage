package com.oyyk.homepage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oyyk.homepage.domain.Others;
import com.oyyk.homepage.domain.OthersExample;
import com.oyyk.homepage.mapper.OthersMapper;
import com.oyyk.homepage.req.OthersQueryReq;
import com.oyyk.homepage.req.OthersSaveReq;
import com.oyyk.homepage.resp.OthersQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OthersService {
    private static final Logger LOG = LoggerFactory.getLogger(OthersService.class);

    @Resource
    private OthersMapper othersMapper;

    public PageResp<OthersQueryResp> list(OthersQueryReq req){

        OthersExample othersExample = new OthersExample();
        OthersExample.Criteria criteria = othersExample.createCriteria();


        PageHelper.startPage(req.getPage(), req.getSize());
        List<Others> othersList = othersMapper.selectByExample(othersExample);

        PageInfo<Others> pageInfo = new PageInfo<>(othersList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<OthersQueryResp> list = CopyUtil.copyList(othersList, OthersQueryResp.class);

        PageResp<OthersQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public List<OthersQueryResp> all(Long outCateId){

        OthersExample othersExample = new OthersExample();
        OthersExample.Criteria criteria = othersExample.createCriteria();

        criteria.andIdEqualTo(outCateId);


        List<Others> othersList = othersMapper.selectByExample(othersExample);


        List<OthersQueryResp> list = CopyUtil.copyList(othersList, OthersQueryResp.class);


        return list;

    }
    public List<OthersQueryResp> findContent(Long outCateId){

        OthersExample othersExample = new OthersExample();
        OthersExample.Criteria criteria = othersExample.createCriteria();

        criteria.andIdEqualTo(outCateId);


        List<Others> othersList = othersMapper.selectByExampleWithBLOBs(othersExample);


        List<OthersQueryResp> list = CopyUtil.copyList(othersList, OthersQueryResp.class);


        return list;

    }

    //保存
    public void save(OthersSaveReq req){
        Others others = CopyUtil.copy(req, Others.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            others.setId(req.getId());
            othersMapper.insert(others);
        } else{
            int count = othersMapper.updateByPrimaryKeyWithBLOBs(others);
            if(count==0){
                othersMapper.insert(others);
            }
        }
    }

    public void delete(Long id){
        othersMapper.deleteByPrimaryKey(id);
    }


}
