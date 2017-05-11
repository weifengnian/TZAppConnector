package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppCard;
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
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
	public Integer deleteUserField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().delete("AppUserInfoDaoMapper.deleteUserField",map);
	}
	
	@Override
	public Integer addAppUserField(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addAppUserField",map);
	}
	
	@Override
	public Integer deleteCertificate(Map<String,String> map){
		// TODO Auto-generated method stub
		return getSqlSession().delete("AppUserInfoDaoMapper.deleteCertificate",map);
	}
	
	@Override
	public Integer deleteUserCertificate(Map<String,String> map){
		// TODO Auto-generated method stub
		return getSqlSession().delete("AppUserInfoDaoMapper.deleteUserCertificate",map);
	}
	
	@Override
	public int addCertificate(Map<String,String> map){
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addCertificate",map);
	}
	
	@Override
	public AppCertificate getAppCertificate(Map<String,String> map){
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getAppCertificate",map);
	}
	
	@Override
	public Integer addUserCertificate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addUserCertificate",map);
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
	public Integer updateCardInfo(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("AppUserInfoDaoMapper.updateCardInfo",map);
	}
	
	@Override
	public Integer addCardInfo(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addCardInfo",map);
	}
	
	@Override
	public AppCard getCardInfo(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getCardInfo",map);
	}
	
	@Override
	public AppAddress getAddress(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("AppUserInfoDaoMapper.getAddress",map);
	}
	
	@Override
	public Integer updateAddress(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().update("AppUserInfoDaoMapper.updateAddress",map);
	}
	
	@Override
	public Integer addAddress(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addAddress",map);
	}
	
	@Override
	public Integer addUserOrdAds(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.addUserOrdAds",map);
	}
	
	@Override
	public Integer insertAppLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return getSqlSession().insert("AppUserInfoDaoMapper.insertAppLog",map);
	}

	@Override
	public List<AppAddress> getAddressList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("AppUserInfoDaoMapper.getAddressList",map);
	}
	
}
