package com.tuzhi.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.dao.IEnterpriseTaskDao;
import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppTaskUser;
import com.tuzhi.app.pojo.AppTaskInfo;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月20日	
 * @Copyright:
 */
public class EnterpriseTaskService implements IEnterpriseTaskService {

	private IEnterpriseTaskDao enterpriseTaskDao;
	private IAppUserInfoDao appUserInfoDao;
	
	public IAppUserInfoDao getAppUserInfoDao() {
		return appUserInfoDao;
	}
	public void setAppUserInfoDao(IAppUserInfoDao appUserInfoDao) {
		this.appUserInfoDao = appUserInfoDao;
	}
	public IEnterpriseTaskDao getEnterpriseTaskDao() {
		return enterpriseTaskDao;
	}
	public void setEnterpriseTaskDao(IEnterpriseTaskDao enterpriseTaskDao) {
		this.enterpriseTaskDao = enterpriseTaskDao;
	}

	@Override
	public Integer insertTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		AppAddress ads = null;
		//修改用户接单地址（注意，这里使用先通过id查询,存在就修改，否则添加）
		if(!StringUtil.isBlank(map.get("address")) || !StringUtil.isBlank(map.get("province")) || 
				!StringUtil.isBlank(map.get("city")) || !StringUtil.isBlank(map.get("district"))){
			
			Map<String,String> maps = new HashMap<String,String>();
			maps.put("details", map.get("address")); //详细地址
			maps.put("pro_name", map.get("province")); //省
			maps.put("city_name", map.get("city")); //市
			maps.put("dis_name", map.get("district")); //区
			maps.put("only_id", StringUtil.getShortUUID());
			//添加地址
			int num = appUserInfoDao.addAddress(maps);
			if(num>0){
				//获取地址id
				ads = appUserInfoDao.getAddress(maps);
			}
		}
		if(ads!=null){
			map.put("address_id", String.valueOf(ads.getId()));
		}
		
		//订单编号
		map.put("order_code", "BH"+String.valueOf(System.currentTimeMillis()));
		
		return enterpriseTaskDao.insertTask(map);
	}

	@Override
	public List<AppTaskInfo> getTask(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getTask(map);
	}
	
	@Override
	public List<AppTaskUser> getOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.getOrders(map);
	}
	
	@Override
	public Integer addOrders(Map<String, String> map) {
		// TODO Auto-generated method stub
		return enterpriseTaskDao.addOrders(map);
	}

}
