package com.tuzhi.app.pojo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年5月21日	
 * @Copyright:
 */
public class TaskUser {
	
	private int user_id; //用户编号
	private String user_name; //用户名
	private String url; //图像
	private String token; //标识
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
