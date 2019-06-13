package org.common.eureka.service;

import java.util.List;
import java.util.Map;

import org.common.eureka.entity.DwDataset;

public interface IDwDataSetService {
	
	/**
	 * 根据主键删除
	 * @return
	 */
    int deleteByPrimaryKey(String id);

    /**
     * 插入单个对象
     * @param record
     * @return
     */
    int insert(DwDataset record);

    /**
     * 插入单个对象（字段有值就插入，无值不管）
     * @param record
     * @return
     */
    int insertSelective(DwDataset record);

    /**
     * 根据主键查找单个对象
     * @param userId
     * @return
     */
    DwDataset queryByPrimaryKey(String id);

    /**
     * 适合部分修改，有值就修改
     * @param userId
     * @return
     */
    int updateByPrimaryKeySelective(DwDataset record);

    /**
     * 全部修改
     * @param userId
     * @return
     */
    int updateByPrimaryKey(DwDataset record);
    
    /**
     * 查询所有
     * @param params
     * @return
     */
    List<DwDataset> queryAll(Map<String,Object> params);

}
