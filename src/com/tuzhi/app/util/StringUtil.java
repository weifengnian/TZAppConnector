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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.tuzhi.app.pojo.AppPickPeople;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.Message.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import net.arnx.jsonic.JSON;
import net.sf.json.JSONObject;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class StringUtil {
	
	private final static Logger log =  Logger.getLogger(StringUtil.class);
	
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
	
	public static String goodField(String str){
		String field = "";
		switch(str){
			case "1":
				field = "网络";
				break;
			case "2":
				field = "服务器";
				break;
			case "3":
				field = "监控";
				break;
			case "4":
				field = "虚拟化";
				break;
			case "5":
				field = "云";
				break;
			case "6":
				field = "视频";
				break;
			default:
				field = "未知";
				break;
		}
		return field;
	}
	
	public static void sendTask(String taskId,Map<String, String> map,List<AppPickPeople> apl){
		try {
			Map<String, String> sendMap = new HashMap<String, String>();
			sendMap.put("task_id",  taskId);
			sendMap.put("task_start_date",  map.get("start_time")==null?"":map.get("start_time"));
			sendMap.put("task_end_date",  map.get("end_time")==null?"":map.get("end_time"));
			sendMap.put("sender",  map.get("create_per")==null?"":map.get("create_per"));
			sendMap.put("release_time",  map.get("create_time")==null?"":map.get("create_time"));
			sendMap.put("title",  map.get("title")==null?"":map.get("title"));
			sendMap.put("money",  map.get("money")==null?"":map.get("money"));
			sendMap.put("address", map.get("address")==null?"":map.get("address"));
			sendMap.put("field", goodField(map.get("field")==null?"":map.get("field")));
			String json = JSON.encode(sendMap);
			
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < apl.size(); i++) {
				bf.append(","+apl.get(i).getUser_id()+"");
			}
			
			String[] alias =  bf.toString().substring(1).split(",");
			sendPush(alias,sendMap.get("title"),json);

		} catch (Exception e) {
			// TODO: handle exception
			log.info("----------sendTask--Exception:"+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		int num = 0;
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			num++;
			bf.append(","+num+"");
		}
		System.out.println(bf.toString().substring(1));
		
		String s = bf.toString().substring(1);
		String[] string = s.split(",") ;
//		System.out.println(string[0]);
//		
//		String[] str = {"13","16"};
//		
//		String[] abc = null;
//		abc[0] = "a";
//		abc[1] = "b";
//		abc[2] = "c";
//		for (int i = 0; i < abc.length; i++) {
//			System.out.println(abc[i]);
//		}
		//sendPush(string,"123","测试数据");
	}
	
	public static void sendPush(String[] alias,String title,String content) {
		try {
			log.info("--alias:"+alias+",--title:"+title+",--content:"+content);
			String masterSecret = "db3823ab049847e1c9c35bb1";
			String appKey = "0a8593ed96f4032f3a67c831";
			JPushClient jpushClient = new JPushClient(masterSecret, appKey);
			PushPayload payload = buildPushObject_audienceOne(alias,title,content);
			PushResult result = jpushClient.sendPush(payload);
			log.info("--resultJpush:"+result);
		} catch (APIConnectionException e) {
			log.info("--APIConnectionException:"+e.getMessage());
			e.printStackTrace();
		} catch (APIRequestException e) {
			log.info("--APIRequestException:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static PushPayload buildPushObject_audienceOne(String[] alias,String title,String content) {
		Builder msg = Message.newBuilder();
		msg.setMsgContent(content);
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.alias(alias))
				.setMessage(Message.newBuilder().setMsgContent(content).setTitle(title).build())
				.setNotification(Notification.alert(content)).build();
	}
	
}
