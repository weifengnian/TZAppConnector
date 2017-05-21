package com.tuzhi.app.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.entity.AppAddress;
import com.tuzhi.app.entity.AppCard;
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.entity.Appquestion;
import com.tuzhi.app.entity.Appquestionreply;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.util.StringUtil;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月23日	
 * @Copyright:
 */
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

	@SuppressWarnings("unused")
	@Override
	public Integer updateAppUser(Map<String, String> map) {
		// TODO Auto-generated method stub
		
		//修改用户 领域信息（注意，这里使用先查询，存在就修改，否则就添加，）
		if(!StringUtil.isBlank(map.get("good_field"))){
			//删除该用户 领域信息
			Integer num = appUserInfoDao.deleteUserField(map);
			String[] field = map.get("good_field").split(",");
			for (int i = 0; i < field.length; i++) {
				Map<String,String> gfMap = new HashMap<String,String>();
				gfMap.put("user_id", map.get("user_id"));
				gfMap.put("good_field", field[i]);
				//新增个人用户 擅长领域关联表
			  num = appUserInfoDao.addAppUserField(gfMap);
			}
		 }
		
		//修改用户资质证书信息（注意，这里使用先删除后添加，一对多，主从表关系）
		if(!StringUtil.isBlank(map.get("qualification_certificate")) && !StringUtil.isBlank(map.get("certificate_name"))){
			
			String[] cn = map.get("certificate_name").split(",");
			String[] cf = map.get("qualification_certificate").split(",");
			String[] lcf = map.get("local_qualification_certificate").split(",");
			
			if(cn.length==cf.length){
				//删除用户资质证书
				//int num = appUserInfoDao.deleteCertificate(map);
				//删除用户与证书关联表
				int num = appUserInfoDao.deleteUserCertificate(map);
				for (int i = 0; i < cf.length; i++) {
					Map<String,String> cfMap = new HashMap<String,String>();
					cfMap.put("user_id", map.get("user_id"));
					//证书名称
					cfMap.put("certificate_name", cn[i]);  
					//证书url
					cfMap.put("qualification_certificate", cf[i]);
					//本地url
					cfMap.put("local_url", lcf[i]);
					//自定义标识
					cfMap.put("only_id", StringUtil.getShortUUID());
					//添加证书
					num = appUserInfoDao.addCertificate(cfMap); 
					if(num>0){
						//获取证书id
						AppCertificate acf = appUserInfoDao.getAppCertificate(cfMap);
						if(acf!=null){
							Map<String,String> ufMap = new HashMap<String,String>();
							ufMap.put("user_id", map.get("user_id"));
							ufMap.put("certificate_id", String.valueOf(acf.getId()));
							//添加用户与证书关联表
							num = appUserInfoDao.addUserCertificate(ufMap);
						}
					}
				}
			}
		}
		
		//修改用户身份证信息（注意，这里使用先查询,存在就修改，否则添加）
		AppCard crd = null;
		if(!StringUtil.isBlank(map.get("card_img")) && !StringUtil.isBlank(map.get("card"))){
			int num = 0;
			String cd[] = map.get("card_img").split(",");
			String lcd[] = map.get("local_card_img").split(",");
			Map<String,String> cdMap = new HashMap<String,String>();
			cdMap.put("number", map.get("card"));
			cdMap.put("upper_url", cd[0]);
			cdMap.put("below_url", cd[1]);
			cdMap.put("local_upper_url", lcd[0]);
			cdMap.put("local_below_url", lcd[1]);
			crd = appUserInfoDao.getCardInfo(cdMap);
			if(crd==null){
				//添加
				num = appUserInfoDao.addCardInfo(cdMap);
				if(num>0){
					crd = appUserInfoDao.getCardInfo(cdMap);
				}
			}else{
				//修改
				num = appUserInfoDao.updateCardInfo(cdMap);
			}
		}
		if(crd != null){
			map.put("card_id", String.valueOf(crd.getId()));
		}
		
		
		//修改用户接单地址（注意，这里使用先通过id查询,存在就修改，否则添加）
		if(!StringUtil.isBlank(map.get("order_address"))){
			//获取地址集合
			String address = map.get("order_address");
			JSONObject jsonAddress = JSONObject.fromObject(address);
			String list = jsonAddress.get("list").toString();
			JSONArray arr = JSONArray.fromObject(list);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject j = arr.getJSONObject(i);
				JSONObject jsonAdrs  = JSONObject.fromObject(j);
				@SuppressWarnings("rawtypes")
				Iterator itea = jsonAdrs.keys();
				//遍历jsonObject数据,添加到Map对象，maps=接单地址
				Map<String,String> maps = new HashMap<String,String>();
				while (itea.hasNext()) {
			        String key = itea.next().toString();
			        String value = jsonAdrs.get(key).toString();
			        maps.put(key, value);
			    }
				
				AppAddress ads = null;
				//判断接单地址id为空,直接做添加，否则查询
				if(!StringUtil.isBlank(maps.get("id"))){
					//查询地址id是否存在
					ads = appUserInfoDao.getAddress(maps);
					if(ads!=null){
						//修改地址
						int num = appUserInfoDao.updateAddress(maps);
					}
				}
				
				if(StringUtil.isBlank(maps.get("id")) || ads==null){
					maps.put("only_id", StringUtil.getShortUUID());
					//添加地址
					int num = appUserInfoDao.addAddress(maps);
					if(num>0){
						//获取地址id
						ads = appUserInfoDao.getAddress(maps);
						if(ads!=null){
							Map<String,String> adsMap = new HashMap<String,String>();
							adsMap.put("address_id", String.valueOf(ads.getId()));
							adsMap.put("user_id", map.get("user_id"));
							//添加用户接单关联地址表
							num = appUserInfoDao.addUserOrdAds(adsMap);
						}
					}
				}
			}
		}
		
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
	
	@Override
	public List<AppAddress> getAddressList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getAddressList(map);
	}
	
	@Override
	public List<Appquestion> getAppquestion(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getAppquestion(map);
	}
	
	@Override
	public Integer addAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.addAppquestionreply(map);
	}
	
	@Override
	public List<Appquestionreply> getAppquestionreply(Map<String, String> map) {
		// TODO Auto-generated method stub
		return appUserInfoDao.getAppquestionreply(map);
	}
}
