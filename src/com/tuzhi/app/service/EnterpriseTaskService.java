package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.IEnterpriseTaskDao;
import com.tuzhi.app.entity.AppTask;


public class EnterpriseTaskService implements IEnterpriseTaskService {

	private IEnterpriseTaskDao enterpriseTaskDao;
	
	public IEnterpriseTaskDao getEnterpriseTaskDao() {
		return enterpriseTaskDao;
	}
	public void setEnterpriseTaskDao(IEnterpriseTaskDao enterpriseTaskDao) {
		this.enterpriseTaskDao = enterpriseTaskDao;
	}

	@Override
	public Integer insertTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		return enterpriseTaskDao.insertTask(map);
	}

	@Override
	public List<AppTask> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTask(map);
	}

}
