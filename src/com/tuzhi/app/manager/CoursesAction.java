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
import com.tuzhi.app.pojo.CoursesInfo;
import com.tuzhi.app.service.ICoursesService;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class CoursesAction extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	//日志  
	private final static Logger log = LoggerFactory.getLogger(CoursesAction.class);
	
	private ICoursesService coursesService;
	
	/**
	 * 查询我的课程
	 */
	public void myCourses(){
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
			List<CoursesInfo> ci = new ArrayList<CoursesInfo>();
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("page")) || StringUtil.isBlank(map.get("rows")) || 
					StringUtil.isBlank(map.get("token")) || StringUtil.isBlank(map.get("title"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				ci = coursesService.getMyCourses(map);
				if(ci.size()<=0){
					status = "26";
					retMsg = "课程列表获取失败";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
			if(ci.size()==0){
				num = -1;
			}
			for (int i = num; i < ci.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("course_id", ci.size()==0?"":ci.get(i).getId()==0?"":ci.get(i).getId());
				map3.put("course_img", ci.size()==0?"":ci.get(i).getImg_url()==null?"":ci.get(i).getImg_url());
				map3.put("study_num", ci.size()==0?"":ci.get(i).getStudy_num()==null?"":ci.get(i).getStudy_num());
				map3.put("course_type", ci.size()==0?"":ci.get(i).getCtname()==null?"":ci.get(i).getCtname());
				map3.put("course_title", ci.size()==0?"":ci.get(i).getName()==null?"":ci.get(i).getName());
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
	 * 查询课程详细
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
			List<CoursesInfo> cd = new ArrayList<CoursesInfo>();
			if(StringUtil.isBlank(map.get("course_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("course_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//课程详细
				cd = coursesService.getCourseDetails(map);
				if(cd.size() <= 0){
					status = "27";
					retMsg = "课程详情获取失败";
				}
			}
				
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
			if(cd.size()==0){
				num = -1;
			}
			for (int i = num; i < cd.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("course_id", cd.size()==0?"":cd.get(i).getId()==0?"":cd.get(i).getId());
				map3.put("course_img", cd.size()==0?"":cd.get(i).getImg_url()==null?"":cd.get(i).getImg_url());
				map3.put("study_num", cd.size()==0?"":cd.get(i).getStudy_num()==null?"":cd.get(i).getStudy_num());
				map3.put("course_type", cd.size()==0?"":cd.get(i).getCtname()==null?"":cd.get(i).getCtname());
				map3.put("course_title", cd.size()==0?"":cd.get(i).getName()==null?"":cd.get(i).getName());
				listMap.add(map3);
			}
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("list", listMap);
			map2.put("course_desc", cd.get(0).getContent());
			
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

	public ICoursesService getCoursesService() {
		return coursesService;
	}
	public void setCoursesService(ICoursesService coursesService) {
		this.coursesService = coursesService;
	}
}
