package com.tuzhi.app.util;

/**
 * @Description: 工具类
 * @company: 
 * @author: weifengnian
 * @Data: 2017年3月23日	
 * @Copyright:
 */
public class TransUtil {
	
	public static final String ENCODING = "UTF-8"; //编码类型
	
	//本地
//	public static final String UPLOAD_PATH = "D:\\Program Files\\apache-tomcat-7.0.55\\webapps"; //上传路径路径
	//生产
	public static final String UPLOAD_PATH = "C:\\tomcat\\apache-tomcat-7.0.55\\webapps"; //上传路径路径
	
	//本地
//	public static final String LOAD_PATH = "http://localhost:8080"; //查看路径
	//生产
	public static final String LOAD_PATH = "http://192.168.8.239:8080"; //查看路径
	
	//是否记录日志
	public static final boolean LOG_FLAG = true;
	
	//记录日志url
	public static final String LOG_URL = "http://192.168.8.239:8080/TZAppConnector/manager/";

}
