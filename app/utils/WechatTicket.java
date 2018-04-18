package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.Gson;

import controllers.comm.Sign;
import models.modules.mobile.WxServer;
import net.sf.json.JSONObject;
import play.Logger;

public class WechatTicket {
	
	//临时整型参数
	private final static String QR_SCENE = "QR_SCENE";
	//临时字符串参数
	private final static String QR_STR_SCENE = "QR_STR_SCENE";
	//永久整型参数
	private final static String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";
	//永久字符串参数
	private final static String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
	//创建二维码
	private static  String CREATE_TICKETPATH = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
	//通过ticket 换取二维码
	private static   String SHOWQRCODE_PATH = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
	//公众号唯一校验码
	private final static String WX_CODE = "gh_00e09da4fab0";
	public static String createQrCode(){
	      String qrCode = "";
		  //得到公众号安全秘钥
		  WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
		  Logger.info("得到公众号安全秘钥");
		  if(null != wxServer){
			  //通过安全秘药得到token
			  String accessToken = Sign.getAccessToken(wxServer.appId,wxServer.appSecret,false);
			  Logger.info("得到token"+accessToken);
			  if(StringUtil.isNotEmpty(accessToken)){
		    	  //组装参数
		    	  TreeMap<String,String> params = new TreeMap<String,String>();
		    	  params.put("access_token",accessToken);
		    	  params.put("action_name",QR_LIMIT_STR_SCENE);
		    	  Map<String,Integer> intMap = new HashMap<String,Integer>();
		    	  Random random = new Random();
		    	  int rand = random.nextInt(1000)%(1000-1+1) + 1;
		    	  intMap.put("scene_id",rand);  
		    	  Map<String,Map<String,Integer>> mapMap = new HashMap<String,Map<String,Integer>>();  
		          mapMap.put("scene", intMap);  
		          
		          Map<String,Object> paramsMap = new HashMap<String,Object>();  
		          paramsMap.put("action_name", QR_LIMIT_SCENE);
		          paramsMap.put("action_info", mapMap);
		          String data = new Gson().toJson(paramsMap); 
		          Logger.info("参数："+data);
		          data = HttpRequestUtil.HttpDefaultExecute(HttpRequestUtil.POST_METHOD, CREATE_TICKETPATH, params, data);
		      	  //JSONObject json = HttpClientUtil.invoke(CREATE_TICKETPATH,"post",JSONObject.fromObject(paramsMap));
		          JSONObject json = JSONObject.fromObject(data);
		      	  Logger.info("........................."+ json);
		      	 if(null != json){
		      		String ticket = json.getString("ticket");
		      		if(StringUtil.isNotEmpty(ticket)){
		      			qrCode = SHOWQRCODE_PATH+"?ticket="+ticket;
		      		}
		      	 }
		      }
		  }
		  return qrCode;
	}
}
