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

import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.pojo.AppUserDetailInfo;
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
			
			//验证参数     type=1指用户、type=2指企业
			if(StringUtil.isBlank(map.get("opinion_content")) || StringUtil.isBlank(map.get("type")) ||
				StringUtil.isBlank(map.get("user_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if((!"1".equals(map.get("type")) && !"2".equals(map.get("type"))) || map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//企业(2),个人(1)
				if("1".equals(map.get("type"))){
					//查询用户
					AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
					if(userInfo == null){
						status = "09";
						retMsg = "该用户已不存在";
					}else{
						//添加反馈意见
						int resultStatus = systemMessagerService.insertFeedback(map);
						if(resultStatus <= 0){
							status = "12";
							retMsg = "信息反馈失败";
						}
					}
				}else{
					//查询企业信息
					AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
					if(enterInfo==null){
						status = "09";
						retMsg = "该用户已不存在";
					}else{
						//添加反馈意见
						int resultStatus = systemMessagerService.insertFeedback(map);
						if(resultStatus <= 0){
							status = "12";
							retMsg = "信息反馈失败";
						}
					}
				}
			}
				
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
		
			//验证参数     type=1指用户、type=2指企业
			List<AppMessage> lm = new ArrayList<AppMessage>();
			if(StringUtil.isBlank(map.get("type")) || StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if((!"1".equals(map.get("type")) && !"2".equals(map.get("type"))) || map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				lm = systemMessagerService.getMessage(map);
				if(lm.size()<=0){
					status = "13";
					retMsg = "消息列表获取失败";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
			if(lm.size()==0){
				num = -1;
			}
			for (int i = num; i < lm.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("msg_id", lm.size()==0?"":lm.get(i).getId()==0?"":lm.get(i).getId());
				map3.put("title", lm.size()==0?"":lm.get(i).getTitle()==null?"":lm.get(i).getTitle());
				map3.put("date", lm.size()==0?"":lm.get(i).getSendtime()==null?"":lm.get(i).getSendtime());
				listMap.add(map3);
			}
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("list", listMap);
			
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
			
			//验证参数 
			List<AppMessage> lm = new ArrayList<AppMessage>();
			if(StringUtil.isBlank(map.get("msg_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("msg_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				lm = systemMessagerService.getMessage(map);
				if(lm.size() <= 0){
					status = "14";
					retMsg = "消息详情获取失败";
				}
			}
				
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("msg_id", lm.size()<=0?"":lm.get(0).getId()==0?"":lm.get(0).getId());
			map2.put("title", lm.size()<=0?"":lm.get(0).getTitle()==null?"":lm.get(0).getTitle());
			map2.put("content", lm.size()<=0?"":lm.get(0).getContent()==null?"":lm.get(0).getContent());
//			map2.put("date", lm.size()<=0?"":lm.get(0).getSendtime()==null?"":lm.get(0).getSendtime());
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
			log.info("---messageDetail--Exception:"+e.getMessage());
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
