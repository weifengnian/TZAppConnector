package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import com.tuzhi.app.entity.AppBanner;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
public class SystemMessagerDao extends SqlSessionDaoSupport implements ISystemMessagerDao {
	

	@Override
	public Integer insertFeedback(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("SystemMessagerDaoMapper.insertFeedback",map);
	}

	@Override
	public List<AppMessage> getMessage(Map<String, String> map) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(map.get("page"))){
			map.put("page", "1");
		}	
		if(StringUtil.isBlank(map.get("rows"))){
			map.put("rows", "999999");
		}
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMessage",map);
	}
	
	@Override
	public List<AppMessage> getMessageDetail(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMessageDetail",map);
	}

	@Override
	public AppMsgReceive getMsgReceive(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("SystemMessagerDaoMapper.getMsgReceive",map);
	}

	@Override
	public List<AppGoodField> getAllField(Map<String, String> map) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(map.get("page"))){
			map.put("page", "1");
		}	
		if(StringUtil.isBlank(map.get("rows"))){
			map.put("rows", "999999");
		}
		return getSqlSession().selectList("SystemMessagerDaoMapper.getAllField",map);
	}

	@Override
	public List<AppBanner> getMyBanner(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("SystemMessagerDaoMapper.getMyBanner",map);
	}

}
