package com.tuzhi.app.manager;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.service.ISystemMessagerService;
import com.tuzhi.app.util.StringUtil;

public class SystemMessagerAction extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	//日志
	private final static Logger log = LoggerFactory.getLogger(SystemMessagerAction.class);
	
	private ISystemMessagerService systemMessagerService;
	private IAppUserInfoService appUserInfoService;
	
	/**
	 * 添加反馈意见
	 */
	public void feedback(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			String status = "0";
			String retMsg = "成功";
			
			//企业(y),个人(n)  type: 1:个人  2:企业
			if("n".equals(map.get("is_business"))){
				map.put("type", "1"); 
			}else{
				map.put("type", "2");
			}
			
			map.put("u_id", map.get("user_id")); //编号(type=1指用户id、type=2指企业id)
		
			//添加反馈意见
			int resultStatus = systemMessagerService.insertFeedback(map);
			if(resultStatus <= 0){
				status = "12";
				retMsg = "信息反馈失败";
			}
				
			log.info("------status:"+status+"-------retMsg:"+retMsg);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			
			String json = JSON.encode(resultMap);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/feedback.action");  //请求命令Url
//			logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
//			logMap.put("type", "");  //1:个人2：企业
//			logMap.put("version", map.get("version"));  //APP版本
//////		logMap.put("record_time", "");  //记录时间 (mapper文件默认当前时间)
//			logMap.put("result_code", status); //状态码
//			logMap.put("result_msg", retMsg); //状态码说明
//			logMap.put("token", map.get("token")); //系统唯一标识
//			logMap.put("req_content", map); //请求内容
//			logMap.put("resp_content", json); //相应内容
//			int resultLog = appUserInfoService.insertAppLog(logMap);
//			log.info("----resultLog:"+resultLog);
			return;
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---register--Exception:"+e.getMessage());
		}
	}
	
	/**
	 * 消息列表
	 */
	@SuppressWarnings("unchecked")
	public void message(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			String status = "0";
			String retMsg = "成功";
		
			//消息列表
			List<AppMessage> listMessage = systemMessagerService.getMessage(map);
			if(listMessage.size()<=0){
				listMessage = (List<AppMessage>) new AppMessage();
				status = "13";
				retMsg = "消息列表获取失败";
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			
			for (int i = 0; i < listMessage.size(); i++) {
				log.info("------status:"+status+"-------retMsg:"+retMsg);
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("msg_id", listMessage.get(i).getId());
				map3.put("title", listMessage.get(i).getTitle());
				map3.put("date", listMessage.get(i).getSendtime());
				
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("list", map3);
				listMap.add(map2);
			}
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			map1.put("data", listMap);
			
			String json = JSON.encode(map1);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/message.action");  //请求命令Url
//			logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
//			logMap.put("type", "");  //1:个人2：企业
//			logMap.put("version", map.get("version"));  //APP版本
////			logMap.put("record_time", "");  //记录时间 (mapper文件默认当前时间)
//			logMap.put("result_code", status); //状态码
//			logMap.put("result_msg", retMsg); //状态码说明
//			logMap.put("token", map.get("token")); //系统唯一标识
//			logMap.put("req_content", map); //请求内容
//			logMap.put("resp_content", json); //相应内容
//			int resultLog = appUserInfoService.insertAppLog(logMap);
//			log.info("----resultLog:"+resultLog);
			return;
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---register--Exception:"+e.getMessage());
		}
	}
	
	
	/**
	 * 系统消息详细
	 */
	@SuppressWarnings("unchecked")
	public void messageDetail(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			String status = "0";
			String retMsg = "成功";
		
			//消息列表
			List<AppMessage> listMessage = systemMessagerService.getMessage(map);
			if(listMessage == null){
				listMessage = (List<AppMessage>) new AppMessage();
				status = "14";
				retMsg = "消息详情获取失败";
			}
				
			log.info("------status:"+status+"-------retMsg:"+retMsg);
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("msg_id", listMessage.get(0).getId());
			map2.put("title", listMessage.get(0).getTitle());
			map2.put("content", listMessage.get(0).getContent());
//			map2.put("date", listMessage.get(0).getSendtime());
			map2.put("file_path", "");
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			map1.put("data", map2);
			
			String json = JSON.encode(map1);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/messageDetail.action");  //请求命令Url
//			logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
//			logMap.put("type", "");  //1:个人2：企业
//			logMap.put("version", map.get("version"));  //APP版本
////			logMap.put("record_time", "");  //记录时间 (mapper文件默认当前时间)
//			logMap.put("result_code", status); //状态码
//			logMap.put("result_msg", retMsg); //状态码说明
//			logMap.put("token", map.get("token")); //系统唯一标识
//			logMap.put("req_content", map); //请求内容
//			logMap.put("resp_content", json); //相应内容
//			int resultLog = appUserInfoService.insertAppLog(logMap);
//			log.info("----resultLog:"+resultLog);
			return;
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---register--Exception:"+e.getMessage());
		}
	}

	public ISystemMessagerService getSystemMessagerService() {
		return systemMessagerService;
	}

	public void setSystemMessagerService(
			ISystemMessagerService systemMessagerService) {
		this.systemMessagerService = systemMessagerService;
	}

	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}

	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}
	
}
