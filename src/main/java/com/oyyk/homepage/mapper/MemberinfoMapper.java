package com.oyyk.homepage.mapper;

import com.oyyk.homepage.domain.Memberinfo;
import com.oyyk.homepage.domain.MemberinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberinfoMapper {
    long countByExample(MemberinfoExample example);

    int deleteByExample(MemberinfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Memberinfo record);

    int insertSelective(Memberinfo record);

    List<Memberinfo> selectByExample(MemberinfoExample example);

    Memberinfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Memberinfo record, @Param("example") MemberinfoExample example);

    int updateByExample(@Param("record") Memberinfo record, @Param("example") MemberinfoExample example);

    int updateByPrimaryKeySelective(Memberinfo record);

    int updateByPrimaryKey(Memberinfo record);
}