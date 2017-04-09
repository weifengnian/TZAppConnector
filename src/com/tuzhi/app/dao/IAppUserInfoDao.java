package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;

public interface IAppUserInfoDao {
	
	/**
	 * 查询用户信息
	 * @param map
	 * @return
	 */
	public AppUserDetailInfo getAppUser(Map<String,String> map);
	
	/**
	 * 查询个人用户领域信息
	 * @param map
	 * @return
	 */
	public List<AppGoodField> getGoodField(Map<String,String> map);
	
	/**
	 * 查询个人用户资质证书信息
	 * @param map
	 * @return
	 */
	public List<AppCertificate> getCertificate(Map<String,String> map);
	
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
	 * 删除用户领域信息
	 * @param map
	 * @return
	 */
	public Integer deleteUserField(Map<String,String> map);
	
	/**
	 * 新增个人用户 擅长领域关联表
	 * @param map
	 * @return
	 */
	public Integer addAppUserField(Map<String,String> map);
	
	/**
	 * 删除用户资质证书
	 * @param map
	 * @return
	 */
	public Integer deleteCertificate(Map<String,String> map);
	
	/**
	 * 删除用户与证书关联表
	 * @param map
	 * @return
	 */
	public Integer deleteUserCertificate(Map<String,String> map);
	
	/**
	 * 添加证书
	 * @param map
	 * @return
	 */
	public Integer addCertificate(Map<String,String> map);
	
	/**
	 * 获取证书id
	 * @param map
	 * @return
	 */
	public AppCertificate getAppCertificate(Map<String,String> map);
	
	/**
	 * 新增用户证书关联表
	 * @param map
	 * @return
	 */
	public Integer addUserCertificate(Map<String,String> map);
	
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
