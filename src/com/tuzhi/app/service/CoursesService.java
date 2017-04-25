package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.ICoursesDao;
import com.tuzhi.app.pojo.CoursesInfo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月20日	
 * @Copyright:
 */
public class CoursesService implements ICoursesService {

	private ICoursesDao coursesDao;

	public ICoursesDao getCoursesDao() {
		return coursesDao;
	}
	public void setCoursesDao(ICoursesDao coursesDao) {
		this.coursesDao = coursesDao;
	}

	@Override
	public List<CoursesInfo> getMyCourses(Map<String, String> map) {
		// TODO Auto-generated method stub
		return coursesDao.getMyCourses(map);
	}
	
	@Override
	public List<CoursesInfo> getChapter(Map<String, String> map) {
		// TODO Auto-generated method stub
		return coursesDao.getChapter(map);
	}
	
	@Override
	public List<CoursesInfo> getClass(Map<String, String> map) {
		// TODO Auto-generated method stub
		return coursesDao.getClass(map);
	}
	
	@Override
	public Integer getCoursesUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return coursesDao.getCoursesUser(map);
	}
	
	@Override
	public Integer addCoursesUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return coursesDao.addCoursesUser(map);
	}

}
