package com.oyyk.homepage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oyyk.homepage.domain.HomeCategory;
import com.oyyk.homepage.domain.HomeCategoryExample;
import com.oyyk.homepage.mapper.HomeCategoryMapper;
import com.oyyk.homepage.req.HomeCategoryQueryReq;
import com.oyyk.homepage.req.HomeCategorySaveReq;
import com.oyyk.homepage.resp.HomeCategoryQueryResp;
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
public class HomeCategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(HomeCategoryService.class);

    @Resource
    private HomeCategoryMapper homeCategoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<HomeCategoryQueryResp> list(HomeCategoryQueryReq req){

        HomeCategoryExample homeCategoryExample = new HomeCategoryExample();
        HomeCategoryExample.Criteria criteria = homeCategoryExample.createCriteria();


        PageHelper.startPage(req.getPage(), req.getSize());
        List<HomeCategory> homeCategoryList = homeCategoryMapper.selectByExample(homeCategoryExample);

        PageInfo<HomeCategory> pageInfo = new PageInfo<>(homeCategoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<HomeCategoryQueryResp> list = CopyUtil.copyList(homeCategoryList, HomeCategoryQueryResp.class);

        PageResp<HomeCategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public List<HomeCategoryQueryResp> all(){

        HomeCategoryExample homeCategoryExample = new HomeCategoryExample();

        homeCategoryExample.setOrderByClause("sort asc");


        List<HomeCategory> homeCategoryList = homeCategoryMapper.selectByExample(homeCategoryExample);


        List<HomeCategoryQueryResp> list = CopyUtil.copyList(homeCategoryList, HomeCategoryQueryResp.class);


        return list;

    }

    //保存
    public void save(HomeCategorySaveReq req){
        HomeCategory homeCategory = CopyUtil.copy(req, HomeCategory.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            homeCategory.setId(snowFlake.nextId());
            homeCategoryMapper.insert(homeCategory);
        }else{
            //更新
            homeCategoryMapper.updateByPrimaryKey(homeCategory);
        }
    }

    public void delete(Long id){
        homeCategoryMapper.deleteByPrimaryKey(id);
    }
}
