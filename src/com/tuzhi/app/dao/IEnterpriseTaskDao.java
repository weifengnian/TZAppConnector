package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.pojo.AppPickPeople;
import com.tuzhi.app.pojo.AppTaskInfo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public interface IEnterpriseTaskDao {
	
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
	 *任务 （可以选择的人员）
	 * @param map
	 * @return
	 */
	public List<AppPickPeople> getPick(Map<String,String> map);
	
}
