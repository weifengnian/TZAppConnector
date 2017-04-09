package com.tuzhi.app.util;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	
	private final static Logger log = LoggerFactory.getLogger(StringUtil.class);
	
	//获取前台传输的JSON字符串
	@SuppressWarnings("rawtypes")
	public static void getJsonStr(BufferedReader br,Map<String,String> map){
		
		try {
			
			//获取JSON字符串
			log.info("--br:"+br);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine())!=null){
				sb.append(line);
			}
			log.info("--sb:"+sb.toString());
			if(StringUtil.isBlank(sb.toString())){
				return ;
			}
			
			// 将json字符串转换成jsonObject
			JSONObject jsonObject  = JSONObject.fromObject(sb.toString());
			Iterator ite = jsonObject.keys();
			// 遍历jsonObject数据,添加到Map对象
			while (ite.hasNext()) {
		        String key = ite.next().toString();
		        String value = jsonObject .get(key).toString();
		        map.put(key, value);
		    }
			
			// 或者直接将 jsonObject赋值给Map
//		     map = jsonObject;
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("----getJsonStr---Exception:"+e.getMessage());
		}
		
	}
	
	//判断字符是否为空
	public static boolean isBlank(String str) {
		if (null == str)
			return true;
		if ("".equals(str.replaceAll("　"," ").trim()))
			return true;
		return false;
	}
	
	//判断字符是否为空
	public static String isStr(String str) {
		
		String string = null==str?"":str;
		
		return string;
	}

	//Md5加密
	public static String MD5pwd(String pwd){
		
		if(StringUtil.isBlank(pwd)){
			return "";
		}
		
		String password = null;
		try {
			// 得到一个信息摘要器  
            MessageDigest digest = MessageDigest.getInstance("MD5");  
            byte[] result = digest.digest(pwd.getBytes("UTF-8"));  
            StringBuffer buffer = new StringBuffer();  
            // 把没一个byte 做一个与运算 0xff;  
            for (byte b : result) {  
                // 与运算  
                int number = b & 0xff;// 加盐  
                String str = Integer.toHexString(number);  
                if (str.length() == 1) {  
                    buffer.append("0");  
                }  
                buffer.append(str);  
            }
            password = buffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("--MD5pwd--Exception:"+e.getMessage());
		}

		return password;
	}
	
	private static final SimpleDateFormat DF_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 获得date时间
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDisplayYMDHMS(){
		try {
			Calendar calendar = Calendar.getInstance();
			return ((SimpleDateFormat) DF_YMDHMS.clone()).format(calendar
					.getTime());
		} catch (Exception e) {
			return null;
		}		
	}

}
