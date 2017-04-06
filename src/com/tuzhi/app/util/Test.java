package com.tuzhi.app.util;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

public class Test {

	public static void main(String[] args) {
//		//注册
//		String url = "http://localhost:8080/TZAppConnector/manager/registerUser.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("phone", "17321048020");
//		map.put("verification_code", "464646");
//		map.put("password", "Asdf1234");
//		map.put("is_business", "n"); //企业(y),个人(n)
		
//		//登录
//		String url = "http://localhost:8080/TZAppConnector/manager/loginUser.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("phone", "17321048020");
//		map.put("password", "Asdf1234");
//		map.put("is_business", "n"); //企业(y),个人(n)
		
//		//完善信息(个人)
//		String url = "http://localhost:8080/TZAppConnector/manager/updateUserInfoUser.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("user_id", "");
//		map.put("phone", "17321048020");
//		map.put("user_name", ""); 
//		map.put("card", "456852163001254978"); 
//		map.put("sex", "1"); 
//		map.put("graduation_time", "2017-04-03"); 
//		map.put("order_address", "上海市"); 
//		map.put("good_field", "01"); 
//		map.put("qualification_certificate", "01"); 
//		map.put("card_img", "46464646"); 
//		map.put("token", "65464165461"); 
		
//		//完善信息(企业)
//		String url = "http://localhost:8080/TZAppConnector/manager/updateEnterpriseInfoUser.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("e_id", "");
//		map.put("e_name", "");
//		map.put("business_code", ""); 
//		map.put("business_img", ""); 
//		map.put("token", "1"); 
		
//		//修改密码
//		String url = "http://localhost:8080/TZAppConnector/manager/updatePasswordUser.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("user_id", "");
//		map.put("token", "");
//		map.put("old_pwd", ""); 
//		map.put("new_pwd", ""); 
//		map.put("is_business", "n"); 
		
//		//意见反馈
//		String url = "http://localhost:8080/TZAppConnector/manager/feedbackMsg.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("user_id", "");
//		map.put("is_business", "n");
//		map.put("token", "");
//		map.put("phone", ""); 
//		map.put("email", ""); 
//		map.put("opinion_content", "");
		
//		//系统消息列表
//		String url = "http://localhost:8080/TZAppConnector/manager/messageMsg.action";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("user_id", "");
//		map.put("token", "");
		
		//系统消息详细
		String url = "http://localhost:8080/TZAppConnector/manager/messageDetailMsg.action";
		Map<String,String> map = new HashMap<String,String>();
		map.put("msg_id", "");
		map.put("token", "");
		
		String json = JSON.encode(map);
		System.out.println(json);
	}
	
	
	
	
	

}
