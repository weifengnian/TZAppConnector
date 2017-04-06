package com.tuzhi.app.entity;

import java.util.Date;

public class AppLog {
	
	private int id; //	4	
	private String url; //请求命令字
	private int u_id; //编号(type=1指用户id、type=2指企业id)
	private int type; //1:个人2：企业
	private String version; //	APP版本
	private Date record_time; //记录时间
	private int result_code; //状态码
	private String result_msg; //状态码说明
	private String req_content; //请求内容
	private String resp_content; //相应内容
	private String token; //系统唯一标识
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getRecord_time() {
		return record_time;
	}
	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}
	public int getResult_code() {
		return result_code;
	}
	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}
	public String getReq_content() {
		return req_content;
	}
	public void setReq_content(String req_content) {
		this.req_content = req_content;
	}
	public String getResp_content() {
		return resp_content;
	}
	public void setResp_content(String resp_content) {
		this.resp_content = resp_content;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
}
