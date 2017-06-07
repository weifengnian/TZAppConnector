package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppBanner;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
public interface ISystemMessagerDao {
	
	/**
	 * 添加反馈意见
	 * @param map
	 * @return
	 */
	public Integer insertFeedback(Map<String,String> map);
	
	/**
	 * 查询消息列表
	 * @param map
	 * @return
	 */
	public List<AppMessage> getMessage(Map<String,String> map);
	
	/**
	 * 查询消息详情
	 * @param map
	 * @return
	 */
	public List<AppMessage> getMessageDetail(Map<String,String> map);
	
	/**
	 * 查询消息管理人员表
	 * @param map
	 * @return
	 */
	public AppMsgReceive getMsgReceive(Map<String,String> map);
	
	/**
	 * 获取所有领域
	 * @param map
	 * @return
	 */
	public List<AppGoodField> getAllField(Map<String,String> map);
	
	/**
	 * 获取广告图片
	 * @param map
	 * @return
	 */
	public List<AppBanner> getMyBanner(Map<String,String> map);

}
