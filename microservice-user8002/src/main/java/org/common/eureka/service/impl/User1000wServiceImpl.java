package org.common.eureka.service.impl;

import org.common.eureka.mapper.User1000wMapper;
import org.common.eureka.entity.User1000w;
import org.common.eureka.service.IUser1000wService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class User1000wServiceImpl implements IUser1000wService {

	@Autowired
	private User1000wMapper user1000wMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.user1000wMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(User1000w record) {
		return this.user1000wMapper.insert(record);
	}

	@Override
	public int insertSelective(User1000w record) {
		return this.user1000wMapper.insertSelective(record);
	}

	@Override
	public User1000w queryByPrimaryKey(Integer id) {
		return this.user1000wMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(User1000w record) {
		return this.user1000wMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(User1000w record) {
		return this.user1000wMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<User1000w> queryAll(Map<String, Object> params) {
		return this.user1000wMapper.selectAll(params);
	}

}
