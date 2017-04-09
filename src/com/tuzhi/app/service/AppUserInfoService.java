package com.tuzhi.app.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tuzhi.app.dao.IAppUserInfoDao;
import com.tuzhi.app.entity.AppCertificate;
import com.tuzhi.app.entity.AppEnterprisesInfo;
import com.tuzhi.app.entity.AppGoodField;
import com.tuzhi.app.pojo.AppUserDetailInfo;
import com.tuzhi.app.util.StringUtil;


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
		
		//修改用户 领域信息（注意，这里使用先删除后添加，一对多，主从表关系）
		if(StringUtil.isBlank(map.get("good_field"))){
			//删除该用户 领域信息
			@SuppressWarnings("unused")
			int num = appUserInfoDao.deleteUserField(map);
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
		if(StringUtil.isBlank(map.get("qualification_certificate"))){
			//删除用户资质证书
			int num = appUserInfoDao.deleteCertificate(map);
			//删除用户与证书关联表
			num = appUserInfoDao.deleteUserCertificate(map);
			String[] cn = map.get("certificate_name").split(",");
			String[] cf = map.get("qualification_certificate").split(",");
			for (int i = 0; i < cf.length; i++) {
				Map<String,String> cfMap = new HashMap<String,String>();
				cfMap.put("user_id", map.get("user_id"));
				//证书名称
				cfMap.put("certificate_name", cn[i]);  
				//证书url
				cfMap.put("qualification_certificate", cf[i]);
				//添加证书
				num = appUserInfoDao.addCertificate(cfMap); 
				if(num>0){
					//获取证书id
					AppCertificate acf = appUserInfoDao.getAppCertificate(cfMap);
					if(acf!=null){
						Map<String,Object> ufMap = new HashMap<String,Object>();
						ufMap.put("user_id", map.get("user_id"));
						ufMap.put("certificate_id", acf.getId());
						//添加用户与证书关联表
						num = appUserInfoDao.addUserCertificate(cfMap);
					}
				}
			}
		}
		
		//修改用户身份证信息（注意，这里使用先删除后添加）
		if(StringUtil.isBlank(map.get("card_img"))){
			//删除用户身份证书信息（根据身份证号删除）
			/*int num = appUserInfoDao.deleteCardId(map);*/
			
			String cd[] = map.get("card_img").split(",");
			Map<String,String> cfMap = new HashMap<String,String>();
			cfMap.put("number", map.get("card"));
			cfMap.put("upper_url", cd[0]);
			cfMap.put("below_url", cd[1]);
			/*num = appUserInfoDao.addCardInfo(cfMap);*/
		}
		
		//获取接单地址
		if(StringUtil.isBlank(map.get("order_address"))){
			//获取地址集合
			String address = map.get("order_address");
			JSONArray arr = JSONArray.fromObject(address);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject j = arr.getJSONObject(i);
				JSONObject job  = JSONObject.fromObject(j);
				String adrs = job.get("list").toString();
				JSONObject jsonAdrs  = JSONObject.fromObject(adrs);
				@SuppressWarnings("rawtypes")
				Iterator itea = jsonAdrs.keys();
				// 遍历jsonObject数据,添加到Map对象
				Map<String,String> maps = new HashMap<String,String>();
				while (itea.hasNext()) {
			        String key = itea.next().toString();
			        String value = jsonAdrs.get(key).toString();
			        maps.put(key, value);
			    }
				/*int num = appUserInfoDao.addAddress(maps); */
			}
		}
		
//		“user_id”:”编号”,
//		“phone”:”手机号”，
//		“user_name”:”真实姓名”,
//	    “nick_name”:”昵称”,
//		“card”:”身份证号”,
//		“sex”:”性别”，
//		“graduation_time”:”毕业时间”，
//	    “order_address”:{// 接单地址
//	       “list”:[{
//					“id”:”编号”，
//					“pro_id”:”省编号”，
//	  				“pro_name”:” 省名称” ，
//	 				“city_id”:”城市编号”, 
//	 				“city_name”:”城市名称”，
//					“dis_id”:”区编号”，
//					“dis_name”:” 区名称” ，
//	 				“street_id”:”街道编号”,
//					“street_name”:”街道名称”,
//					“details”:”地址详细”
//				   }]
//						},
 

//		“card_img”:”身份证照片”，
//		“token”:”系统唯一标示”，
//		“version”:”版本”，
//		“logo”:”头像url”

		
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
