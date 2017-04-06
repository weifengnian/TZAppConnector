package com.tuzhi.app.dao;

import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppUserInfo;

public class AppUserInfoDao extends SqlSessionDaoSupport implements IAppUserInfoDao {

	@Override
	public AppUserInfo getAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getAppUser",map);
	}

	@Override
	public Integer addAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addAppUser",map);
	}

	@Override
	public AppEnterprisesInfo getEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getEnterprises",map);
	}

	@Override
	public Integer addEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addEnterprises",map);
	}
	
	@Override
	public Integer addAppUserField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addAppUserField",map);
	}
	
	@Override
	public Integer addAppUserCertificate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addAppUserCertificate",map);
	}

	@Override
	public Integer updateAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("AppUserInfoDaoMapper.updateAppUser",map);
	}

	@Override
	public Integer updateEnterprises(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("AppUserInfoDaoMapper.updateEnterprises",map);
	}

	@Override
	public Integer insertAppLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.insertAppLog",map);
	}
	
}
