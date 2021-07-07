package com.oyyk.homepage.mapper;

import com.oyyk.homepage.domain.HomeCategory;
import com.oyyk.homepage.domain.HomeCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HomeCategoryMapper {
    long countByExample(HomeCategoryExample example);

    int deleteByExample(HomeCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(HomeCategory record);

    int insertSelective(HomeCategory record);

    List<HomeCategory> selectByExample(HomeCategoryExample example);

    HomeCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HomeCategory record, @Param("example") HomeCategoryExample example);

    int updateByExample(@Param("record") HomeCategory record, @Param("example") HomeCategoryExample example);

    int updateByPrimaryKeySelective(HomeCategory record);

    int updateByPrimaryKey(HomeCategory record);
}