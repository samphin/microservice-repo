package org.common.eureka.dao;

import java.util.List;
import java.util.Map;

import org.common.eureka.entity.DwDataset;

public interface DwDatasetMapper {
    int deleteByPrimaryKey(String id);

    int insert(DwDataset record);

    int insertSelective(DwDataset record);

    DwDataset selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DwDataset record);

    int updateByPrimaryKey(DwDataset record);
    
    List<DwDataset> selectAll(Map<String, Object> params);
}