package com.tuzhi.app.service;

import java.util.Map;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppUserInfo;


public class AppUserInfoService implements IAppUserInfoService {

	private IAppUserInfoDao appUserInfoDao;

	public IAppUserInfoDao getAppUserInfoDao() {
		return appUserInfoDao;
	}

	public void setAppUserInfoDao(IAppUserInfoDao appUserInfoDao) {
		this.appUserInfoDao = appUserInfoDao;
	}
	
	@Override
	public AppUserInfo getAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getAppUser(map);
	}

	@Override
	public Integer addAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.addAppUser(map);
	}

	@Override
	public AppEnterprisesInfo getEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getEnterprises(map);
	}

	@Override
	public Integer addEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.addEnterprises(map);
	}
	
	@Override
	public Integer addAppUserField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.addAppUserField(map);
	}
	
	@Override
	public Integer addAppUserCertificate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.addAppUserField(map);
	}

	@Override
	public Integer updateAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.updateAppUser(map);
	}

	@Override
	public Integer updateEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.updateEnterprises(map);
	}
	
	@Override
	public Integer insertAppLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.insertAppLog(map);
	}
	
}
