package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.pojo.AppPickPeople;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.pojo.TaskUser;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public class EnterpriseTaskDao extends SqlSessionDaoSupport implements IEnterpriseTaskDao {
	
	@Override
	public Integer insertTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.insertTask",map);
	}

	@Override
	public List<AppTaskInfo> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		//userid＝－1查询所有任务
//		if("-1".equals(map.get("user_id"))){
//			map.remove("page");
//			map.remove("rows");
//		}
		
		if(StringUtil.isBlank(map.get("page"))){
			map.put("page", "1");
		}	
		if(StringUtil.isBlank(map.get("rows"))){
			map.put("rows", "999999");
		}
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getTask",map);
	}
	
	@Override
	public List<TaskUser> getTaskUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getTaskUser",map);
	}

	@Override
	public List<AppTaskUser> getOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getOrders",map);
	}

	@Override
	public Integer addOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addOrders",map);
	}
	
	@Override
	public Integer updateOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("EnterpriseTaskDaoMapper.updateOrders",map);
	}

	@Override
	public List<AppPickPeople> getPick(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getPick",map);
	}

	@Override
	public AppTaskInfo getEnterpriseTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("EnterpriseTaskDaoMapper.getEnterpriseTask",map);
	}

	@Override
	public Integer addTaskUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addTaskUser",map);
	}

}
