package com.tuzhi.app.dao;

import java.util.List;
import java.util.Map;

import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppCard;
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.Appquestion;
import com.tuzhi.app.entity.Appquestionreply;
import com.tuzhi.app.pojo.AppUserDetailInfo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
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
	public int addCertificate(Map<String,String> map);
	
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
	 * 修改用户身份证书信息（根据身份证号修改）
	 * @param map
	 * @return
	 */
	public Integer updateCardInfo(Map<String, String> map);
	
	/**
	 * 添加用户身份证书信息
	 * @param map
	 * @return
	 */
	public Integer addCardInfo(Map<String, String> map);
	
	/**
	 * 获取用户身份证书信息
	 * @param map
	 * @return
	 */
	public AppCard getCardInfo(Map<String, String> map);

	/**
	 * 查询地址id是否存在
	 * @param map
	 * @return
	 */
	public AppAddress getAddress(Map<String, String> map);
	
	/**
	 * 修改地址
	 * @param map
	 * @return
	 */
	public Integer updateAddress(Map<String, String> map);
	
	/**
	 * 添加地址
	 * @param map
	 * @return
	 */
	public Integer addAddress(Map<String, String> map);
	
	/**
	 * 添加用户地址关联表
	 * @param map
	 * @return
	 */
	public Integer addUserOrdAds(Map<String, String> map);
	
	/**
	 * 添加请求日志
	 * @param map
	 * @return
	 */
	public Integer insertAppLog(Map<String,Object> map);
	
	/**
	 * 查询用户接单地址
	 * @param map
	 * @return
	 */
	public List<AppAddress> getAddressList(Map<String,String> map);
	
	/**
	 * 查询问题
	 * @param map
	 * @return
	 */
	public List<Appquestion> getAppquestion(Map<String,String> map);
	
	/**
	 * 问题回复
	 * @param map
	 * @return
	 */
	public Integer addAppquestionreply(Map<String,String> map);
	
	/**
	 * 查询回复内容
	 * @param map
	 * @return
	 */
	public List<Appquestionreply> getAppquestionreply(Map<String,String> map);
}
