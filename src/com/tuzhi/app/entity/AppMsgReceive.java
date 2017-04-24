package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月24日	
 * @Copyright:
 */
public class AppMsgReceive {
	
	private int id; //
	private int msg_id; //信息关联
	private int status; //0：未读,1：已读
	private String status_time; //已读时间
	private int type; //1:个人	2:企业
	private int u_id; //编号(type=1指用户id、type=2指企业id)
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatus_time() {
		return status_time;
	}
	public void setStatus_time(String status_time) {
		this.status_time = status_time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
}
