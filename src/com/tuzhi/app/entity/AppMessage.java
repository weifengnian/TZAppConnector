package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月24日	
 * @Copyright:
 */
public class AppMessage {
	
	private int id; //
	private String sendtime; //发送时间
	private int sender_id; //发送者
	private String content; //内容
	private String title; //标题
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
