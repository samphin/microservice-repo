package org.common.eureka.dao;

import java.util.List;
import java.util.Map;

import org.common.eureka.entity.DwCatalog;

public interface DwCatalogMapper {
    int deleteByPrimaryKey(String id);

    int insert(DwCatalog record);

    int insertSelective(DwCatalog record);

    DwCatalog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DwCatalog record);

    int updateByPrimaryKey(DwCatalog record);
    
    List<DwCatalog> selectAll(Map<String, Object> params);
}