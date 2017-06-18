package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.entity.Appquestion;
import com.tuzhi.app.entity.Appquestionreply;
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
	public AppTaskInfo getTaskDetail(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("EnterpriseTaskDaoMapper.getTaskDetail",map);
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

	@Override
	public Integer updateTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("EnterpriseTaskDaoMapper.updateTask",map);
	}

	@Override
	public List<AppPickPeople> getOrderUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getOrderUser",map);
	}

	@Override
	public Integer addQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addQuestion",map);
	}

	@Override
	public List<Appquestion> getAppquestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getAppquestion",map);
	}

	@Override
	public Integer addAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addAppquestionreply",map);
	}

	@Override
	public Appquestionreply getAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("EnterpriseTaskDaoMapper.getAppquestionreply",map);
	}

	@Override
	public Integer addCoursesQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addCoursesQuestion",map);
	}

	@Override
	public Integer addForumQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.addForumQuestion",map);
	}

	@Override
	public List<Appquestion> getQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(map.get("page"))){
			map.put("page", "1");
		}	
		if(StringUtil.isBlank(map.get("rows"))){
			map.put("rows", "999999");
		}
		
		/**
		 * courses_id传值，forum_id传-1则相应对应course_id下面的所有问题列表，
		 * 反之forum_id传值，course_id传-1则展示该forum_id下的所有列表
		 */
		if("-1".equals(map.get("forum_id"))){
			return getSqlSession().selectList("EnterpriseTaskDaoMapper.getCourseQuestion",map);
		}else if("-1".equals(map.get("courses_id"))){
			return getSqlSession().selectList("EnterpriseTaskDaoMapper.getForumQuestion",map);
		}else{
			return getSqlSession().selectList("EnterpriseTaskDaoMapper.getQuestion",map);
		}
	}

	@Override
	public List<Appquestionreply> getQuestionReply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getQuestionReply",map);
	}

}
