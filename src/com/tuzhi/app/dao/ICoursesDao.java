package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.pojo.CoursesInfo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public interface ICoursesDao {
	
	/**
	 * 获取我的课程
	 * @param map
	 * @return
	 */
	public List<CoursesInfo> getMyCourses(Map<String, String> map);
	
	/**
	 * 获取课程-->章节 （获取章节）
	 * @param map
	 * @return
	 */
	public List<CoursesInfo> getChapter(Map<String, String> map);
	
	/**
	 * 获取课程-->章节-->课时 （获取课时）
	 * @param map
	 * @return
	 */
	public List<CoursesInfo> getClass(Map<String, String> map);
	
	/**
	 * 查询该用户，是否拥有该课程
	 * @param map
	 * @return
	 */
	public Integer getCoursesUser(Map<String, String> map);
	
	/**
	 * 将该课程，添加到该用户下
	 * @param map
	 * @return
	 */
	public Integer addCoursesUser(Map<String, String> map);
	
}
