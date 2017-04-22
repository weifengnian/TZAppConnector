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
	 * 获取课程详细
	 * @param map
	 * @return
	 */
	public List<CoursesInfo> getCourseDetails(Map<String, String> map);
	
}
