package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.ISystemMessagerDao;
import com.tuzhi.app.entity.AppBanner;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.AppMessage;
import com.tuzhi.app.entity.AppMsgReceive;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
public class SystemMessagerService implements ISystemMessagerService {

	private ISystemMessagerDao systemMessagerDao;
	
	public ISystemMessagerDao getSystemMessagerDao() {
		return systemMessagerDao;
	}

	public void setSystemMessagerDao(ISystemMessagerDao systemMessagerDao) {
		this.systemMessagerDao = systemMessagerDao;
	}

	@Override
	public Integer insertFeedback(Map<String, String> map) {
		// TODO Auto-generated method stub
		return systemMessagerDao.insertFeedback(map);
	}

	@Override
	public List<AppMessage> getMessage(Map<String, String> map) {
		// TODO Auto-generated method stub
		return systemMessagerDao.getMessage(map);
	}

	@Override
	public AppMsgReceive getMsgReceive(Map<String, String> map) {
		// TODO Auto-generated method stub
		return systemMessagerDao.getMsgReceive(map);
	}

	@Override
	public List<AppGoodField> getAllField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return systemMessagerDao.getAllField(map);
	}

	@Override
	public List<AppBanner> getMyBanner(Map<String, String> map) {
		// TODO Auto-generated method stub
		return systemMessagerDao.getMyBanner(map);
	}
	
}
