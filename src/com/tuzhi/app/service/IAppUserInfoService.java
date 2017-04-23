package com.tuzhi.app.service;

import java.util.List;
import java.util.Map;

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
public interface IAppUserInfoService {
	
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
