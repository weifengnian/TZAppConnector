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

import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.pojo.CoursesInfo;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.service.ICoursesService;
import com.tuzhi.app.util.StringUtil;
import com.tuzhi.app.util.TransUtil;

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
	private IAppUserInfoService appUserInfoService;
	
	/**
	 * 查询我的课程
	 */
	public void myCourses(){
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
			List<CoursesInfo> ci = new ArrayList<CoursesInfo>();
			if(StringUtil.isBlank(map.get("page")) || StringUtil.isBlank(map.get("rows"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10 || map.get("course_type_id").length()>10 ){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				ci = coursesService.getMyCourses(map);
				if(ci.size()<=0){
					status = "0";
					retMsg = "无课程信息";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(ci.size()==0){
//				num = -1;
//			}
			for (int i = num; i < ci.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("course_id", ci.size()==0?"":ci.get(i).getId()==0?"":ci.get(i).getId());
				map3.put("course_img", ci.size()==0?"":ci.get(i).getImg_url()==null?"":ci.get(i).getImg_url());
				
				map.put("course_id", String.valueOf(ci.size()==0?"":ci.get(i).getId()==0?"":ci.get(i).getId()));
				CoursesInfo cnt = coursesService.getMcCntUsr(map);
				map3.put("study_num", cnt==null?"":cnt.getStudy_num()==null?"":cnt.getStudy_num());
				
				map3.put("course_type_id", ci.size()==0?"":ci.get(i).getCourses_type_id()==0?"":ci.get(i).getCourses_type_id());
				map3.put("course_type", ci.size()==0?"":ci.get(i).getCtname()==null?"":ci.get(i).getCtname());
				map3.put("course_title", ci.size()==0?"":ci.get(i).getName()==null?"":ci.get(i).getName());
				map3.put("course_time", ci.size()==0?"":ci.get(i).getUpdate_time()==null?"":ci.get(i).getUpdate_time());
				map3.put("status", ci.size()==0?"":ci.get(i).getSastutas()==null?"":ci.get(i).getSastutas());
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
					logMap.put("url", TransUtil.LOG_URL+"myCoursesCus.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "1");  //1:个人2：企业
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
			log.info("---myCourses--Exception:"+e.getMessage());
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
	 * 课程详细
	 */
	public void courseDetails(){
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
			
			List<CoursesInfo> zj = new ArrayList<CoursesInfo>();
			//验证参数 
			if(StringUtil.isBlank(map.get("course_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("course_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//查询用户 (通过token查询)
				AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
				//查询企业信息
				AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
				
				if(userInfo!=null || enterInfo!=null){
					//查询课程章节
					zj = coursesService.getChapter(map);
					if(zj.size() <= 0){
						status = "0";
						retMsg = "无课程详情";
					}
					
					//客户一旦请求课程明细，将改课程添加到我的课程中，  （这里先查询再添加）
					if(zj.size()>0){
						Map<String,String> cuMap = new HashMap<String,String>();
						cuMap.put("user_id", String.valueOf(userInfo!=null?userInfo.getId():enterInfo.getId()));
						cuMap.put("courses_id", String.valueOf(zj.get(0).getId()));
						cuMap.put("type", userInfo!=null?"1":"2");
						//查询当前用户是否拥有，当前课程
						int cnt = coursesService.getCoursesUser(cuMap);
						if(cnt<=0){
							//将当前课程赋给当前用户（添加）
							cnt = coursesService.addCoursesUser(cuMap);
						}
					}
					
				}else{
					status = "09";
					retMsg = "该用户已不存在";
				}
			}
			
				
			List<Map<String,Object>> listMapZj = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(zj.size()==0){
//				num = -1;
//			}
			for (int i = num; i < zj.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("chapter_id", zj.size()==0?"":zj.get(i).getChapter_id()==null?"":zj.get(i).getChapter_id());
				map3.put("chapter_title", zj.size()==0?"":zj.get(i).getChapter_title()==null?"":zj.get(i).getChapter_title());
				
					//章节id存入map
					Map<String,String> mapKs = new HashMap<String,String>();
					mapKs.put("chapter_id", zj.size()==0?"-1":zj.get(i).getChapter_id()==null?"-1":zj.get(i).getChapter_id());
					//根据章节id，查询课时
					List<CoursesInfo> ks = coursesService.getClass(mapKs);
					//定义课时ListMap
					List<Map<String,Object>> listMapKs = new ArrayList<Map<String,Object>>();
					int cnt = 0;
//					if(ks.size()==0){
//						cnt = -1;
//					}
					for (int j = cnt; j < ks.size(); j++) {
						Map<String,Object> ksMap = new HashMap<String,Object>();
						ksMap.put("class_id", ks.size()==0?"":ks.get(j).getClass_id()==null?"":ks.get(j).getClass_id());
						ksMap.put("class_title", ks.size()==0?"":ks.get(j).getClass_title()==null?"":ks.get(j).getClass_title());
						ksMap.put("class_url", ks.size()==0?"":ks.get(j).getClass_url()==null?"":ks.get(j).getClass_url());
						ksMap.put("class_time", ks.size()==0?"":ks.get(j).getClass_time()==null?"":ks.get(j).getClass_time());
						listMapKs.add(ksMap);
					}
					
				map3.put("chapter",listMapKs);
				listMapZj.add(map3);
			}
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("list", listMapZj);
			map2.put("course_desc", zj.size()==0?"":zj.get(0).getContent()==null?"":zj.get(0).getContent());
			
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
					logMap.put("url", TransUtil.LOG_URL+"courseDetailsCus.action");  //请求命令Url
					logMap.put("u_id", map.get("user_id"));  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "1");  //1:个人2：企业
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
			log.info("---courseDetails--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}

	public ICoursesService getCoursesService() {
		return coursesService;
	}
	public void setCoursesService(ICoursesService coursesService) {
		this.coursesService = coursesService;
	}
	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}
	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}
}
