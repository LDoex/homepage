package com.oyyk.homepage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oyyk.homepage.domain.Content;
import com.oyyk.homepage.domain.ContentExample;
import com.oyyk.homepage.domain.Doc;
import com.oyyk.homepage.domain.DocExample;
import com.oyyk.homepage.mapper.ContentMapper;
import com.oyyk.homepage.mapper.DocMapper;
import com.oyyk.homepage.req.DocQueryReq;
import com.oyyk.homepage.req.DocSaveReq;
import com.oyyk.homepage.resp.DocQueryResp;
import com.oyyk.homepage.resp.PageResp;
import com.oyyk.homepage.util.CopyUtil;
import com.oyyk.homepage.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    public PageResp<DocQueryResp> list(DocQueryReq req){

        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();


        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public List<DocQueryResp> all(Long outCateId){

        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();

        criteria.andOutcateIdEqualTo(outCateId);

        docExample.setOrderByClause("sort asc");


        List<Doc> docList = docMapper.selectByExample(docExample);


        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);


        return list;

    }

    //保存
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            //新增内容
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            //更新
            docMapper.updateByPrimaryKey(doc);

            //更新content,要用带大字段的方法
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count == 0){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }
    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        } else {
            return content.getContent();
        }
    }
    public List<Content> findContent(List<String> idsStr){
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria criteria = contentExample.createCriteria();

        criteria.andIdIn(idsStr);
        List<Content> contents = contentMapper.selectByExampleWithBLOBs(contentExample);
        if(ObjectUtils.isEmpty(contents)){
            return Collections.emptyList();
        } else {
            return contents;
        }
    }

}
