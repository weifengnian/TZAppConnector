package com.tuzhi.app.dao;

import java.util.Map;

import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppUserInfo;

public interface IAppUserInfoDao {
	
	/**
	 * 查询用户信息
	 * @param map
	 * @return
	 */
	public AppUserInfo getAppUser(Map<String,String> map);
	
	/**
	 * 注册用户信息
	 * @param map
	 * @return
	 */
	public Integer addAppUser(Map<String,String> map);
	
	/**
	 * 查询企业用户信息
	 * @param map
	 * @return
	 */
	public AppEnterprisesInfo getEnterprises(Map<String,String> map);
	
	/**
	 * 注册企业用户信息
	 * @param map
	 * @return
	 */
	public Integer addEnterprises(Map<String,String> map);
	
	/**
	 * 新增个人用户 擅长领域关联表
	 * @param map
	 * @return
	 */
	public Integer addAppUserField(Map<String,String> map);
	
	/**
	 * 新增个人用户用户证书关联表
	 * @param map
	 * @return
	 */
	public Integer addAppUserCertificate(Map<String,String> map);
	
	/**
	 * 修改（完善）用户信息
	 * @param map
	 * @return
	 */
	public Integer updateAppUser(Map<String,String> map);
	
	/**
	 * 修改（完善）企业用户信息
	 * @param map
	 * @return
	 */
	public Integer updateEnterprises(Map<String,String> map);

	/**
	 * 添加请求日志
	 * @param map
	 * @return
	 */
	public Integer insertAppLog(Map<String,Object> map);
}
