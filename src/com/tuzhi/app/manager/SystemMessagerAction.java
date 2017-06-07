package com.tuzhi.app.manager;

import java.io.BufferedReader;
import java.io.File;
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

import com.tuzhi.app.entity.AppBanner;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.service.IAppUserInfoService;
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"feedbackMsg.action");  //请求命令Url
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
			log.info("---feedback--Exception:"+e.getMessage());
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
	 * 消息列表
	 */
	public void message(){
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
					status = "0";
					retMsg = "无消息数据";
				}
			}
			
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(lm.size()==0){
//				num = -1;
//			}
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"messageMsg.action");  //请求命令Url
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
			log.info("---message--Exception:"+e.getMessage());
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
	 * 系统消息详细
	 */
	public void messageDetail(){
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
			List<AppMessage> lm = new ArrayList<AppMessage>();
			if(StringUtil.isBlank(map.get("msg_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("msg_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息详情
				lm = systemMessagerService.getMessageDetail(map);
				if(lm.size() <= 0){
					status = "0";
					retMsg = "无消息详情";
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"messageDetailMsg.action");  //请求命令Url
					logMap.put("u_id", map.get("msg_id"));  //编号(type=1指用户id、type=2指企业id) 
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
	
	/**
	 * 获取所有领域
	 */
	public void allGoodField(){
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
			List<AppGoodField> gf = new ArrayList<AppGoodField>();
			if(StringUtil.isBlank(map.get("page")) || StringUtil.isBlank(map.get("rows"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else{
				//领域信息
				gf = systemMessagerService.getAllField(map);
				if(gf.size() <= 0){
					status = "0";
					retMsg = "无领域信息";
				}
			}
				
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(gf.size()==0){
//				num = -1;
//			}
			for (int i = num; i < gf.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("filed_id", gf.size()==0?"":gf.get(i).getId()==0?"":gf.get(i).getId());
				map3.put("name", gf.size()==0?"":gf.get(i).getName()==0?"":gf.get(i).getName());
				map3.put("url", gf.size()==0?"":gf.get(i).getUrl()==null?"":gf.get(i).getUrl());
				map3.put("desc", gf.size()==0?"":gf.get(i).getDesc()==null?"":gf.get(i).getDesc());
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
					logMap.put("url", TransUtil.LOG_URL+"allGoodFieldMsg.action");  //请求命令Url
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
			log.info("---allGoodField--Exception:"+e.getMessage());
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
	 * 获取广告图片
	 */
	public void myBanner(){
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
			
			List<AppBanner> ab = new ArrayList<AppBanner>();
			//验证参数 
			if(StringUtil.isBlank(map.get("type"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(!"1".equals(map.get("type")) && !"2".equals(map.get("type"))){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//消息列表
				ab = systemMessagerService.getMyBanner(map);
				if(ab.size() <= 0){
					status = "0";
					retMsg = "无广告图片";
				}
			}
				
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			int num = 0;
//			if(ab.size()==0){
//				num = -1;
//			}
			for (int i = num; i < ab.size(); i++) {
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("banner_id", ab.size()==0?"":ab.get(i).getId()==0?"":ab.get(i).getId());
				map3.put("img_url", ab.size()==0?"":ab.get(i).getUrl()==null?"":ab.get(i).getUrl());
				map3.put("link_url", ab.size()==0?"":ab.get(i).getLink_url()==null?"":ab.get(i).getLink_url());
				map3.put("title", ab.size()==0?"":ab.get(i).getTitle()==null?"":ab.get(i).getTitle());
				map3.put("content", ab.size()==0?"":ab.get(i).getContent()==null?"":ab.get(i).getContent());
				map3.put("from", ab.size()==0?"":ab.get(i).getFrom()==null?"":ab.get(i).getFrom());
				map3.put("type", ab.size()==0?"":ab.get(i).getType()==0?"":ab.get(i).getType());
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
					logMap.put("url", TransUtil.LOG_URL+"myBannerMsg.action");  //请求命令Url
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
			log.info("---myBanner--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}
	
	
	private String fileName; //文件名称
	private String version; //版本号
	private String template; //功能模板，1、用户，2、企业，3、身份证，4资质证书,5营业执照
	private File img; //文件流(图片)
	/**
	 * 上传图片
	 */
	public void upload(){
		//HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			
			log.info("----fileName:"+fileName+"----template:"+template+",----file:"+img+",----template:"+template);
			
			String status = "0";
			String retMsg = "成功";
			
//			img = new File("C:\\abc.png");
			
			//判断功能模板，1、用户，2、企业，3、身份证，4资质证书
			if(!"1".equals(template) && !"2".equals(template) && !"3".equals(template) && !"4".equals(template) && !"5".equals(template)){
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("status", "17");
				resultMap.put("retMsg", "图片类型输入有误");
				resultMap.put("url", "");
				String json = JSON.encode(resultMap);
				response.getWriter().write(json);
				return;
			}
			
			//判断文件流
			if(img == null){
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("status", "18");
				resultMap.put("retMsg", "无法获取图片流");
				resultMap.put("url", "");
				String json = JSON.encode(resultMap);
				response.getWriter().write(json);
				return;
			}
			
			//获取日期yyyy-mm-dd
			String date = StringUtil.getDisplayYMD();
			StringBuffer upload_path = new StringBuffer();
			StringBuffer load_path = new StringBuffer();
			
			//用户
			if("1".equals(template)){ 
				upload_path.append("\\picture\\1\\"+date+"\\");
				load_path.append("/picture/1/"+date+"/");
			
			//企业
			}else if("2".equals(template)){ 
				upload_path.append("\\picture\\2\\"+date+"\\");
				load_path.append("/picture/2/"+date+"/");
			
			//身份证
			}else if("3".equals(template)){
				upload_path.append("\\picture\\3\\"+date+"\\");
				load_path.append("/picture/3/"+date+"/");
			
			//资质证书
			}else if("4".equals(template)){
				upload_path.append("\\picture\\4\\"+date+"\\");
				load_path.append("/picture/4/"+date+"/");
				
			//5营业执照	
			}else{
				upload_path.append("\\picture\\5\\"+date+"\\");
				load_path.append("/picture/5/"+date+"/");
			}
			
			//http访问目标文件
			String targetDirectory = TransUtil.UPLOAD_PATH+upload_path.toString();
			//本地目标文件
			String local_Directory = "C:"+upload_path.toString();
			
			//建立http可访问文件夹
			StringUtil.createDirectory(targetDirectory);
			//创建本地文件夹
			StringUtil.createDirectory(local_Directory);
			
			//生成图片名称
			String directory = String.valueOf(System.currentTimeMillis());
			
			//创建http可访问文件目录
			File filepath = new File(targetDirectory+"\\"+directory+".jpg");
			//创建本地文件目录
			File local_filepath = new File(local_Directory+"\\"+directory+".jpg");
			
			//拷贝文件到http可访问目录
			StringUtil.copy(img, filepath);
			//拷贝文件到本地目录
			StringUtil.copy(img, local_filepath);
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", status);
			resultMap.put("retMsg", retMsg);
			resultMap.put("url", TransUtil.LOAD_PATH+load_path.toString()+directory+".jpg");
			resultMap.put("local_url", local_Directory+directory+".jpg");
			
			log.info("----upload_path:"+filepath);
			log.info("----load_path:"+TransUtil.LOAD_PATH+load_path.toString()+directory+".jpg");
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"uploadMsg.action");  //请求命令Url
					logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
					logMap.put("type", "");  //1:个人2：企业
					logMap.put("version", version);  //APP版本
					logMap.put("req_content", fileName+","+version+","+template+","+img); //请求内容
					logMap.put("resp_content", json.length()>8000?json.substring(0, 8000):json); //相应内容
					logMap.put("token", ""); //系统唯一标识
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
			log.info("---upload--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	public File getImg() {
		return img;
	}

	public void setImg(File img) {
		this.img = img;
	}
}
