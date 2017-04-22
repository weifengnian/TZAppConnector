package com.tuzhi.app.pojo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class CoursesInfo {
	
	private int id; //	
	private String code; //课程编号
	private String name; //课程名称
	private String img_url; //Img url
	private String local_url; //本地位置
	private String content; //详细介绍
	private int courses_type_id; //关联课程类型
	private int user_id; //创建人
	private String create_time; //创建时间
	private String update_time; //修改时间
	private String desc; //备注

	private String ctname; //课程类型
	private String study_num; //课程参与人数
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getLocal_url() {
		return local_url;
	}
	public void setLocal_url(String local_url) {
		this.local_url = local_url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCourses_type_id() {
		return courses_type_id;
	}
	public void setCourses_type_id(int courses_type_id) {
		this.courses_type_id = courses_type_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCtname() {
		return ctname;
	}
	public void setCtname(String ctname) {
		this.ctname = ctname;
	}
	public String getStudy_num() {
		return study_num;
	}
	public void setStudy_num(String study_num) {
		this.study_num = study_num;
	}
}
