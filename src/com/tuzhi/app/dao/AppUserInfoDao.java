package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;

public class AppUserInfoDao extends SqlSessionDaoSupport implements IAppUserInfoDao {

	@Override
	public AppUserDetailInfo getAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getAppUser",map);
	}
	
	@Override
	public List<AppGoodField> getGoodField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("AppUserInfoDaoMapper.getGoodField",map);
	}
	
	@Override
	public List<AppCertificate> getCertificate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("AppUserInfoDaoMapper.getCertificate",map);
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
