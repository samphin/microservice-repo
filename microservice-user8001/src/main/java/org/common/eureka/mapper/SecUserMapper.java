package org.common.eureka.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.common.eureka.entity.SecUser;

public interface SecUserMapper {
	/**
	 * 根据主键删除
	 * @param userId
	 * @return
	 */
    int deleteByPrimaryKey(String userId);

    /**
     * 插入单个对象
     * @param record
     * @return
     */
    int insert(SecUser record);

    /**
     * 插入单个对象（字段有值就插入，无值不管）
     * @param record
     * @return
     */
    int insertSelective(SecUser record);

    /**
     * 根据主键查找单个对象
     * @param userId
     * @return
     */
    SecUser selectByPrimaryKey(String userId);

    /**
     * 根据名称查询
     * @param username
     * @return
     */
    SecUser selectByUsername(@Param("username") String username);

    /**
     * 适合部分修改，有值就修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SecUser record);

    /**
     * 全部修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(SecUser record);
    
    /**
     * 查询所有
     * @param params
     * @return
     */
    List<SecUser> selectAll(Map<String, Object> params);
}