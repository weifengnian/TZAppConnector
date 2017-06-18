package com.tuzhi.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.dao.IEnterpriseTaskDao;
import com.tuzhi.app.entity.AppAddress;
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
 * @Data: 2017年4月20日	
 * @Copyright:
 */
public class EnterpriseTaskService implements IEnterpriseTaskService {

	private IEnterpriseTaskDao enterpriseTaskDao;
	private IAppUserInfoDao appUserInfoDao;
	
	public IAppUserInfoDao getAppUserInfoDao() {
		return appUserInfoDao;
	}
	public void setAppUserInfoDao(IAppUserInfoDao appUserInfoDao) {
		this.appUserInfoDao = appUserInfoDao;
	}
	public IEnterpriseTaskDao getEnterpriseTaskDao() {
		return enterpriseTaskDao;
	}
	public void setEnterpriseTaskDao(IEnterpriseTaskDao enterpriseTaskDao) {
		this.enterpriseTaskDao = enterpriseTaskDao;
	}

	@Override
	public Integer insertTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		AppAddress ads = null;
		//修改用户接单地址（注意，这里使用先通过id查询,存在就修改，否则添加）
		if(!StringUtil.isBlank(map.get("address")) || !StringUtil.isBlank(map.get("province")) || 
				!StringUtil.isBlank(map.get("city")) || !StringUtil.isBlank(map.get("district"))){
			
			Map<String,String> maps = new HashMap<String,String>();
			maps.put("details", map.get("address")); //详细地址
			maps.put("pro_name", map.get("province")); //省
			maps.put("city_name", map.get("city")); //市
			maps.put("dis_name", map.get("district")); //区
			maps.put("only_id", StringUtil.getShortUUID());
			//添加地址
			int num = appUserInfoDao.addAddress(maps);
			if(num>0){
				//获取地址id
				ads = appUserInfoDao.getAddress(maps);
			}
		}
		if(ads!=null){
			map.put("address_id", String.valueOf(ads.getId()));
		}
		
		//任务编号
		map.put("order_code", "BH"+String.valueOf(System.currentTimeMillis()));
		//任务状态（添加任务状态默认为1，  5任务过期，4任务取消，3任务完成，2接单(工作种)，1报名中(报名)）
		map.put("status", "1");
		
		//任务id
		int taskId = 0;
		//添加
		int num = enterpriseTaskDao.insertTask(map);
		if(num>0){
			Map<String,String> taskMap = new HashMap<String,String>();
			taskMap.put("order_code", map.get("order_code"));
			//查询任务编号
			AppTaskInfo taskInfo = enterpriseTaskDao.getEnterpriseTask(taskMap);
			if(taskInfo!=null){
				//添加企业任务关联人员
				taskMap.remove("order_code");
				taskMap.put("order_id", String.valueOf(taskInfo.getId()));
				taskMap.put("user_id", map.get("create_user_id"));
				taskMap.put("status", "1");
				taskMap.put("type", "2");
				num = enterpriseTaskDao.addTaskUser(taskMap);
				if(num>0){
					taskId = taskInfo.getId();
				}
			}
		}
		
		return taskId;
	}

	@Override
	public List<AppTaskInfo> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTask(map);
	}
	
	@Override
	public AppTaskInfo getTaskDetail(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTaskDetail(map);
	}
	
	@Override
	public List<TaskUser> getTaskUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTaskUser(map);
	}
	
	@Override
	public List<AppTaskUser> getOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getOrders(map);
	}
	
	@Override
	public Integer addOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		int num = enterpriseTaskDao.updateTask(map);
		if(num>0){
			num = enterpriseTaskDao.addOrders(map);
		}
		return num;
	}
	
	@Override
	public Integer updateOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		int num = enterpriseTaskDao.updateTask(map);
		if(num>0){
			num = enterpriseTaskDao.updateOrders(map);
		}
		return num;
	}
	
	@Override
	public List<AppPickPeople> getPick(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getPick(map);
	}
	@Override
	public Integer updateTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.updateTask(map);
	}
	@Override
	public List<AppPickPeople> getOrderUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getOrderUser(map);
	}
	@Override
	public AppTaskInfo getEnterpriseTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getEnterpriseTask(map);
	}
	@Override
	public Integer addTaskUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.addTaskUser(map);
	}
	
	@Override
	public Integer addQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		Map<String,String> QtMap = new HashMap<String,String>();
		
		//添加问题
		map.put("only_id", StringUtil.getShortUUID());
		int num = enterpriseTaskDao.addQuestion(map);
		if(num>0){
			List<Appquestion> at = enterpriseTaskDao.getAppquestion(map);
			if(at.size()>0){
				QtMap.put("question_id", String.valueOf(at.get(0).getId()));
			}
		}
		
		@SuppressWarnings("unused")
		int cnt = 0;
		//添加，课程问题关联表
		if(!StringUtil.isBlank(map.get("courses_id")) && !StringUtil.isBlank(QtMap.get("question_id"))){
			QtMap.put("courses_id", map.get("courses_id"));
			cnt = enterpriseTaskDao.addCoursesQuestion(QtMap);
		}
		
		//添加，论坛问题关联表
		if(!StringUtil.isBlank(map.get("forum_id")) && !StringUtil.isBlank(QtMap.get("question_id"))){
			QtMap.put("forum_id", map.get("forum_id"));
			cnt = enterpriseTaskDao.addForumQuestion(QtMap);
		}
		
		return num;
	}
	
	@Override
	public List<Appquestion> getAppquestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getAppquestion(map);
	}
	
	@Override
	public Integer addAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.addAppquestionreply(map);
	}
	
	@Override
	public Appquestionreply getAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getAppquestionreply(map);
	}
	@Override
	public List<Appquestion> getQuestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getQuestion(map);
	}
	@Override
	public List<Appquestionreply> getQuestionReply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getQuestionReply(map);
	}

}
