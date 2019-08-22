package org.common.eureka.service.impl;

import java.util.List;
import java.util.Map;

import org.common.eureka.mapper.DwDatasetMapper;
import org.common.eureka.entity.DwDataset;
import org.common.eureka.service.IDwDataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DwDataSetServiceImpl implements IDwDataSetService {
	
	@Autowired
	private DwDatasetMapper datasetMapper;

	@Override
	public int deleteByPrimaryKey(String id) {
		return datasetMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DwDataset record) {
		return datasetMapper.insert(record);
	}

	@Override
	public int insertSelective(DwDataset record) {
		return datasetMapper.insertSelective(record);
	}

	@Override
	public DwDataset queryByPrimaryKey(String id) {
		return datasetMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DwDataset record) {
		return datasetMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DwDataset record) {
		return datasetMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<DwDataset> queryAll(Map<String, Object> params) {
		return datasetMapper.selectAll(params);
	}
}
