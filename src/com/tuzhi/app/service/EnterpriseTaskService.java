package com.tuzhi.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.dao.IEnterpriseTaskDao;
import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppTaskUser;
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
				num = enterpriseTaskDao.addTaskUser(taskMap);
			}
		}
		
		return num;
	}

	@Override
	public List<AppTaskInfo> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTask(map);
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

}
