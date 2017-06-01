package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import com.tuzhi.app.pojo.CoursesInfo;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public class CoursesDao extends SqlSessionDaoSupport implements ICoursesDao {
	
	@Override
	public List<CoursesInfo> getMyCourses(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		//userid＝－1查询所有课程
//		if("-1".equals(map.get("user_id"))){
//			map.remove("page");
//			map.remove("rows");
//		}
		
		if(StringUtil.isBlank(map.get("page"))){
			map.put("page", "1");
		}	
		if(StringUtil.isBlank(map.get("rows"))){
			map.put("rows", "999999");
		}
		if("-1".equals(map.get("user_id"))){
			return getSqlSession().selectList("CoursesDaoMapper.getMyCourses1",map);
		}else if("-1".equals(map.get("course_type_id"))){
			return getSqlSession().selectList("CoursesDaoMapper.getMyCourses2",map);
		}else{
			return getSqlSession().selectList("CoursesDaoMapper.getMyCourses",map);
		}
	}
	

	@Override
	public List<CoursesInfo> getChapter(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("CoursesDaoMapper.getChapter",map);
	}

	@Override
	public List<CoursesInfo> getClass(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("CoursesDaoMapper.getClass",map);
	}

	@Override
	public Integer getCoursesUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("CoursesDaoMapper.getCoursesUser",map);
	}

	@Override
	public Integer addCoursesUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("CoursesDaoMapper.addCoursesUser",map);
	}

}
