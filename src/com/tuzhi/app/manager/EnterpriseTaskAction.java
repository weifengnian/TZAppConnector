package com.tuzhi.app.manager;

import java.io.BufferedReader;
import java.io.IOException;
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

import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.entity.Appquestion;
import com.tuzhi.app.entity.Appquestionreply;
import com.tuzhi.app.pojo.AppPickPeople;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.pojo.TaskUser;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.service.IEnterpriseTaskService;
import com.tuzhi.app.service.ISystemMessagerService;
import com.tuzhi.app.util.StringUtil;
import com.tuzhi.app.util.TransUtil;
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
	@SuppressWarnings("unused")
	public void addTask(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			map.put("create_time", StringUtil.getDisplayYMDHMS());
			
			//任务编号
			int taskId = 0;
			
			String status = "0";
			String retMsg = "成功";
			if(StringUtil.isBlank(map.get("title")) || StringUtil.isBlank(map.get("content")) || 
					StringUtil.isBlank(map.get("start_time")) || StringUtil.isBlank(map.get("end_time")) ||
					StringUtil.isBlank(map.get("money"))){
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
						//发布者
						map.put("create_per", enfo.getName()==null?"":enfo.getName());
						//添加发布任务
						taskId = enterpriseTaskService.insertTask(map);
						if(taskId<=0){
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			//推送消息(推送给当前任务领域的所有人员，并且是实名制人员)
			if("0".equals(status)){
				Map<String,String> umap = new HashMap<String,String>();
				umap.put("task_id", String.valueOf(taskId));
				List<AppPickPeople> apl = enterpriseTaskService.getOrderUser(umap);
				log.info("--sendTask--apl:"+apl.size());
				if(apl.size()>0){
					for (int i = 0; i < apl.size(); i++) {
						Map<String,String> taskMap = new HashMap<String,String>();
						taskMap.put("order_id", String.valueOf(taskId));
						taskMap.put("user_id", apl.get(i).getUser_id());
						taskMap.put("status", "0");
						int num = enterpriseTaskService.addTaskUser(taskMap);
					}
					StringUtil.sendTask(String.valueOf(taskId),map,apl,"BM");
				}
			}
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"addTaskTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("e_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "2");  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---addTask--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 查询任务列表
	 */
	public void taskList(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			if(StringUtil.isBlank(map.get("user_id"))){
				map.put("token", "");
				map.put("type", "");
			}
			
		
			//验证参数     type=1指用户、type=2指企业
			List<AppTaskInfo> at = new ArrayList<AppTaskInfo>();
			if(StringUtil.isBlank(map.get("page")) || StringUtil.isBlank(map.get("rows"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				at = enterpriseTaskService.getTask(map);
				if(at.size()<=0){
					status = "0";
					retMsg = "无任务信息";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(at.size()==0){
//				num = -1;
//			}
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
				map3.put("time", at.size()==0?"":at.get(i).getTime()==null?"":at.get(i).getTime());
				listMap.add(map3);
			}
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("list", listMap);
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			map1.put("data", map2);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"taskListTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", map.get("type"));  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---taskList--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	
	/**
	 * 查询任务详细
	 */
	public void taskDetails(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
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
				//查任务状态，根据任务状态来判断，任务详情获取那些人
				Map<String,String> mp = new HashMap<String,String>();
				mp.put("task_id", map.get("task_id"));
				List<AppTaskInfo> atf = enterpriseTaskService.getTask(mp);
				if(atf.size()>0){
					map.put("status", String.valueOf(atf.get(0).getStatus()));
				}
				//任务详细 （注意这里要去掉token）
				map.remove("token");
				at = enterpriseTaskService.getTask(map);
				if(at.size() <= 0){
					status = "0";
					retMsg = "无任务详情";
				}
				List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
				int num = 0;
//				if(at.size()==0){
//					num = -1;
//				}
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
			}
			
			List<TaskUser> tu = new ArrayList<TaskUser>();
			tu = enterpriseTaskService.getTaskUser(map);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int actor_num = 0; //订单参与人数
			int num = 0;
//			if(tu.size()==0){
//			num = -1;
//		}
			for (int i = num; i < tu.size(); i++) {
				actor_num++;
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("user_id", tu.size()==0?"":tu.get(i).getUser_id()==0?"":tu.get(i).getUser_id());
				map3.put("user_name", tu.size()==0?"":tu.get(i).getUser_name()==null?"":tu.get(i).getUser_name());
				map3.put("token", tu.size()==0?"":tu.get(i).getToken()==null?"":tu.get(i).getToken());
				map3.put("url",  tu.size()==0?"":tu.get(i).getUrl()==null?"":tu.get(i).getUrl());
				listMap.add(map3);
			}
			Map<String,Object> map4 = new HashMap<String,Object>();
			map4.put("list", listMap);
				
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("title", at.size()==0?"":at.get(0).getTitle()==null?"":at.get(0).getTitle());
			map2.put("content", at.size()==0?"":at.get(0).getContent()==null?"":at.get(0).getContent());
			map2.put("order_id", at.size()==0?"":at.get(0).getId()==0?"":at.get(0).getId());
			map2.put("order_state", at.size()==0?"0":at.get(0).getStatus()==0?"0":at.get(0).getStatus());
			map2.put("actor_num", actor_num);
			map2.put("people", map4);
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			map1.put("data", map2);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"taskDetailsTsk.action");  //请求命令Url
					logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "");  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---messageDetail--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
	   String d1 = "2014-03-01"; 
       String d2 = "2014-03-02"; 
       if(d1.compareTo(d2)<0){
    	   System.out.println("--0"+d1.compareTo(d2));  
       }else{
    	   System.out.println(d1.compareTo(d2));
       }
	}
	
	/**
	 * 报名、接单
	 */
	public void orders(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			//验证参数 
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("token")) 
					|| StringUtil.isBlank(map.get("task_id")) || StringUtil.isBlank(map.get("type"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10 || map.get("task_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				map.put("status", map.get("type"));

				Map<String,String> mp = new HashMap<String,String>();
				mp.put("task_id", map.get("task_id"));
				List<AppTaskInfo> at = enterpriseTaskService.getTask(mp);
				if(at.size()>0){
					if(2==at.get(0).getStatus() && "2".equals(map.get("type"))){
						status = "37";
						retMsg = "任务已被接收";
					}else if(3==at.get(0).getStatus() && "3".equals(map.get("type"))){
						status = "38";
						retMsg = "任务已被完成";
					}else if(4==at.get(0).getStatus() && "4".equals(map.get("type"))){
						status = "39";
						retMsg = "企业已取消该任务";
					}else if((at.get(0).getStatus()==5 || at.get(0).getEnd_time().compareTo(StringUtil.getDisplayYMDHMS())<0) && "5".equals(map.get("type"))){
						map.put("status", "5");
						enterpriseTaskService.updateTask(map);
						status = "40";
						retMsg = "任务已过期";
					}else{
						//查询该用户是否报名或者接单
						List<AppTaskUser> tu = enterpriseTaskService.getOrders(map);
						if(tu.size()<=0){
							//添加(并修改订单状态，service层)
							int num = enterpriseTaskService.addOrders(map);
							if(num<=0){
								status = "33";
								retMsg = "失败";
							}
						}else{
							if(tu.get(0).getStatus()==1 && "1".equals(map.get("type"))){
								status = "36";
								retMsg = "已报名成功，不可重复报名";
							}else{
								//更新用户任务状态(并修改订单状态，service层)
								int num = enterpriseTaskService.updateOrders(map);
								if(num<=0){
									status = "33";
									retMsg = "失败";
								}
							}
						}
					}
				}else{
					status = "32";
					retMsg = "任务编号无效";
				}
			}	
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
			response.getWriter().write(json);
			
			//判断用户是否接单，如果是接单则推送一条消息
			if("2".equals(map.get("type")) && "0".equals(status)){
				Map<String,String> taskMap = new HashMap<String,String>();
				taskMap.put("id", map.get("task_id"));
				//查询任务内容
				AppTaskInfo taskInfo = enterpriseTaskService.getEnterpriseTask(taskMap);
				if(taskInfo != null){
					
					Map<String,String> UMap = new HashMap<String,String>();
					UMap.put("user_id", map.get("user_id"));
					AppUserDetailInfo uf = appUserInfoService.getAppUser(UMap);
					
					AppPickPeople app = new AppPickPeople();
					app.setPhoneNo(uf==null?"":uf.getMobile_phone());
					List<AppPickPeople> apl = new ArrayList<AppPickPeople>();
					apl.add(app);
					
					//定义Map
					Map<String,String> Pushmap = new HashMap<String,String>();
					Pushmap.put("start_time", taskInfo.getStart_time());
					Pushmap.put("end_time", taskInfo.getEnd_time());
					Pushmap.put("create_per", taskInfo.getCreate_per());
					Pushmap.put("create_time", taskInfo.getCreate_time());
					Pushmap.put("title", taskInfo.getTitle());
					Pushmap.put("money", String.valueOf(taskInfo.getMoney()));
					Pushmap.put("address", taskInfo.getAddress());
					Pushmap.put("field", taskInfo.getFieldid());
					
					StringUtil.sendTask(map.get("task_id"),Pushmap,apl,"JD");
				}
			}
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"ordersTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "");  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---orders--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	
	/**
	 * 选人
	 */
	public void pick(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			//验证参数 
			List<AppPickPeople> at = new ArrayList<AppPickPeople>();
			if(StringUtil.isBlank(map.get("e_id")) || StringUtil.isBlank(map.get("task_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("task_id").length()>10 || map.get("e_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//任务 （可以选择的人员）
				at = enterpriseTaskService.getPick(map);
				if(at.size() <= 0){
					status = "0";
					retMsg = "无人员可选";
				}
			}
			
			List<Map<String,Object>> listMapAt = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(at.size()==0){
//				num = -1;
//			}
			for (int i = num; i < at.size(); i++) {
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("user_id", at.size()==0?"":at.get(i).getUser_id()==null?"":at.get(i).getUser_id());
				map2.put("token", at.size()==0?"":at.get(i).getToken()==null?"":at.get(i).getToken());
				map2.put("user_name", at.size()==0?"":at.get(i).getUser_name()==null?"":at.get(i).getUser_name());
				map2.put("url", at.size()==0?"":at.get(i).getUrl()==null?"":at.get(i).getUrl());
				map2.put("auth_time", at.size()==0?"":at.get(i).getAuth_time()==null?"":at.get(i).getAuth_time());
				map2.put("field", at.size()==0?"":at.get(i).getField()==null?"":at.get(i).getField());
			
				//查用户接单地址
				Map<String,String> arsMap = new HashMap<String,String>();
				arsMap.put("user_id", String.valueOf(map2.get("user_id")));
			    List<AppAddress> la = appUserInfoService.getAddressList(arsMap);
			    List<Map<String,Object>> listMapLa = new ArrayList<Map<String,Object>>();
			    int cnt = 0;
//				if(la.size()==0){
//					cnt = -1;
//				}
				for (int j = cnt; j < la.size(); j++) {
					Map<String,Object> adrMap = new HashMap<String,Object>();
					adrMap.put("id", la.size()==0?"":la.get(j).getId()==0?"":la.get(j).getId());
					adrMap.put("pro_id", la.size()==0?"":la.get(j).getPro_id()==null?"":la.get(j).getPro_id());
					adrMap.put("pro_name", la.size()==0?"":la.get(j).getPro_name()==null?"":la.get(j).getPro_name());
					adrMap.put("city_id", la.size()==0?"":la.get(j).getCity_id()==null?"":la.get(j).getCity_id());
					adrMap.put("city_name", la.size()==0?"":la.get(j).getCity_name()==null?"":la.get(j).getCity_name());
					adrMap.put("dis_id", la.size()==0?"":la.get(j).getDis_id()==null?"":la.get(j).getDis_id());
					adrMap.put("dis_name", la.size()==0?"":la.get(j).getDis_name()==null?"":la.get(j).getDis_name());
					adrMap.put("street_id", la.size()==0?"":la.get(j).getStreet_id()==null?"":la.get(j).getStreet_id());
					adrMap.put("street_name", la.size()==0?"":la.get(j).getStreet_name()==null?"":la.get(j).getStreet_name());
					adrMap.put("details", la.size()==0?"":la.get(j).getDetails()==null?"":la.get(j).getDetails());
					listMapLa.add(adrMap);
				}
				map2.put("order_address", listMapLa);
				listMapAt.add(map2);
			}
			
			Map<String,Object> fdMap1 = new HashMap<String,Object>();
			fdMap1.put("list", listMapAt);
			
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			map1.put("data", fdMap1);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"pickTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("e_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "");  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---pick--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 问题回复
	 */
	public void reply(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			//验证参数
			if(StringUtil.isBlank(map.get("question_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("question_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				List<Appquestion> at = enterpriseTaskService.getAppquestion(map);
				if(at.size()<=0){
					status = "34";
					retMsg = "问题不存在";
				}else{
					int num = enterpriseTaskService.addAppquestionreply(map);
					if(num<=0){
						status = "35";
						retMsg = "问题回复失败";
					}
				}
			}
				
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"replyTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", map.get("type"));  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---reply--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 新增问题
	 */
	public void addQuestion(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			//验证参数
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("user_name"))
					|| StringUtil.isBlank(map.get("content")) || StringUtil.isBlank(map.get("title"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				int num = enterpriseTaskService.addQuestion(map);
				if(num<=0){
					status = "41";
					retMsg = "新增问题失败";
				}
			}
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"addQuestionTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", map.get("type"));  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---addQuestion--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 问题列表
	 */
	public void questionList(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			List<Appquestion> at = new ArrayList<Appquestion>();
			//验证参数
			if(map.get("user_id").length()>10 || map.get("courses_id").length()>10 || map.get("forum_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				at = enterpriseTaskService.getQuestion(map);
				if(at.size()<=0){
					status = "0";
					retMsg = "问题列表为空";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(at.size()==0){
//				num = -1;
//			}
			for (int i = num; i < at.size(); i++) {
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("question_id", at.size()==0?"":at.get(i).getId()==0?"":at.get(i).getId());
				map2.put("title", at.size()==0?"":at.get(i).getTitle()==null?"":at.get(i).getTitle());
				map2.put("content", at.size()==0?"":at.get(i).getContent()==null?"":at.get(i).getContent());
				map2.put("time", at.size()==0?"":at.get(i).getCreate_time()==null?"":at.get(i).getCreate_time());
				map2.put("create_id", at.size()==0?"":at.get(i).getUser_id()==0?"":at.get(i).getUser_id());
				map2.put("create_name", at.size()==0?"":at.get(i).getCreate_per()==null?"":at.get(i).getCreate_per());
				
				//查询问题最后回复人员
				Map<String,String> map1 = new HashMap<String,String>();
				map1.put("question_id", String.valueOf(map2.get("question_id")));
				Appquestionreply atr = enterpriseTaskService.getAppquestionreply(map1);
				
				map2.put("reply_user_id", atr==null?"":atr.getUser_id()==0?"":atr.getUser_id());
				map2.put("reply_user_name", atr==null?"":atr.getUser_name()==null?"":atr.getUser_name());
				map2.put("reply_time", atr==null?"":atr.getUpdate_time()==null?"":atr.getUpdate_time());
				map2.put("reply_content", atr==null?"":atr.getContent()==null?"":atr.getContent());
				listMap.add(map2);
			}
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			resultMap.put("list", listMap);
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"questionListTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", map.get("type"));  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---questionList--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 回复问题列表
	 */
	public void replyQuestionList(){
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			log.info("----request--map:"+map);
			
			String status = "0";
			String retMsg = "成功";
			
			List<Appquestionreply> atr = new ArrayList<Appquestionreply>();
			//验证参数
			if(StringUtil.isBlank(map.get("question_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("question_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				atr = enterpriseTaskService.getQuestionReply(map);
				if(atr.size()<=0){
					status = "0";
					retMsg = "问题回复列表为空";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(at.size()==0){
//				num = -1;
//			}
			for (int i = num; i < atr.size(); i++) {
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("reply_user_id", atr.size()==0?"":atr.get(i).getUser_id()==0?"":atr.get(i).getUser_id());
				map2.put("reply_user_name", atr.size()==0?"":atr.get(i).getUser_name()==null?"":atr.get(i).getUser_name());
				map2.put("reply_time", atr.size()==0?"":atr.get(i).getUpdate_time()==null?"":atr.get(i).getUpdate_time());
				map2.put("reply_content", atr.size()==0?"":atr.get(i).getContent()==null?"":atr.get(i).getContent());
				map2.put("courses_id", atr.size()==0?"":atr.get(i).getCourses_id()==0?"":atr.get(i).getCourses_id());
				map2.put("forum_id", atr.size()==0?"":atr.get(i).getForum_id()==0?"":atr.get(i).getForum_id());
				listMap.add(map2);
			}
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			resultMap.put("list", listMap);
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"replyQuestionListTsk.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", map.get("type"));  //1:个人2：企业
					logMap.put("version", map.get("version"));  //APP版本
					logMap.put("req_content", map.toString().length()>8000?map.toString().substring(0, 8000):map.toString()); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", map.get("token")); //系统唯一标识
					logMap.put("result_code", status); //状态码
					logMap.put("result_msg", retMsg); //状态码说明
					int resultLog = appUserInfoService.insertAppLog(logMap);
					log.info("----resultLog:"+resultLog);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.info("--------add_log:"+e.getMessage());
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("---replyQuestionList--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
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
