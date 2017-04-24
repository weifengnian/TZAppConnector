package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
		if("-1".equals(map.get("user_id"))){
			map.remove("page");
			map.remove("rows");
		}
		
		int pagenum=1;
		int pagesize=999999;
		if(map.get("page")!=null && StringUtils.isNumeric(map.get("page").toString()))
			pagenum=Integer.parseInt(map.get("page").toString());
		if(map.get("rows")!=null && StringUtils.isNumeric(map.get("rows").toString()))
			pagesize=Integer.parseInt(map.get("rows").toString());
		RowBounds row = StringUtil.getRowBounds(pagenum, pagesize);
		return getSqlSession().selectList("CoursesDaoMapper.getMyCourses",map,row);
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

}
