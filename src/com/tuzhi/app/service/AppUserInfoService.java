package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;


public class AppUserInfoService implements IAppUserInfoService {

	private IAppUserInfoDao appUserInfoDao;

	public IAppUserInfoDao getAppUserInfoDao() {
		return appUserInfoDao;
	}

	public void setAppUserInfoDao(IAppUserInfoDao appUserInfoDao) {
		this.appUserInfoDao = appUserInfoDao;
	}
	
	@Override
	public AppUserDetailInfo getAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getAppUser(map);
	}
	
	@Override
	public List<AppGoodField> getGoodField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getGoodField(map);
	}
	
	@Override
	public List<AppCertificate> getCertificate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getCertificate(map);
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
