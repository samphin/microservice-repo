package org.common.eureka.service;

import java.util.List;
import java.util.Map;

import org.common.eureka.entity.SecUser;

public interface ISecUserService {
	
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
    SecUser queryByPrimaryKey(String userId);

    /**
     * 适合部分修改，有值就修改
     * @param userId
     * @return
     */
    int updateByPrimaryKeySelective(SecUser record);

    /**
     * 全部修改
     * @param userId
     * @return
     */
    int updateByPrimaryKey(SecUser record);
    
    /**
     * 查询所有
     * @param params
     * @return
     */
    List<SecUser> queryAll(Map<String,Object> params);
    
    /**
     * 查询用户目录信息
     * @param userId
     * @return
     */
    String queryUserCatalogInfos(String userId);
    
    /**
     * 将查找用户目录信息设置到队列中
     * @param userId
     */
    void sendUserCatalogInfos(String userId);

}
