package com.tuzhi.app.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppUserInfo;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.util.StringUtil;

public class UserInfoAction extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	//日志
	private final static Logger log = LoggerFactory.getLogger(UserInfoAction.class);
	
	private IAppUserInfoService appUserInfoService;
	
	/**
	 * 注册
	 */
	public void register(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			//密码加密
			String pwd = map.get("password");
			map.put("password", StringUtil.MD5pwd(pwd));
			String token =String.valueOf(System.currentTimeMillis());
			map.put("token", StringUtil.MD5pwd(token));
			map.put("create_time", StringUtil.getDisplayYMDHMS());
			
			String status = "0";
			String retMsg = "成功";
		
			//企业(y),个人(n)
			if("n".equals(map.get("is_business"))){
				//查询
				AppUserInfo userInfo = appUserInfoService.getAppUser(map);
				if(userInfo != null){
					status = "01";
					retMsg = "该个人用户已经注册";
				}else{
					//注册app用户信息
					int resultStatus = appUserInfoService.addAppUser(map);
					if(resultStatus<=0){
						status = "03";
						retMsg = "注册失败";
					}
				}
				
			}else{
				//查询企业信息
				AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
				if(enterInfo != null){
					status = "02";
					retMsg = "该企业用户已经注册";
				}else{
					//注册企业用户
					int resultStatus = appUserInfoService.addEnterprises(map);
					if(resultStatus<=0){
						status = "03";
						retMsg = "注册失败";
					}
				}
				
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
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/register.action");  //请求命令Url
//			logMap.put("u_id", "");  //编号(type=1指用户id、type=2指企业id) 
//			logMap.put("type", map.get("is_business"));  //1:个人2：企业
//			logMap.put("version", map.get("version"));  //APP版本
////			logMap.put("record_time", StringUtil.getDisplayYMDHMS());  //记录时间 (mapper文件默认当前时间)
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
	 * 登陆
	 */
	public void login(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			//密码加密
			String pwd = map.get("password");
			map.put("password", StringUtil.MD5pwd(pwd));
			
			Map<String,Object> map3 = new HashMap<String,Object>();
			Map<String,Object> map2 = new HashMap<String,Object>();
			Map<String,Object> map1 = new HashMap<String,Object>();
			String status = "0";
			String retMsg = "成功";
			
			//企业(y),个人(n)
			if("n".equals(map.get("is_business"))){
				//查询人员信息
				AppUserInfo userInfo = appUserInfoService.getAppUser(map);
				if(userInfo == null){
					userInfo = new AppUserInfo();
					status = "04";
					retMsg = "个人用户不存在或输入错误";
				}else{
					if(!map.get("password").equals(userInfo.getPassword())){
						userInfo = new AppUserInfo();
						status = "05"; //密码输入错误
						retMsg = "密码输入错误";
					}
				}
				
				map3.put("user_id", userInfo.getId());
				map3.put("user_name", userInfo.getName());
				map3.put("phone", userInfo.getMobile_phone());
				map3.put("logo_url", userInfo.getLocal_url());
				
				map2.put("user_Info", map3);
				
				map1.put("status", status);
				map1.put("retMsg", retMsg);
				map1.put("token", userInfo.getToken());
				map1.put("data", map2);
				
			}else{
				//查询企业信息
				AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
				if(enterInfo == null){
					enterInfo = new AppEnterprisesInfo();
					status = "06";
					retMsg = "企业用户不存在或输入错误";
				}else{
					if(!map.get("password").equals(enterInfo.getPassword())){
						enterInfo = new AppEnterprisesInfo();
						status = "05"; //密码输入错误
						retMsg = "密码输入错误";
					}
				}
				
				map3.put("user_id", enterInfo.getId());
				map3.put("user_name", enterInfo.getName());
				map3.put("phone", enterInfo.getMobile_phone());
				map3.put("logo_url", enterInfo.getEnterprise_url());
				
				map2.put("user_Info", map3);
				
				map1.put("status", status);
				map1.put("retMsg", retMsg);
				map1.put("token", enterInfo.getToken());
				map1.put("data", map2);
			}
			
			String json = JSON.encode(map1);
		
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/login.action");  //请求命令Url
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
			log.info("---login--Exception:"+e.getMessage());
		}
	}
	
	
	/**
	 * 完善个人用户信息
	 */
	public void updatePersonageInfo(){
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
			
			//查询用户是否存在
			AppUserInfo userInfo = appUserInfoService.getAppUser(map);
			if(userInfo == null){
				status = "09";
				retMsg = "改用户已不存在";
			}else{
				
				//新增个人用户 擅长领域关联表
				//@SuppressWarnings("unused")
				//int s = appUserInfoService.addAppUserField(map);
				
				//新增个人用户用户证书关联表
				//@SuppressWarnings("unused")
				//int s = appUserInfoService.addAppUserField(map);
				
				//完善个人用户信息
				int resultStstus = appUserInfoService.updateAppUser(map);
				if(resultStstus<=0){
					status = "07";
					retMsg = "完善个人信息失败";
				}
			}
			
				
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
				
			String json = JSON.encode(map1);
		
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/updatePersonageInfo.action");  //请求命令Url
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
			log.info("---updateUserInfo--Exception:"+e.getMessage());
		}
	}
	
	/**
	 *完善企业信息
	 */
	public void updateEnterpriseInfo(){
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
			
			//查询企业信息
			AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
			if(enterInfo == null){
				status = "09";
				retMsg = "改用户已不存在";
			}else{
				//完善企业信息
				int resultStatus = appUserInfoService.updateEnterprises(map);
				if(resultStatus<=0){
					status = "08";
					retMsg = "完善企业信息失败";
				}
			}

			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
		
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/updateEnterpriseInfo.action");  //请求命令Url
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
			log.info("---updateEnterpriseInfo--Exception:"+e.getMessage());
		}
	}

	/**
	 * 修改密码
	 */
	public void updatePassword(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest(); 
			HttpServletResponse response = ServletActionContext.getResponse();
			//读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			//定义Map
			Map<String,String> map = new HashMap<String,String>();
			
			//处理JSON字符串
			StringUtil.getJsonStr(br,map);
			
			//获取旧密码
			String old_pwd = StringUtil.MD5pwd(map.get("old_pwd"));
			//新密码
			map.put("password", StringUtil.MD5pwd(map.get("new_pwd")));
	
			Map<String,Object> map1 = new HashMap<String,Object>();
			String status = "0";
			String retMsg = "成功";
			
			//企业(y),个人(n)
			if("n".equals(map.get("is_business"))){
				//查询人员信息
				AppUserInfo userInfo = appUserInfoService.getAppUser(map);
				if(userInfo == null){
					status = "09";
					retMsg = "改用户已不存在";
				}else{
					if(!old_pwd.equals(userInfo.getPassword())){
						status = "10";
						retMsg = "原始密码输入错误";
					}else{
						int resultStatus = appUserInfoService.updateAppUser(map);
						if(resultStatus<=0){
							status = "11";
							retMsg = "密码修改失败";
						}
					}
				}
				
				
			}else{
				//查询企业信息
				AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
				if(enterInfo == null){
					status = "09";
					retMsg = "改用户已不存在";
				}else{
					if(!old_pwd.equals(enterInfo.getPassword())){
						status = "10";
						retMsg = "原始密码输入错误";
					}else{
						int resultStatus = appUserInfoService.updateEnterprises(map);
						if(resultStatus<=0){
							status = "11";
							retMsg = "密码修改失败";
						}
					}
				}
	
			}
			
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
		
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/updatePassword.action");  //请求命令Url
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
			log.info("---updatePassword--Exception:"+e.getMessage());
		}
	}

	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}

	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtil.MD5pwd("Asdf1234"));
//		39bb37cf36d3b29a9280d8a70a0eed42
//		39bb37cf36d3b29a9280d8a70a0eed42
		
//		e10adc3949ba59abbe56e057f20f883e
//		e10adc3949ba59abbe56e057f20f883e
	}
	
}
