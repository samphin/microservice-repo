package org.common.eureka.mapper;

import java.util.List;
import java.util.Map;

import org.common.eureka.entity.User1000w;

public interface User1000wMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User1000w record);

    int insertSelective(User1000w record);

    User1000w selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User1000w record);

    int updateByPrimaryKey(User1000w record);
    
    List<User1000w> selectAll(Map<String, Object> params);
}