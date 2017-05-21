package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年5月21日	
 * @Copyright:
 */
public class Appquestion {
	
	private int id; //	
	private String title; //问题标题
	private String content; //内容
	private String create_time; //创建时间
	private int user_id; //创建人编号
	private String create_per; //创建人
	private int is_selected; //1:精选  0:no
	private int status; //0:不显示 1:显示
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCreate_per() {
		return create_per;
	}
	public void setCreate_per(String create_per) {
		this.create_per = create_per;
	}
	public int getIs_selected() {
		return is_selected;
	}
	public void setIs_selected(int is_selected) {
		this.is_selected = is_selected;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
