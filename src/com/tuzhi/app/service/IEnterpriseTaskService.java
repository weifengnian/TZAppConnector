package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.pojo.AppPickPeople;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.pojo.TaskUser;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public interface IEnterpriseTaskService {
	
	/**
	 * 企业发布任务（添加）
	 * @param map
	 * @return
	 */
	public Integer insertTask(Map<String,String> map);
	
	/**
	 * 查询任务列表
	 * @param map
	 * @return
	 */
	public List<AppTaskInfo> getTask(Map<String,String> map);
	
	/**
	 * 查询企业发布单条任务
	 * @param map
	 * @return
	 */
	public AppTaskInfo getEnterpriseTask(Map<String,String> map);
	
	/**
	 * 查询任务参与人员
	 * @param map
	 * @return
	 */
	public List<TaskUser> getTaskUser(Map<String,String> map);
	
	/**
	 * 查询该单是否被接
	 * @param map
	 * @return
	 */
	public List<AppTaskUser> getOrders(Map<String,String> map);
	
	/**
	 * 添加接单信息
	 * @param map
	 * @return
	 */
	public Integer addOrders(Map<String,String> map);
	
	/**
	 * 修改接单信息
	 * @param map
	 * @return
	 */
	public Integer updateOrders(Map<String,String> map);
	
	/**
	 *任务 （可以选择的人员）
	 * @param map
	 * @return
	 */
	public List<AppPickPeople> getPick(Map<String,String> map);
	
	/**
	 * 修改任务状态
	 * @param map
	 * @return
	 */
	public Integer updateTask(Map<String,String> map);
	
	/**
	 * 查询符合接单人员（可推送人员）
	 * @param map
	 * @return
	 */
	public List<AppPickPeople> getOrderUser(Map<String,String> map);
	
	/**
	 * 添加企业任务关联人员
	 * @param map
	 * @return
	 */
	public Integer addTaskUser(Map<String,String> map);
}
