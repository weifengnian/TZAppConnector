package com.tuzhi.app.pojo;

/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
public class AppUserDetailInfo {
	
	private int id; //
	private String name; //真实名
	private String nick_name; //昵称
	private String password; //密码
	private String icon_url; //头像
	private String local_url; //本地地址
	private String mobile_phone; //手机
	private int sex; //性别	1:男	2:女
	private String create_time; //创建时间
	private String update_time; //修改时间
	private int integral; //积分
	private int card_id; //关联身份证
	private String graduation_time; //毕业时间
	private String address_id; //所在地址
	private String token; //令牌
	private int level_id; //关联等级
	private int is_auth; //是否实名认证	0:未提交	1:已认证	10:认证中/待实名	-1:认证未通过(人工)	-2:认证未通过(机器)
	private String auth_create_time; //认证时间
	private double money; //帐号金额
	private int status; //状态	-1:黑名单	1:正常
	
	private int addressid; // id
	private String pro_id; //省
	private String pro_name; //省名称
	private String city_id; //市
	private String city_name; //城市名
	private String dis_id; //区
	private String dis_name; //区名
	private String street_id; //街道
	private String street_name; //街道名
	private String details; //详细
	private float latitude; //纬度
	private float longitude; //经度
	
	private int cardid; //
	private String number; //身份证号码
	private String upper_url; //正面照片地址
	private String local_upper_url; //本地地址
	private String below_url; //反面照片地址
	private String local_below_url; //本地地址
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getLocal_url() {
		return local_url;
	}
	public void setLocal_url(String local_url) {
		this.local_url = local_url;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getLevel_id() {
		return level_id;
	}
	public void setLevel_id(int level_id) {
		this.level_id = level_id;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAddressid() {
		return addressid;
	}
	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}
	public String getPro_id() {
		return pro_id;
	}
	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getDis_id() {
		return dis_id;
	}
	public void setDis_id(String dis_id) {
		this.dis_id = dis_id;
	}
	public String getDis_name() {
		return dis_name;
	}
	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}
	public String getStreet_id() {
		return street_id;
	}
	public void setStreet_id(String street_id) {
		this.street_id = street_id;
	}
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getUpper_url() {
		return upper_url;
	}
	public void setUpper_url(String upper_url) {
		this.upper_url = upper_url;
	}
	public String getLocal_upper_url() {
		return local_upper_url;
	}
	public void setLocal_upper_url(String local_upper_url) {
		this.local_upper_url = local_upper_url;
	}
	public String getBelow_url() {
		return below_url;
	}
	public void setBelow_url(String below_url) {
		this.below_url = below_url;
	}
	public String getLocal_below_url() {
		return local_below_url;
	}
	public void setLocal_below_url(String local_below_url) {
		this.local_below_url = local_below_url;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getGraduation_time() {
		return graduation_time;
	}
	public void setGraduation_time(String graduation_time) {
		this.graduation_time = graduation_time;
	}
	public String getAuth_create_time() {
		return auth_create_time;
	}
	public void setAuth_create_time(String auth_create_time) {
		this.auth_create_time = auth_create_time;
	}
}
