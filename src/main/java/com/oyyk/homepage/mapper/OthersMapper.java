package com.oyyk.homepage.mapper;

import com.oyyk.homepage.domain.Others;
import com.oyyk.homepage.domain.OthersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OthersMapper {
    long countByExample(OthersExample example);

    int deleteByExample(OthersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Others record);

    int insertSelective(Others record);

    List<Others> selectByExampleWithBLOBs(OthersExample example);

    List<Others> selectByExample(OthersExample example);

    Others selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Others record, @Param("example") OthersExample example);

    int updateByExampleWithBLOBs(@Param("record") Others record, @Param("example") OthersExample example);

    int updateByExample(@Param("record") Others record, @Param("example") OthersExample example);

    int updateByPrimaryKeySelective(Others record);

    int updateByPrimaryKeyWithBLOBs(Others record);
}