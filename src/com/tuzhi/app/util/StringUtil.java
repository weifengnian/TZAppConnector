package com.tuzhi.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
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
	
	/**
	 * 获得date时间
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	private static final SimpleDateFormat DF_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getDisplayYMDHMS(){
		try {
			Calendar calendar = Calendar.getInstance();
			return ((SimpleDateFormat) DF_YMDHMS.clone()).format(calendar
					.getTime());
		} catch (Exception e) {
			return null;
		}		
	}
	
	/**
	 * 获得date时间
	 * @param date
	 * @return yyyy-MM-dd
	 */
	private static final SimpleDateFormat DF_YMD = new SimpleDateFormat("yyyy-MM-dd");
	public static String getDisplayYMD(){
		try {
			Calendar calendar = Calendar.getInstance();
			return ((SimpleDateFormat) DF_YMD.clone()).format(calendar
					.getTime());
		} catch (Exception e) {
			return null;
		}		
	}
	
	/**
	 * 获取uuid
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	public static String getShortUUID() {
		String s = getUUID();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
		+ s.substring(19, 23) + s.substring(24);
	}
	
	/**
	 * 复制文件
	 * @param src 源文件
	 * @param dst 目标文件
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void copy(File src, File dst) throws IOException {
		FileChannel srcChannel = new FileInputStream(src).getChannel();
		FileChannel destChannel = new FileOutputStream(dst).getChannel();
		try {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			srcChannel.close();
			destChannel.close();
		}
	}
	
	/**
	 * 建立文件夹
	 * @param filePath 文件夹路径
	 * @return
	 */
	public static File createDirectory(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	/**
	 * 分页
	 * @param PageNum
	 * @param PageSize
	 * @return
	 */
	public static RowBounds getRowBounds(int PageNum, int PageSize)
	{
		int offset;
		int limit;

		if (PageNum == 0)
		{
			offset = 0;
			//limit = Integer.MAX_VALUE;
			limit = PageSize;
		}
		else
		{
			offset = (PageNum - 1) * PageSize;
			//limit = offset + PageSize;
			limit = PageSize;
		}

		RowBounds rowBounds = new RowBounds(offset, limit);
		return rowBounds;
	}

}
