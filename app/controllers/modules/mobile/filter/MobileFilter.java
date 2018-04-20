package controllers.modules.mobile.filter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Http;
import play.mvc.Http.Request;
import utils.CommonValidateUtil;
import utils.DateUtil;
import utils.HttpClientUtil;
import utils.StringUtil;
import controllers.LoginService;
import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.WxUserBo;
import models.modules.mobile.WxServer;
import models.modules.mobile.WxUser;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-24 下午1:47:15
 * @describe  类说明
 */
public class MobileFilter extends BaseController{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MobileFilter.class);
	private static int REQ_TIME = 0;
	private final static String WX_CODE = "gh_00e09da4fab0";
	@Before(unless = { "Application.index","LoginService.index", "LoginService.mIndex","LoginService.login",
			"LoginService.logout","LoginService.mlogout" })
	static void checkLogin() {
		Http.Response.current().setHeader("Access-Control-Allow-Origin","*");
		log.debug("checkLogin开始检查登录状态");
		String userAgent = request.headers.get("user-agent").value().toLowerCase();
		boolean isNeedInterface =false;
		SessionInfo sessionInfo=getSessionInfo();
		String deviceFlag = params.get("flag");
		Logger.info("----------------deviceFlag"+deviceFlag);
		WxUser 	wxUser = null;
		if("weChat".equals(deviceFlag)){
			Logger.info("weChat登录 --- ");
			if(null != sessionInfo){
				Logger.info("weChat登录 --- 当前session不为空");
				wxUser = sessionInfo.getWxUser();
				log.debug("weChat登录 ---  当前用户:" + wxUser);
				if(null !=wxUser){
					isNeedInterface = false;
					 wxUser = WxUser.getFindByOpenId(wxUser.wxOpenId);
					 if(null != wxUser){
						 sessionInfo.setWxUser(wxUser);
				 		 Cache.add(MobileFilter.getSessionKey(), sessionInfo);
				 		 log.debug("weChat登录 --- 读取信息end");
					 }else{
						 sessionInfo=new SessionInfo();
						 isNeedInterface=true;
					 }
				}
			}else{
				log.debug("weChat登录 --- 当前session为空");
				if(!isMobile(userAgent)){//pc
					log.debug("weChat登录 --- 当前浏览器不是手机，这里认为是pc");
					if("testPC".equals(deviceFlag)){
						log.debug("weChat登录 --- 是pc上的测试标示testPC");
						wxUser = WxUser.getFindByOpenId(null);
						if(null != wxUser){
							log.debug("weChat登录 --- 使用模拟用户加入到session中，昵称是:" + wxUser.nickName);
							sessionInfo=new SessionInfo();
			        		sessionInfo.setWxUser(wxUser);
			        		setSessionInfo(sessionInfo);
							isNeedInterface = false;
						}else{
							log.error("weChat登录 --- 数据库中一个微信用户都没有,模拟用户失败");
							Logger.error("weChat登录 --- ++++++++++++++++模拟oxh64jkHZeWtbUYc2AMqDc0HiJZg登录失败");
						}
					}else{
						//跳转到登录口 后续增加
						LoginService.index();
					}
			}else{
				Logger.info("weChat登录 ---  创建新的session");
				String code = params.get("code");
				Logger.info("===========code=========== " + code);
				if(StringUtil.isNotEmpty(code)){
					 WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
					 if(null != wxServer){
						 String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxServer.appId+"&secret="+wxServer.appSecret+"&code="+code+"&grant_type=authorization_code";
						 Logger.info("weChat登录 ---   url"+url);
						 JSONObject json = HttpClientUtil.invoke(url, "POST", null);
						 Logger.info("weChat登录 ---  创建token:"+json);
						 if(null != json && json.containsKey("access_token")){
							 String accessToken = json.getString("access_token");
							 String openId = json.getString("openid");
							 wxUser = WxUser.getUserByOpenId(openId);
							 if(null == wxUser){
								 Logger.info("weChat登录 ---  用户不存在");
								 String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
								 json = HttpClientUtil.invoke(userUrl, "POST", null);
								 Logger.info("weChat登录 ---  创建用户信息:"+json);
								 if(null != json && json.containsKey("openid")){
									 Logger.info("weChat登录 ---  用户姓名"+json.getString("nickname"));
									 WxUser _wxUser = new WxUser();
									 _wxUser.wxOpenId = json.getString("openid");
									 _wxUser.nickName = json.getString("nickname");
									 _wxUser.sex = json.getString("sex");
									 _wxUser.sex = "1".equals(_wxUser.sex)?"男":"女";
									 _wxUser.language = json.getString("language");
									 _wxUser.city = json.getString("city");
									 _wxUser.province = json.getString("province"); 
									 _wxUser.country = json.getString("country");
									 _wxUser.headImgUrl = getWXSmallHeadImage(json.getString("headimgurl"));
									 _wxUser.isConcerned = "Y";
									 _wxUser.openIdChanncel="web_grant";
									 _wxUser.upOpenidTime= DateUtil.getNowDate();
									 _wxUser.userType="1";
									 _wxUser = WxUserBo.save(_wxUser);
									 _wxUser = WxUser.getFindByOpenId(openId);
									sessionInfo=new SessionInfo();
									sessionInfo.setWxUser(_wxUser);
					        		setSessionInfo(sessionInfo);
								 }
							 }else{
								 Logger.info("weChat登录 ---  用户存在");
								 sessionInfo=new SessionInfo();
								 sessionInfo.setWxUser(wxUser);
					        	 setSessionInfo(sessionInfo);
							 }
						 }
					 }
				}
			}
			}
		}else if("PC".equals(deviceFlag)){
			Logger.info("PC端登录 --- ");
			if(null == sessionInfo){
				String code = params.get("code");
				//第一步 得到应用id和秘钥
				WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
				if(null!=wxServer && StringUtil.isNotEmpty(code)){
					//第二步 得到token和 openid
					String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxServer.appletAppId+"&secret="+wxServer.appletAppSecret+"&code="+code+"&grant_type=authorization_code";
					JSONObject json = HttpClientUtil.invoke(url, "POST", null);
					if(null != json && !json.containsKey("errcode")){
						Logger.info("..............................."+json);
						String token = json.getString("access_token");
						String openId = json.getString("openid");
						wxUser = new WxUser();
						wxUser = WxUser.getUserByOpenId(openId);
						if(null == wxUser){
							url="https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openId;
							json = HttpClientUtil.invoke(url, "POST", null);
							if(null != json && json.containsKey("openid")){
								Logger.info("PC端登录 --- 用户信息....................."+json);
								 WxUser _wxUser = new WxUser();
								_wxUser.wxOpenId = json.getString("openid");
								_wxUser.nickName = json.getString("nickname");
								_wxUser.sex = json.getString("sex");
								_wxUser.sex = "1".equals(_wxUser.sex)?"男":"女";
								_wxUser.language = json.getString("language");
								_wxUser.city = json.getString("city");
								_wxUser.province = json.getString("province"); 
								_wxUser.country = json.getString("country");
								_wxUser.headImgUrl = getWXSmallHeadImage(json.getString("headimgurl"));
								_wxUser.isConcerned = "Y";
								_wxUser.openIdChanncel="web_grant";
								_wxUser.upOpenidTime= DateUtil.getNowDate();
								_wxUser.userType="2";
								_wxUser = WxUserBo.save(_wxUser);
								Map<String, String> condition = new HashMap<>();
								Map ret = WxUser.queryWxUserListByPage(condition, 1,100);
								List<WxUser> list = (List<WxUser>) ret.get("data");
								Logger.info("PC端登录 --- 用户保存信息....................1111."+list.size());
								_wxUser = WxUser.getFindByOpenId(openId);
								sessionInfo=new SessionInfo();
								sessionInfo.setWxUser(_wxUser);
				        		setSessionInfo(sessionInfo);
							}	
						}else{
							sessionInfo=new SessionInfo();
							sessionInfo.setWxUser(wxUser);
			        		setSessionInfo(sessionInfo);
						}
					}else{
						Logger.info("......超时登录");
					}
				}else{
					//session 为空并且未登录操作
					render("modules/xjldw/pc/scan_upload.html");
				}
			}
		}
//		log.debug("checkLogin开始检查登录状态");
//		String userAgent = request.headers.get("user-agent").value().toLowerCase();
//		String deviceFlag = params.get("flag");
//		boolean isNeedInterface =false;
//		SessionInfo sessionInfo=getSessionInfo();
//		WxUser 	wxUser = null;
//		
//		if(null != sessionInfo){
//			log.debug("当前session不为空");
//			wxUser = sessionInfo.getWxUser();
//			log.debug("当前用户:" + wxUser);
//			if(null !=wxUser){
//				isNeedInterface = false;
//				 wxUser = WxUser.getFindByOpenId(wxUser.wxOpenId);
//				 if(null != wxUser){
//					 sessionInfo.setWxUser(wxUser);
//			 		 Cache.add(MobileFilter.getSessionKey(), sessionInfo);
//			 		 log.debug("读取信息end");
//				 }else{
//					 sessionInfo=new SessionInfo();
//					 isNeedInterface=true;
//				 }
//			}
//		}else{
//			log.debug("当前session为空");
//			if(!isMobile(userAgent)){//pc
//				log.debug("当前浏览器不是手机，这里认为是pc");
//				if("testPC".equals(deviceFlag)){
//					log.debug("是pc上的测试标示testPC");
//					wxUser = WxUser.getFindByOpenId(null);
//					if(null != wxUser){
//						log.debug("使用模拟用户加入到session中，昵称是:" + wxUser.nickName);
//						sessionInfo=new SessionInfo();
//		        		sessionInfo.setWxUser(wxUser);
//		        		setSessionInfo(sessionInfo);
//						isNeedInterface = false;
//					}else{
//						log.error("数据库中一个微信用户都没有,模拟用户失败");
//						Logger.error("++++++++++++++++模拟oxh64jkHZeWtbUYc2AMqDc0HiJZg登录失败");
//					}
//				}else{
//					//跳转到登录口 后续增加
//					LoginService.index();
//				}
//			}else{
//				sessionInfo=new SessionInfo();
//				isNeedInterface=true;
//			}
//		}
//		log.debug("isNeedInterface状态:"+isNeedInterface);
	}
	
	/**
	 * 得到微信头像，使用小图
	 * @param headImageUrl
	 * @return
	 */
	private static String getWXSmallHeadImage(String headImageUrl){
		if (headImageUrl.endsWith("/0")){
			return headImageUrl.substring(0, headImageUrl.length()-2)+"/132";
		} else {
			return headImageUrl;
		}
	}
	private static boolean isMobile(String userAgent) {
		if (userAgent != null) {
			if (userAgent.indexOf("micromessenger") >= 0) {
				return true;
			} else if (userAgent.indexOf("pad") >= 0) {
				return true;
			} else if (userAgent.indexOf("android") >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 得到微信用户，同时从缓存中获取微信登录用户信息
	 * @return
	 */
	public static SessionInfo getSessionInfo() {
		String userKey = getSessionKey();
		log.debug("userKey:"+userKey);
		SessionInfo sessionInfo = null;
		if (Cache.get(userKey)!=null) {
			sessionInfo = (SessionInfo) Cache.get(userKey);
		}
		return sessionInfo;
	}
	public static WxUser getWXUser() {
		SessionInfo sessionInfo = getSessionInfo();
		if (sessionInfo!=null) {
			WxUser wxUser=sessionInfo.getWxUser();
			log.debug("wxUser信息 openId:"+wxUser.wxOpenId + " nickName:"+wxUser.nickName);
			return wxUser;
		}
		else{
			log.debug("session为空");
        	nok(Messages.get("appletSessionBeOverdue"));
		}
		return null;
	}
	private static void setSessionInfo(SessionInfo sessionInfo){
		Cache.set(getSessionKey(), sessionInfo);
	}
	public static String getSessionKey(){
		return session.getId()+"_userkey";
	}
	public static void main(String[] args) {
		System.out.println(MobileFilter.getWXSmallHeadImage("http://wx.qlogo.cn/mmopen/0wRpPfN90ibAwzs8Tsvm1T9dia4kdMEWIHqCsYR3IomWSSVtCPvXHk0gSMsLibypxRmuXEA1HROlVWZUa3vE031bU1dBs26cyKT/0"));
	}
	
}

 