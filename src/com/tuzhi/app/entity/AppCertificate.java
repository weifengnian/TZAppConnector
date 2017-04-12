package com.tuzhi.app.entity;


public class AppCertificate {
	
	private int id; //
	private String certificate_name; //证书名称
	private String certificate_url; //证书url 
	private String local_url; //本地地址
	private String level; //证书等级
	private int is_auth; //是否审核	0:未提交	1:已审核通过   10:审核中	-1:审核未通过
	private String create_time; //创建时间
	private String auth_time; //审核时间
	private String only_id; //唯一标识，用来获取id
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCertificate_name() {
		return certificate_name;
	}
	public void setCertificate_name(String certificate_name) {
		this.certificate_name = certificate_name;
	}
	public String getCertificate_url() {
		return certificate_url;
	}
	public void setCertificate_url(String certificate_url) {
		this.certificate_url = certificate_url;
	}
	public String getLocal_url() {
		return local_url;
	}
	public void setLocal_url(String local_url) {
		this.local_url = local_url;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(String auth_time) {
		this.auth_time = auth_time;
	}
	public String getOnly_id() {
		return only_id;
	}
	public void setOnly_id(String only_id) {
		this.only_id = only_id;
	}
}
