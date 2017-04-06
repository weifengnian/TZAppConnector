package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;

public class SystemMessagerDao extends SqlSessionDaoSupport implements ISystemMessagerDao {
	

	@Override
	public Integer insertFeedback(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("SystemMessagerDaoMapper.insertFeedback",map);
	}

	@Override
	public List<AppMessage> getMessage(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMessage",map);
	}

	@Override
	public AppMsgReceive getMsgReceive(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("SystemMessagerDaoMapper.getMsgReceive",map);
	}

}
