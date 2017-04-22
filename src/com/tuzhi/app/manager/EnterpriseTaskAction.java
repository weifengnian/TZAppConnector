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
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.service.IEnterpriseTaskService;
import com.tuzhi.app.service.ISystemMessagerService;
import com.tuzhi.app.util.StringUtil;
/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class EnterpriseTaskAction extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	//日志  
	private final static Logger log = LoggerFactory.getLogger(EnterpriseTaskAction.class);
	
	private IEnterpriseTaskService enterpriseTaskService;
	private IAppUserInfoService appUserInfoService;
	private ISystemMessagerService systemMessagerService;
	
	/**
	 * 企业发布任务（添加）
	 */
	public void addTask(){
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
			if(StringUtil.isBlank(map.get("title")) || StringUtil.isBlank(map.get("content")) || 
					StringUtil.isBlank(map.get("start_time")) || StringUtil.isBlank(map.get("end_time")) ||
					StringUtil.isBlank(map.get("money")) || StringUtil.isBlank(map.get("desc"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("e_id").length()>10 || map.get("field").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				AppEnterprisesInfo enfo = appUserInfoService.getEnterprises(map);
				if(enfo != null){
					//获取领域信息
					Map<String,String> eiMap = new HashMap<String,String>();
					eiMap.put("id", map.get("field"));
					List<AppGoodField> gf = systemMessagerService.getAllField(eiMap);
					if(gf.size()>0){
						//发布者Id
						map.put("create_user_id", String.valueOf(enfo.getId()));
						//添加发布任务
						int resultStstu = enterpriseTaskService.insertTask(map);
						if(resultStstu<=0){
							status = "21";
							retMsg = "任务发布失败";
						}
					}else{
						status = "25";
						retMsg = "领域编号无效";
					}
				}else{
					status = "24";
					retMsg = "企业编号无效";
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
	 * 查询任务列表
	 */
	public void taskList(){
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
			List<AppTaskInfo> at = new ArrayList<AppTaskInfo>();
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("page")) || StringUtil.isBlank(map.get("rows")) || 
					StringUtil.isBlank(map.get("token")) || StringUtil.isBlank(map.get("type"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if((!"1".equals(map.get("type")) && !"2".equals(map.get("type"))) || map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				at = enterpriseTaskService.getTask(map);
				if(at.size()<=0){
					status = "22";
					retMsg = "任务列表获取失败";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
			if(at.size()==0){
				num = -1;
			}
			for (int i = num; i < at.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("task_id", at.size()==0?"":at.get(i).getId()==0?"":at.get(i).getId());
				map3.put("task_start_date", at.size()==0?"":at.get(i).getStart_time()==null?"":at.get(i).getStart_time());
				map3.put("task_end_date", at.size()==0?"":at.get(i).getEnd_time()==null?"":at.get(i).getEnd_time());
				map3.put("sender", at.size()==0?"":at.get(i).getCreate_per()==null?"":at.get(i).getCreate_per());
				map3.put("release_time", at.size()==0?"":at.get(i).getCreate_time()==null?"":at.get(i).getCreate_time());
				map3.put("title", at.size()==0?"":at.get(i).getTitle()==null?"":at.get(i).getTitle());
				map3.put("money", at.size()==0?"":at.get(i).getMoney()==0?"":at.get(i).getMoney());
				map3.put("province", at.size()==0?"":at.get(i).getProvince()==null?"":at.get(i).getProvince());
				map3.put("city", at.size()==0?"":at.get(i).getCity()==null?"":at.get(i).getCity());
				map3.put("district", at.size()==0?"":at.get(i).getDistrict()==null?"":at.get(i).getDistrict());
				map3.put("address", at.size()==0?"":at.get(i).getAddress()==null?"":at.get(i).getAddress());
				map3.put("field", at.size()==0?"":at.get(i).getName()==null?"":at.get(i).getName());
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
	 * 查询任务详细
	 */
	public void taskDetails(){
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
			List<AppTaskInfo> at = new ArrayList<AppTaskInfo>();
			if(StringUtil.isBlank(map.get("task_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("task_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//任务详细 （注意这里要去掉token）
				map.remove("token");
				at = enterpriseTaskService.getTask(map);
				if(at.size() <= 0){
					status = "23";
					retMsg = "任务详情获取失败";
				}
			}
				
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("title", at.size()==0?"":at.get(0).getTitle()==null?"":at.get(0).getTitle());
			map2.put("content", at.size()==0?"":at.get(0).getContent()==null?"":at.get(0).getContent());
			map2.put("order_id", at.size()==0?"":at.get(0).getId()==0?"":at.get(0).getId());
			map2.put("order_state", at.size()==0?"":at.get(0).getStatus()==0?"":at.get(0).getStatus());
			map2.put("actor_num", at.size()==0?"":at.get(0).getCnt()==null?"":at.get(0).getCnt());
			
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

	public IEnterpriseTaskService getEnterpriseTaskService() {
		return enterpriseTaskService;
	}

	public void setEnterpriseTaskService(
			IEnterpriseTaskService enterpriseTaskService) {
		this.enterpriseTaskService = enterpriseTaskService;
	}

	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}

	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}

	public ISystemMessagerService getSystemMessagerService() {
		return systemMessagerService;
	}

	public void setSystemMessagerService(
			ISystemMessagerService systemMessagerService) {
		this.systemMessagerService = systemMessagerService;
	}
}
