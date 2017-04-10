package com.tuzhi.app.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;
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
			
			//验证参数
			if(StringUtil.isBlank(map.get("phone")) || StringUtil.isBlank(map.get("password")) ||
				StringUtil.isBlank(map.get("is_business"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(!"y".equals(map.get("is_business")) && !"n".equals(map.get("is_business").toString())){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//企业(y),个人(n)
				if("n".equals(map.get("is_business"))){
					//查询
					AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
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
			
			//验证参数
			if(StringUtil.isBlank(map.get("phone")) || StringUtil.isBlank(map.get("password")) ||
				StringUtil.isBlank(map.get("is_business"))){
				status = "15";
				retMsg = "必要参数缺失";
				
				map3.put("user_id", "");
				map3.put("user_name", "");
				map3.put("phone", "");
				map3.put("logo_url", "");
				
				map2.put("user_Info", map3);
				
				map1.put("status", status);
				map1.put("retMsg", retMsg);
				map1.put("token", "");
				map1.put("data", map2);
			}else if(!"y".equals(map.get("is_business")) && !"n".equals(map.get("is_business"))){
				status = "16";
				retMsg = "必要参数输入有误";
				
				map3.put("user_id", "");
				map3.put("user_name", "");
				map3.put("phone", "");
				map3.put("logo_url", "");
				
				map2.put("user_Info", map3);
				
				map1.put("status", status);
				map1.put("retMsg", retMsg);
				map1.put("token", "");
				map1.put("data", map2);
			}else{
				//企业(y),个人(n)
				if("n".equals(map.get("is_business"))){
					//查询人员信息
					AppUserDetailInfo ud = appUserInfoService.getAppUser(map);
					if(ud == null){
						status = "04";
						retMsg = "个人用户不存在或输入错误";
					}else{
						if(!map.get("password").equals(ud.getPassword())){
							ud = new AppUserDetailInfo();
							status = "05"; //密码输入错误
							retMsg = "密码输入错误";
						}
					}
					
					map3.put("user_id", ud==null?"":ud.getId()==0?"":ud.getId());
					map3.put("user_name", ud==null?"":ud.getName()==null?"":ud.getName());
					map3.put("phone", ud==null?"":ud.getMobile_phone()==null?"":ud.getMobile_phone());
					map3.put("logo_url", ud==null?"":ud.getLocal_url()==null?"":ud.getLocal_url());
					
					map2.put("user_Info", map3);
					
					map1.put("status", status);
					map1.put("retMsg", retMsg);
					map1.put("token", ud==null?"":ud.getToken()==null?"":ud.getToken());
					map1.put("data", map2);
					
				}else{
					//查询企业信息
					AppEnterprisesInfo ep = appUserInfoService.getEnterprises(map);
					if(ep == null){
						status = "06";
						retMsg = "企业用户不存在或输入错误";
					}else{
						if(!map.get("password").equals(ep.getPassword())){
							ep = new AppEnterprisesInfo();
							status = "05"; //密码输入错误
							retMsg = "密码输入错误";
						}
					}
					
					map3.put("user_id", ep==null?"":ep.getId()==0?"":ep.getId());
					map3.put("user_name", ep==null?"":ep.getName()==null?"":ep.getName());
					map3.put("phone", ep==null?"":ep.getMobile_phone()==null?"":ep.getMobile_phone());
					map3.put("logo_url", ep==null?"":ep.getEnterprise_url()==null?"":ep.getEnterprise_url());
					
					map2.put("user_Info", map3);
					
					map1.put("status", status);
					map1.put("retMsg", retMsg);
					map1.put("token", ep==null?"":ep.getToken()==null?"":ep.getToken());
					map1.put("data", map2);
				}
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
	public void updateUserInfo(){
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
			AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
			if(userInfo == null){
				status = "09";
				retMsg = "改用户已不存在";
			}else{
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
	 * 获取用户信息
	 */
	public void getUserInfo(){
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
			
			//获取用户领域信息
			List<AppGoodField> fd = new ArrayList<AppGoodField>();
			//获取用户资质证书
			List<AppCertificate> cf = new ArrayList<AppCertificate>();
			//获取用户信息(地址and身份证图片)
			AppUserDetailInfo uf = null;
			
			//验证参数
			if(StringUtil.isBlank(map.get("user_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				uf = appUserInfoService.getAppUser(map);
				if(uf!=null){
					fd = appUserInfoService.getGoodField(map);
					cf = appUserInfoService.getCertificate(map);
				}else{
					status = "09";
					retMsg = "该用户已不存在";
				}
			}
			
			//用户地址信息
			Map<String,Object> adrListMap = new HashMap<String,Object>();
			Map<String,Object> adrMap = new HashMap<String,Object>();
			adrMap.put("id", uf==null?"":uf.getAddressid()==0?"":uf.getAddressid());
			adrMap.put("pro_id", uf==null?"":uf.getPro_id()==null?"":uf.getPro_id());
			adrMap.put("pro_name", uf==null?"":uf.getPro_name()==null?"":uf.getPro_name());
			adrMap.put("city_id", uf==null?"":uf.getCard_id()==0?"":uf.getCard_id());
			adrMap.put("city_name", uf==null?"":uf.getCity_name()==null?"":uf.getCity_name());
			adrMap.put("dis_id", uf==null?"":uf.getDis_id()==null?"":uf.getDis_id());
			adrMap.put("dis_name", uf==null?"":uf.getDis_name()==null?"":uf.getDis_name());
			adrMap.put("street_id", uf==null?"":uf.getStreet_id()==null?"":uf.getStreet_id());
			adrMap.put("street_name", uf==null?"":uf.getStreet_name()==null?"":uf.getStreet_name());
			adrMap.put("details", uf==null?"":uf.getDetails()==null?"":uf.getDetails());
			adrListMap.put("list", adrMap);
			
			//用户身份信息
			Map<String,Object> crdMap = new HashMap<String,Object>();
			crdMap.put("id", uf==null?"":uf.getCardid()==0?"":uf.getCardid());
			crdMap.put("number", uf==null?"":uf.getNumber()==null?"":uf.getNumber());
			crdMap.put("upper_url", uf==null?"":uf.getUpper_url()==null?"":uf.getUpper_url());
			crdMap.put("below_url", uf==null?"":uf.getBelow_url()==null?"":uf.getBelow_url());
			
			//领域信息
			List<Map<String,Object>> fdListMap = new ArrayList<Map<String,Object>>();
			int num1 = 0;
			if(fd.size()==0){
				num1 = -1;
			}
			for (int i = num1; i < fd.size(); i++) {
				Map<String,Object> fdMap = new HashMap<String,Object>();
				fdMap.put("id", fd.size()==0?"":fd.get(i).getId()==0?"":fd.get(i).getId());
				fdMap.put("name", fd.size()==0?"":fd.get(i).getName()==0?"":fd.get(i).getName());
				fdMap.put("url", fd.size()==0?"":fd.get(i).getUrl()==null?"":fd.get(i).getUrl());
				fdMap.put("desc", fd.size()==0?"":fd.get(i).getDesc()==null?"":fd.get(i).getDesc());
				
				Map<String,Object> fdMap1 = new HashMap<String,Object>();
				fdMap1.put("list", fdMap);
				fdListMap.add(fdMap1);
			}
			
			//资质证书
			List<Map<String,Object>> cfListMap = new ArrayList<Map<String,Object>>();
			int num2 = 0;
			if(fd.size()==0){
				num2 = -1;
			}
			for (int i = num2; i < cf.size(); i++) {
				Map<String,Object> cfMap = new HashMap<String,Object>();
				cfMap.put("id", cf.size()==0?"":cf.get(i).getId()==0?"":cf.get(i).getId());
				cfMap.put("certificate_name", cf.size()==0?"":cf.get(i).getCertificate_name()==null?"":cf.get(i).getCertificate_name());
				cfMap.put("certificate_url", cf.size()==0?"":cf.get(i).getCertificate_url()==null?"":cf.get(i).getCertificate_url());
				
				Map<String,Object> cfMap1 = new HashMap<String,Object>();
				cfMap1.put("list", cfMap);
				cfListMap.add(cfMap1);
			}
			
			//用户基本信息
			Map<String,Object> ufdMap = new HashMap<String,Object>();
			ufdMap.put("id", uf==null?"":uf.getId()==0?"":uf.getId());
			ufdMap.put("phone", uf==null?"":uf.getMobile_phone()==null?"":uf.getMobile_phone());
			ufdMap.put("name", uf==null?"":uf.getName()==null?"":uf.getName());
			ufdMap.put("nick_name", uf==null?"":uf.getNick_name()==null?"":uf.getNick_name());
			ufdMap.put("sex", uf==null?"":uf.getSex()==0?"":uf.getSex());
			ufdMap.put("logo", uf==null?"":uf.getLocal_url()==null?"":uf.getLocal_url());
			ufdMap.put("money", uf==null?"":uf.getMoney());
			ufdMap.put("integral", uf==null?"":uf.getIntegral());
			ufdMap.put("is_auth", uf==null?"":uf.getIs_auth());
			ufdMap.put("auth_create_time", uf==null?"":uf.getAuth_create_time()==null?"":uf.getAuth_create_time());
			ufdMap.put("create_time", uf==null?"":uf.getCreate_time()==null?"":uf.getCreate_time());
			ufdMap.put("update_time", uf==null?"":uf.getUpdate_time()==null?"":uf.getUpdate_time());
			ufdMap.put("graduation_time", uf==null?"":uf.getGraduation_time()==null?"":uf.getGraduation_time());
			ufdMap.put("order_address", adrListMap);
			ufdMap.put("good_field", fdListMap);
			ufdMap.put("qualification_certificate", cfListMap);
			ufdMap.put("card", crdMap);
			
			Map<String,Object> ufMap = new HashMap<String,Object>();
			ufMap.put("status", status);
			ufMap.put("retMsg", retMsg);
			ufMap.put("user_info", ufdMap);
			
			String json = JSON.encode(ufMap);
		
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(json);
			
//			//添加日志信息
//			Map<String,Object> logMap = new HashMap<String,Object>();
//			logMap.put("url", "xxxxxx/TZAppConnector/manager/getAppUser.action");  //请求命令Url
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
			AppEnterprisesInfo ep = appUserInfoService.getEnterprises(map);
			if(ep == null){
				status = "09";
				retMsg = "该用户已不存在";
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
	 * 获取企业信息
	 */
	public void getEnterpriseInfo(){
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
			AppEnterprisesInfo ep = null;
			
			//验证参数
			if(StringUtil.isBlank(map.get("e_id"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("e_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//查询企业信息
				ep = appUserInfoService.getEnterprises(map);
				if(ep == null){
					status = "09";
					retMsg = "改用户已不存在";
				}
			}
			
			Map<String,Object> epMap = new HashMap<String,Object>();
			epMap.put("status", status);
			epMap.put("retMsg", retMsg);
			epMap.put("id", ep==null?"":ep.getId()==0?"":ep.getId());
			epMap.put("e_name", ep==null?"":ep.getName()==null?"":ep.getName());
			epMap.put("business_code", ep==null?"":ep.getBusiness_code()==null?"":ep.getBusiness_code());
			epMap.put("business_img", ep==null?"":ep.getBusiness_url()==null?"":ep.getBusiness_url());
			epMap.put("logo", ep==null?"":ep.getEnterprise_url()==null?"":ep.getEnterprise_url());
			epMap.put("mobile_phone", ep==null?"":ep.getMobile_phone()==null?"":ep.getMobile_phone());
			epMap.put("telephone", ep==null?"":ep.getTelephone()==null?"":ep.getTelephone());
			epMap.put("create_time", ep==null?"":ep.getCreate_time()==null?"":ep.getCreate_time());
			epMap.put("update_time", ep==null?"":ep.getUpdate_time()==null?"":ep.getUpdate_time());
			epMap.put("legal_person", ep==null?"":ep.getLegal_person()==null?"":ep.getLegal_person());
			epMap.put("desc", ep==null?"":ep.getDesc()==null?"":ep.getDesc());
			epMap.put("money", ep==null?"":ep.getMoney()==0?"":ep.getMoney());
			epMap.put("is_auth", ep==null?"":ep.getIs_auth());
			epMap.put("auth_create_time", ep==null?"":ep.getAuth_create_time()==null?"":ep.getAuth_create_time());
			
			String json = JSON.encode(epMap);
		
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
			
			//获取旧密码
			String old_pwd = StringUtil.MD5pwd(map.get("old_pwd"));
			//新密码
			map.put("password", StringUtil.MD5pwd(map.get("new_pwd")));
	
			Map<String,Object> map1 = new HashMap<String,Object>();
			String status = "0";
			String retMsg = "成功";
			
			//验证参数
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("old_pwd")) ||
				 StringUtil.isBlank(map.get("new_pwd")) || StringUtil.isBlank(map.get("type"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(!"1".equals(map.get("type")) && !"2".equals(map.get("type")) && map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//企业(2),个人(1)
				if("1".equals(map.get("type"))){
					//查询人员信息
					AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
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
			}
			
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
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
			try {
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}

	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Map<String,String> cfMap = new HashMap<String,String>();
		cfMap.put("a", "123456789");
		System.out.println(cfMap.get("a").length());
//		cfMap.put("b", "2");
//		cfMap.put("c", "3");
//		
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("a1", "1");
//		map.put("b2", "2");
//		map.put("c3", "3");
//		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		list.add(cfMap);
//		list.add(map);
		
		for (int i = 0; i < 2; i++) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("a1", "1");
			map.put("b2", "2");
			map.put("c3", "3");
			
			Map<String,Object> fdMap1 = new HashMap<String,Object>();
			fdMap1.put("list", map);
			list.add(fdMap1);
		}
		
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("info", list);
		
		String json = JSON.encode(map1);
//		System.out.println(json);
		
		JSONObject jsonObject  = JSONObject.fromObject(json);
		@SuppressWarnings("rawtypes")
		Iterator ite = jsonObject.keys();
		// 遍历jsonObject数据,添加到Map对象
		while (ite.hasNext()) {
	        String key = ite.next().toString();
	        String value = jsonObject .get(key).toString();
	        cfMap.put(key, value);
	    }
//		System.out.println(cfMap);
		String abcd = cfMap.get("info");
//		System.out.println(abc);
//		JSONObject ob = JSONObject.fromObject(cfMap.get("info"));
//		System.out.println(ob.get("list"));
		JSONArray arr = JSONArray.fromObject(abcd);
//		System.out.println(arr.size());
		for (int i = 0; i < arr.size(); i++) {
			JSONObject j = arr.getJSONObject(i);
			JSONObject js  = JSONObject.fromObject(j);
//			JSONArray a = JSONArray.fromObject(j);
//			System.out.println(js.get("list"));
			String qq = js.get("list").toString();
			System.out.println(qq);
//			JSONObject jsonObject  = JSONObject.fromObject(sb.toString());
			JSONObject jsd  = JSONObject.fromObject(qq.toString());
//			System.out.println(jsd.get("a1"));
			
			Iterator itea = jsd.keys();
			// 遍历jsonObject数据,添加到Map对象
			Map<String,String> map = new HashMap<String,String>();
			while (itea.hasNext()) {
		        String key = itea.next().toString();
		        String value = jsd.get(key).toString();
		        map.put(key, value);
		    }
			System.out.println(map);
		}
//		System.out.println(arr);
	}
	
}
