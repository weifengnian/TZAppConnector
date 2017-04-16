package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppBanner;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;
import com.tuzhi.app.util.StringUtil;

public class SystemMessagerDao extends SqlSessionDaoSupport implements ISystemMessagerDao {
	

	@Override
	public Integer insertFeedback(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("SystemMessagerDaoMapper.insertFeedback",map);
	}

	@Override
	public List<AppMessage> getMessage(Map<String, String> map) {
		// TODO Auto-generated method stub
		int pagenum=1;
		int pagesize=999999;
		if(map.get("page")!=null && StringUtils.isNumeric(map.get("page").toString()))
			pagenum=Integer.parseInt(map.get("page").toString());
		if(map.get("rows")!=null && StringUtils.isNumeric(map.get("rows").toString()))
			pagesize=Integer.parseInt(map.get("rows").toString());
		RowBounds row = StringUtil.getRowBounds(pagenum, pagesize);
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMessage",map,row);
	}

	@Override
	public AppMsgReceive getMsgReceive(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("SystemMessagerDaoMapper.getMsgReceive",map);
	}

	@Override
	public List<AppGoodField> getAllField(Map<String, String> map) {
		// TODO Auto-generated method stub
		int pagenum=1;
		int pagesize=999999;
		if(map.get("page")!=null && StringUtils.isNumeric(map.get("page").toString()))
			pagenum=Integer.parseInt(map.get("page").toString());
		if(map.get("rows")!=null && StringUtils.isNumeric(map.get("rows").toString()))
			pagesize=Integer.parseInt(map.get("rows").toString());
		RowBounds row = StringUtil.getRowBounds(pagenum, pagesize);
		return getSqlSession().selectList("SystemMessagerDaoMapper.getAllField",map,row);
	}

	@Override
	public List<AppBanner> getMyBanner(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMyBanner",map);
	}

}
