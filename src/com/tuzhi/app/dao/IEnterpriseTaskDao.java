package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;
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
	
}
