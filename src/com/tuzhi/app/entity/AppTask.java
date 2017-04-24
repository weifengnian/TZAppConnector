package com.tuzhi.app.entity;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月24日	
 * @Copyright:
 */
public class AppTask {
	
	private int id; //	
	private String title; //标题
	private String content; //内容
	private String order_code; //订单编号
	private String start_time; //开始时间
	private String end_time; //结束时间
	private double money; //金额(两位小数)
	private int is_urgent; //加急置顶	(排序规则.暂时不收费,预留功能)
	private int is_acceptace; //验收单
	private String create_time; //创建时间
	private int create_user_id; //创建人编号
	private String create_per; //创建人
	private int status; //状态	-1:屏蔽	1:正常
	private int field_id; //关联领域
	private int address_id; //关联地址
	private String desc; //备注
	
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
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getIs_urgent() {
		return is_urgent;
	}
	public void setIs_urgent(int is_urgent) {
		this.is_urgent = is_urgent;
	}
	public int getIs_acceptace() {
		return is_acceptace;
	}
	public void setIs_acceptace(int is_acceptace) {
		this.is_acceptace = is_acceptace;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(int create_user_id) {
		this.create_user_id = create_user_id;
	}
	public String getCreate_per() {
		return create_per;
	}
	public void setCreate_per(String create_per) {
		this.create_per = create_per;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getField_id() {
		return field_id;
	}
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}
	public int getAddress_id() {
		return address_id;
	}
	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
