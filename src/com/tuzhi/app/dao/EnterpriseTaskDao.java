package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月17日	
 * @Copyright:
 */
public class EnterpriseTaskDao extends SqlSessionDaoSupport implements IEnterpriseTaskDao {
	
	@Override
	public Integer insertTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("EnterpriseTaskDaoMapper.insertTask",map);
	}

	@Override
	public List<AppTaskInfo> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		int pagenum=1;
		int pagesize=999999;
		if(map.get("page")!=null && StringUtils.isNumeric(map.get("page").toString()))
			pagenum=Integer.parseInt(map.get("page").toString());
		if(map.get("rows")!=null && StringUtils.isNumeric(map.get("rows").toString()))
			pagesize=Integer.parseInt(map.get("rows").toString());
		RowBounds row = StringUtil.getRowBounds(pagenum, pagesize);
		return getSqlSession().selectList("EnterpriseTaskDaoMapper.getTask",map,row);
	}

}
