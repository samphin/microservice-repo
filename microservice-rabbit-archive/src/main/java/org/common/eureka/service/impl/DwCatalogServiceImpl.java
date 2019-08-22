package org.common.eureka.service.impl;

import java.util.List;
import java.util.Map;

import org.common.eureka.mapper.DwCatalogMapper;
import org.common.eureka.entity.DwCatalog;
import org.common.eureka.service.IDwCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DwCatalogServiceImpl implements IDwCatalogService {
	
	@Autowired
	private DwCatalogMapper catalogMapper;

	@Override
	public int deleteByPrimaryKey(String id) {
		return catalogMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DwCatalog record) {
		return catalogMapper.insert(record);
	}

	@Override
	public int insertSelective(DwCatalog record) {
		return catalogMapper.insertSelective(record);
	}

	@Override
	public DwCatalog queryByPrimaryKey(String id) {
		return catalogMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DwCatalog record) {
		return catalogMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DwCatalog record) {
		return catalogMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<DwCatalog> queryAll(Map<String, Object> params) {
		return catalogMapper.selectAll(params);
	}
}
