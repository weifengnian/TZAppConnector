package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;

public interface ISystemMessagerService {
	
	/**
	 * 添加反馈意见
	 * @param map
	 * @return
	 */
	public Integer insertFeedback(Map<String,String> map);
	
	/**
	 * 查询消息
	 * @param map
	 * @return
	 */
	public List<AppMessage> getMessage(Map<String,String> map);
	
	/**
	 * 查询消息管理人员表
	 * @param map
	 * @return
	 */
	public AppMsgReceive getMsgReceive(Map<String,String> map);
	
}
