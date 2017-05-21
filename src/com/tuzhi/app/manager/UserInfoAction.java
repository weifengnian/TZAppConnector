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
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.service.IAppUserInfoService;
import com.tuzhi.app.util.StringUtil;
import com.tuzhi.app.util.TransUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class UserInfoAction extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
	
	//日志
	private final static Logger log = LoggerFactory.getLogger(UserInfoAction.class);
	
	private IAppUserInfoService appUserInfoService;
	
	/**
	 * 注册
	 */
	public void register(){
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
			
			//密码加密
			String pwd = map.get("password");
			map.put("password", StringUtil.MD5pwd(pwd));
			map.put("create_time", StringUtil.getDisplayYMDHMS());
			String status = "0";
			String retMsg = "成功";
			String userid = "";
			
			//验证参数
			if(StringUtil.isBlank(map.get("phone")) || StringUtil.isBlank(map.get("password")) ||
				StringUtil.isBlank(map.get("is_business"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(!"y".equals(map.get("is_business")) && !"n".equals(map.get("is_business"))){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				/*//查询用户
				AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
				//查询企业信息
				AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
				
				if(userInfo !=null || enterInfo !=null){
					status = "01";
					retMsg = "该用户已经注册";
				}else{
					//企业(y),个人(n)
					if("n".equals(map.get("is_business"))){
						//注册个人用户信息
						map.put("token", StringUtil.MD5pwd(String.valueOf(System.currentTimeMillis())).toUpperCase());
						int resultStatus = appUserInfoService.addAppUser(map);
						if(resultStatus<=0){
							status = "03";
							retMsg = "注册失败";
						}
					}else{
						//注册企业用户
						map.put("token", StringUtil.MD5pwd(String.valueOf(System.currentTimeMillis())).toUpperCase());
						int resultStatus = appUserInfoService.addEnterprises(map);
						if(resultStatus<=0){
							status = "03";
							retMsg = "注册失败";
						}
					}
				}*/
				
				//企业(y),个人(n)
				if("n".equals(map.get("is_business"))){
					//查询用户
					AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
					if(userInfo != null){
						status = "01";
						retMsg = "该用户已经注册";
						userid = String.valueOf(userInfo.getId());
					}else{
						//注册app用户信息
						map.put("token", StringUtil.MD5pwd(String.valueOf(System.currentTimeMillis())).toUpperCase());
						int resultStatus = appUserInfoService.addAppUser(map);
						if(resultStatus>0){
							userInfo = appUserInfoService.getAppUser(map);
							userid = String.valueOf(userInfo.getId());
						}
						if(resultStatus<=0){
							status = "03";
							retMsg = "注册失败";
						}
					}
					
				}else{
					//查询企业信息
					AppEnterprisesInfo enterInfo = appUserInfoService.getEnterprises(map);
					if(enterInfo != null){
						status = "01";
						retMsg = "该用户已经注册";
						userid = String.valueOf(enterInfo.getId());
					}else{
						//注册企业用户
						map.put("token", StringUtil.MD5pwd(String.valueOf(System.currentTimeMillis())).toUpperCase());
						int resultStatus = appUserInfoService.addEnterprises(map);
						if(resultStatus>0){
							enterInfo = appUserInfoService.getEnterprises(map);
							userid = String.valueOf(enterInfo.getId());
						}
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
			resultMap.put("userid", userid);
			
			String json = JSON.encode(resultMap);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"registerUr.action");  //请求命令Url
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
			log.info("---register--Exception:"+e.getMessage());
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
	 * 登陆
	 */
	public void login(){
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
			
			//密码加密
			String pwd = map.get("password");
			map.put("password", StringUtil.MD5pwd(pwd));
			
			Map<String,Object> map3 = new HashMap<String,Object>();
			Map<String,Object> map2 = new HashMap<String,Object>();
			Map<String,Object> map1 = new HashMap<String,Object>();
			String status = "0";
			String retMsg = "成功";
			
			//验证参数
			if(StringUtil.isBlank(map.get("phone")) || StringUtil.isBlank(map.get("password"))){
				status = "15";
				retMsg = "必要参数缺失";
				
				map3.put("user_id", "");
				map3.put("user_name", "");
				map3.put("nick_name", "");
				map3.put("phone", "");
				map3.put("logo_url", "");
				
				map2.put("user_Info", map3);
				
				map1.put("status", status);
				map1.put("retMsg", retMsg);
				map1.put("token", "");
				map1.put("data", map2);
			}else{
				//查询人员信息
				AppUserDetailInfo ud = appUserInfoService.getAppUser(map);
				if(ud != null){
					if(!map.get("password").equals(ud.getPassword())){
						ud = new AppUserDetailInfo();
						status = "05"; //密码输入错误
						retMsg = "密码输入错误";
					}
					map3.put("user_id", ud.getId()==0?"":ud.getId());
					map3.put("user_name", ud.getName()==null?"":ud.getName());
					map3.put("nick_name", ud.getNick_name()==null?"":ud.getNick_name());
					map3.put("phone", ud.getMobile_phone()==null?"":ud.getMobile_phone());
					map3.put("logo_url", ud.getIcon_url()==null?"":ud.getIcon_url());
					map3.put("type", "1");
					
					map2.put("user_Info", map3);
					
					map1.put("status", status);
					map1.put("retMsg", retMsg);
					map1.put("token", ud.getToken()==null?"":ud.getToken());
					map1.put("data", map2);
				}else{
					//查询企业信息
					AppEnterprisesInfo ep = appUserInfoService.getEnterprises(map);
					if(ep!=null){
						if(!map.get("password").equals(ep.getPassword())){
							ep = new AppEnterprisesInfo();
							status = "05"; //密码输入错误
							retMsg = "密码输入错误";
						}
					}else{
						status = "09"; //密码输入错误
						retMsg = "该用户不存在";
					}
					map3.put("user_id", ep==null?"":ep.getId()==0?"":ep.getId());
					map3.put("user_name", ep==null?"":ep.getName()==null?"":ep.getName());
					map3.put("nick_name", ep==null?"":ep.getLogin_name()==null?"":ep.getLogin_name());
					map3.put("phone", ep==null?"":ep.getMobile_phone()==null?"":ep.getMobile_phone());
					map3.put("logo_url", ep==null?"":ep.getEnterprise_url()==null?"":ep.getEnterprise_url());
					map3.put("type", ep==null?"":"2");
					
					map2.put("user_Info", map3);
					
					map1.put("status", status);
					map1.put("retMsg", retMsg);
					map1.put("token", ep==null?"":ep.getToken()==null?"":ep.getToken());
					map1.put("data", map2);
				}
			}
			/*else if(!"y".equals(map.get("is_business")) && !"n".equals(map.get("is_business"))){
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
			}*/
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
		
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"loginUr.action");  //请求命令Url
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
			log.info("---login--Exception:"+e.getMessage());
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
	 * 完善个人用户信息
	 */
	public void updateUserInfo(){
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
			
			//查询用户是否存在
			AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
			if(userInfo == null){
				status = "09";
				retMsg = "该用户已不存在";
			}else{
				//完善个人用户信息
				map.put("status", "10");  //用户状态（-1:黑名单1:正常,10待认证）
				int resultStstu = appUserInfoService.updateAppUser(map);
				if(resultStstu<=0){
					status = "07";
					retMsg = "完善个人信息失败";
				}
			}
				
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
				
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
		
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"updateUserInfoUr.action");  //请求命令Url
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
			log.info("---updateUserInfo--Exception:"+e.getMessage());
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
	 * 获取用户信息
	 */
	public void getUserInfo(){
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
			
			//获取用户领域信息
			List<AppGoodField> fd = new ArrayList<AppGoodField>();
			//获取用户资质证书
			List<AppCertificate> cf = new ArrayList<AppCertificate>();
			//获取用户接单地址
			List<AppAddress> ad = new ArrayList<AppAddress>();
			//获取用户信息(地址and身份证图片)
			AppUserDetailInfo uf = null;
			
			//验证参数
			if(StringUtil.isBlank(map.get("user_id")) || StringUtil.isBlank(map.get("token"))){
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
					ad = appUserInfoService.getAddressList(map);
				}else{
					status = "09";
					retMsg = "该用户已不存在";
				}
			}
			
			//用户接单地址信息
			List<Map<String,Object>> adrListMap = new ArrayList<Map<String,Object>>();
			int num0 = 0;
//			if(ad.size()==0){
//				num0 = -1;
//			}
			for (int i = num0; i < ad.size(); i++) {
				Map<String,Object> adrMap = new HashMap<String,Object>();
				adrMap.put("id", ad.size()==0?"":ad.get(i).getId()==0?"":ad.get(i).getId());
				adrMap.put("pro_id", ad.size()==0?"":ad.get(i).getPro_id()==null?"":ad.get(i).getPro_id());
				adrMap.put("pro_name", ad.size()==0?"":ad.get(i).getPro_name()==null?"":ad.get(i).getPro_name());
				adrMap.put("city_id", ad.size()==0?"":ad.get(i).getCity_id()==null?"":ad.get(i).getCity_id());
				adrMap.put("city_name", ad.size()==0?"":ad.get(i).getCity_name()==null?"":ad.get(i).getCity_name());
				adrMap.put("dis_id", ad.size()==0?"":ad.get(i).getDis_id()==null?"":ad.get(i).getDis_id());
				adrMap.put("dis_name", ad.size()==0?"":ad.get(i).getDis_name()==null?"":ad.get(i).getDis_name());
				adrMap.put("street_id", ad.size()==0?"":ad.get(i).getStreet_id()==null?"":ad.get(i).getStreet_id());
				adrMap.put("street_name", ad.size()==0?"":ad.get(i).getStreet_name()==null?"":ad.get(i).getStreet_name());
				adrMap.put("details", ad.size()==0?"":ad.get(i).getDetails()==null?"":ad.get(i).getDetails());
				adrListMap.add(adrMap);
			}
			Map<String,Object> adMap1 = new HashMap<String,Object>();
			adMap1.put("list", adrListMap);
			
			//用户身份信息
			Map<String,Object> crdMap = new HashMap<String,Object>();
			crdMap.put("id", uf==null?"":uf.getCardid()==0?"":uf.getCardid());
			crdMap.put("number", uf==null?"":uf.getNumber()==null?"":uf.getNumber());
			crdMap.put("upper_url", uf==null?"":uf.getUpper_url()==null?"":uf.getUpper_url());
			crdMap.put("up_local_url", uf==null?"":uf.getLocal_upper_url()==null?"":uf.getLocal_upper_url());
			crdMap.put("below_url", uf==null?"":uf.getBelow_url()==null?"":uf.getBelow_url());
			crdMap.put("down_local_url", uf==null?"":uf.getLocal_below_url()==null?"":uf.getLocal_below_url());
			
			//领域信息
			List<Map<String,Object>> fdListMap = new ArrayList<Map<String,Object>>();
			int num1 = 0;
//			if(fd.size()==0){
//				num1 = -1;
//			}
			for (int i = num1; i < fd.size(); i++) {
				Map<String,Object> fdMap = new HashMap<String,Object>();
				fdMap.put("id", fd.size()==0?"":fd.get(i).getId()==0?"":fd.get(i).getId());
				fdMap.put("name", fd.size()==0?"":fd.get(i).getName()==0?"":fd.get(i).getName());
				fdMap.put("url", fd.size()==0?"":fd.get(i).getUrl()==null?"":fd.get(i).getUrl());
				fdMap.put("desc", fd.size()==0?"":fd.get(i).getDesc()==null?"":fd.get(i).getDesc());
				fdListMap.add(fdMap);
			}
			Map<String,Object> fdMap1 = new HashMap<String,Object>();
			fdMap1.put("list", fdListMap);
			
			//资质证书
			List<Map<String,Object>> cfListMap = new ArrayList<Map<String,Object>>();
			int num2 = 0;
//			if(fd.size()==0){
//				num2 = -1;
//			}
			for (int i = num2; i < cf.size(); i++) {
				Map<String,Object> cfMap = new HashMap<String,Object>();
				cfMap.put("id", cf.size()==0?"":cf.get(i).getId()==0?"":cf.get(i).getId());
				cfMap.put("certificate_name", cf.size()==0?"":cf.get(i).getCertificate_name()==null?"":cf.get(i).getCertificate_name());
				cfMap.put("certificate_url", cf.size()==0?"":cf.get(i).getCertificate_url()==null?"":cf.get(i).getCertificate_url());
				cfMap.put("certificate_local_url", cf.size()==0?"":cf.get(i).getLocal_url()==null?"":cf.get(i).getLocal_url());
				cfMap.put("is_auth", cf.size()==0?"":cf.get(i).getIs_auth()==null?"":cf.get(i).getIs_auth());
				cfListMap.add(cfMap);
			}
			Map<String,Object> cfMap1 = new HashMap<String,Object>();
			cfMap1.put("list", cfListMap);
			
			//用户基本信息
			Map<String,Object> ufdMap = new HashMap<String,Object>();
			ufdMap.put("id", uf==null?"":uf.getId()==0?"":uf.getId());
			ufdMap.put("phone", uf==null?"":uf.getMobile_phone()==null?"":uf.getMobile_phone());
			ufdMap.put("name", uf==null?"":uf.getName()==null?"":uf.getName());
			ufdMap.put("nick_name", uf==null?"":uf.getNick_name()==null?"":uf.getNick_name());
			ufdMap.put("sex", uf==null?"":uf.getSex()==0?"":uf.getSex());
			ufdMap.put("logo", uf==null?"":uf.getIcon_url()==null?"":uf.getIcon_url());
			ufdMap.put("money", uf==null?"":uf.getMoney());
			ufdMap.put("integral", uf==null?"":uf.getIntegral());
			ufdMap.put("is_auth", uf==null?"":uf.getIs_auth());
			ufdMap.put("auth_create_time", uf==null?"":uf.getAuth_create_time()==null?"":uf.getAuth_create_time());
			ufdMap.put("create_time", uf==null?"":uf.getCreate_time()==null?"":uf.getCreate_time());
			ufdMap.put("update_time", uf==null?"":uf.getUpdate_time()==null?"":uf.getUpdate_time());
			ufdMap.put("graduation_time", uf==null?"":uf.getGraduation_time()==null?"":uf.getGraduation_time());
			ufdMap.put("order_address", adMap1);
			ufdMap.put("good_field", fdMap1);
			ufdMap.put("qualification_certificate", cfMap1);
			ufdMap.put("card", crdMap);
			
			Map<String,Object> ufMap = new HashMap<String,Object>();
			ufMap.put("status", status);
			ufMap.put("retMsg", retMsg);
			ufMap.put("user_info", ufdMap);
			
			String json = JSON.encode(ufMap);
			log.info("----response--json:"+json);
		
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"getUserInfoUr.action");  //请求命令Url
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
			log.info("------getUserInfo---Exception:"+e.getMessage());
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
	 * 完善企业信息
	 */
	public void updateEnterpriseInfo(){
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
			if(StringUtil.isBlank(map.get("e_id")) || StringUtil.isBlank(map.get("token"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else if(map.get("e_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
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
			}

			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
		
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"updateEnterpriseInfoUr.action");  //请求命令Url
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
			log.info("---updateEnterpriseInfo--Exception:"+e.getMessage());
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
	 * 获取企业信息
	 */
	public void getEnterpriseInfo(){
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
			
			//查询企业信息
			AppEnterprisesInfo ep = null;
			
			//验证参数
			if(StringUtil.isBlank(map.get("e_id")) || StringUtil.isBlank(map.get("token"))){
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
					retMsg = "该用户已不存在";
				}
			}
			
			Map<String,Object> epMap = new HashMap<String,Object>();
			epMap.put("status", status);
			epMap.put("retMsg", retMsg);
			epMap.put("id", ep==null?"":ep.getId()==0?"":ep.getId());
			epMap.put("e_name", ep==null?"":ep.getName()==null?"":ep.getName());
			epMap.put("business_code", ep==null?"":ep.getBusiness_code()==null?"":ep.getBusiness_code());
			epMap.put("business_img", ep==null?"":ep.getBusiness_url()==null?"":ep.getBusiness_url());
			epMap.put("business_local_url", ep==null?"":ep.getLocal_business_url()==null?"":ep.getLocal_business_url());
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"getEnterpriseInfoUr.action");  //请求命令Url
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
			log.info("---getEnterpriseInfo--Exception:"+e.getMessage());
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
			log.info("----request--map:"+map);
			
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
			}else if((!"1".equals(map.get("type")) && !"2".equals(map.get("type"))) || map.get("user_id").length()>10){
				status = "16";
				retMsg = "必要参数输入有误";
			}else{
				//企业(2),个人(1)
				if("1".equals(map.get("type"))){
					//查询人员信息
					AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
					if(userInfo == null){
						status = "09";
						retMsg = "该用户已不存在";
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
						retMsg = "该用户已不存在";
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
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"updatePasswordUr.action");  //请求命令Url
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
			log.info("---updatePassword--Exception:"+e.getMessage());
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
	 * 找回密码
	 */
	public void setPwd(){
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
			
			//新密码
			map.put("password", StringUtil.MD5pwd(map.get("password")));
	
			Map<String,Object> map1 = new HashMap<String,Object>();
			String status = "0";
			String retMsg = "成功";
			
			//验证参数
			if(StringUtil.isBlank(map.get("phone")) || StringUtil.isBlank(map.get("password"))){
				status = "15";
				retMsg = "必要参数缺失";
			}else{
				//企业信息
				AppEnterprisesInfo enterInfo = null;
				//查询人员信息
				AppUserDetailInfo userInfo = appUserInfoService.getAppUser(map);
				if(userInfo != null){
					int resultStatus = appUserInfoService.updateAppUser(map);
					if(resultStatus<=0){
						status = "28";
						retMsg = "密码重置失败";
					}
				}else{
					//查询企业信息
					enterInfo = appUserInfoService.getEnterprises(map);
					if(enterInfo != null){
						int resultStatus = appUserInfoService.updateEnterprises(map);
						if(resultStatus<=0){
							status = "28";
							retMsg = "密码重置失败";
						}
					}
				}
				
				if(userInfo == null && enterInfo == null){
					status = "09";
					retMsg = "该用户已不存在";
				}
			}
			
			map1.put("status", status);
			map1.put("retMsg", retMsg);
			
			String json = JSON.encode(map1);
			log.info("----response--json:"+json);
			
			response.getWriter().write(json);
			
			try {
				if(TransUtil.LOG_FLAG){
					//添加日志信息
					Map<String,Object> logMap = new HashMap<String,Object>();
					logMap.put("url", TransUtil.LOG_URL+"setPwdUr.action");  //请求命令Url
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
			log.info("---updatePassword--Exception:"+e.getMessage());
			try {
				response.getWriter().write("{\"status\":\"29\",\"retMsg\":\"数据异常\"}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.info("---IOException:"+e1.getMessage());
			}
		}
	}

	
	public IAppUserInfoService getAppUserInfoService() {
		return appUserInfoService;
	}
	public void setAppUserInfoService(IAppUserInfoService appUserInfoService) {
		this.appUserInfoService = appUserInfoService;
	}
}
