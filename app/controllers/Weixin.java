package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.ivy.Main;
import org.json.JSONException;
import org.json.XML;
import models.modules.mobile.WxServer;
import models.modules.mobile.XjlDwFile;
import models.modules.mobile.XjlStampSet;
import net.sf.json.JSONObject;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Http.Request;
import utils.EncoderHandler;
import utils.HttpClientUtil;
import utils.MD5Encoder;
import utils.MatrixToImageWriterWithLogo;
import utils.SysParamUtil;
import utils.UrlHelper;
import utils.WXPayConstants.SignType;
import utils.WXPayUtil;
import controllers.comm.BaseController;
import controllers.modules.weixin.utils.AccessTokenHolder;
import controllers.modules.weixin.utils.WXRequestAddr;

public class Weixin extends BaseController {

	
	private static String API_KEY="79159FC319A347D391A5E8F0B974951E";
	private static String SERVICE_CODE = "gh_00e09da4fab0";
	public static void getWxSdkInfo(Long vnoId) {
		String url = params.get("url");
		String openId = params.get("openId");

		Logger.info(" +++getWxSdkInfo vnoId =%s ", vnoId);
		Logger.info(" +++getWxSdkInfo openId =%s ", openId);

		WxServer server = WxServer.findById(1l);

		String appId = server.appId;
		Logger.info(" +++getWxSdkInfo appId =%s ", appId);

		String timestamp = create_timestamp();
		String nonce = create_nonce_str();
		String accessToken = AccessTokenHolder.getAccessTokenByAppId(appId);

		String jsapi_ticket = (String) Cache.get(appId);

		if (jsapi_ticket == null) {
			String getTicket = String.format(WXRequestAddr.POST_JSAPI_TICKET,
					accessToken);
			JSONObject json = HttpClientUtil.invoke(getTicket, "POST", null);
			// 去SHA1 散列值
			jsapi_ticket = json.getString("ticket");
			Cache.set(appId, jsapi_ticket, "1h");
		}

		String[] paramArr = new String[] { "jsapi_ticket=" + jsapi_ticket,
				"timestamp=" + timestamp, "noncestr=" + nonce, "url=" + url };
		Arrays.sort(paramArr);
		// 将排序后的结果拼接成一个字符串
		String content = paramArr[0].concat("&" + paramArr[1])
				.concat("&" + paramArr[2]).concat("&" + paramArr[3]);

		//
		// String getSinature = "jsapi_ticket=" + jsapi_ticket + "&noncestr="
		// + nonce + "&timestamp=" + timestamp + "&url=" + url;
		// Logger.info("getSinature = " + content);

		// String sortStr = CheckSignature.getSortString(timestamp, nonce,
		// token);
		// 去SHA1 散列值
		String signature = EncoderHandler.encode("SHA1", content);

		// Logger.info("jsapi_ticket = " + jsapi_ticket);
		// Logger.info("signature = " + signature);
		// Logger.info("timestamp = " + timestamp);
		// Logger.info("nonce = " + nonce);
		 Logger.info("url = " + url);
		HashMap map = new HashMap();
		map.put("appId", appId);
		map.put("signature", signature);
		map.put("timestamp", timestamp);
		map.put("nonce", nonce);
		map.put("jsapi_ticket", jsapi_ticket);
		map.put("url", url);
		renderJSON(map);
		// renderTemplate("/weixin.html", appId, signature, timestamp, nonce);
	}

	public static void doUnifiedOrder(){
		 String UTF8 = "UTF-8";
	        String nonce_str=UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	        WxServer wxServier = WxServer.getServerByServerid("gh_00e09da4fab0");
	        String openId = params.get("openId");
	        String setId = params.get("paramId");
	        if(null != wxServier){
	        	XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(setId));
	        	if(null != xjlStampSet){
	        		String total_free = String.valueOf( Math.round(Double.parseDouble(xjlStampSet.totalPrice)*100));
	        		String addressIp = UrlHelper.getIpAddr();
	        		Map<String,String> _wxCon = new HashMap<>();
	        		_wxCon.put("appid",wxServier.appId);
	        		_wxCon.put("attach","test");
	        		_wxCon.put("body","测试商家-商品类目");
	        		_wxCon.put("device_info","WEB");
	        		_wxCon.put("fee_type","CNY");
	        		_wxCon.put("mch_id","1496505462");
	        		_wxCon.put("nonce_str",nonce_str);
	        		_wxCon.put("notify_url","http://dw201709.com/sp");
	        		_wxCon.put("openid",openId);
	        		_wxCon.put("out_trade_no",xjlStampSet.outTradeNo);
	        		_wxCon.put("spbill_create_ip",addressIp);
	        		_wxCon.put("total_fee",total_free);
	        		_wxCon.put("trade_type","JSAPI");
	        	}
	        }
	}
	
	public static void queryOrder() throws Exception{
		 String UTF8 = "UTF-8";
	     String nonce_str=UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	     WxServer wxServier = WxServer.getServerByServerid(SERVICE_CODE);
	     String openId = params.get("openId");
	     String setId = params.get("paramId");
	     if(null != wxServier){
	    	 XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(setId));
	    	 if(null != xjlStampSet){
	    		 String total_free = String.valueOf( Math.round(Double.parseDouble(xjlStampSet.totalPrice)*100));
	        	 String addressIp = UrlHelper.getIpAddr();
	        	 Map<String,String> _wxCon = new HashMap<>();
	        	 _wxCon.put("appid",wxServier.appId);
        		 _wxCon.put("mch_id","1496505462");
        		 _wxCon.put("nonce_str",nonce_str);
        		 _wxCon.put("out_trade_no",xjlStampSet.outTradeNo);
        	     String sign = WXPayUtil.generateSignature(_wxCon,API_KEY,SignType.MD5);
        	     _wxCon.put("sign",sign);
	        	 String reqBody = WXPayUtil.mapToXml(_wxCon);
	        	 URL httpUrl = new URL("https://api.mch.weixin.qq.com/pay/orderquery");
	 	         HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
	 	         httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
	 	         httpURLConnection.setDoOutput(true);
	 	         httpURLConnection.setRequestMethod("POST");
	 	         httpURLConnection.setConnectTimeout(10*1000);
	 	         httpURLConnection.setReadTimeout(10*1000);
	 	         httpURLConnection.connect();
	 	         OutputStream outputStream = httpURLConnection.getOutputStream();
	 	         outputStream.write(reqBody.getBytes(UTF8));
	 	        //获取内容
	 	         InputStream inputStream = httpURLConnection.getInputStream();
	 	         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
	 	         final StringBuffer stringBuffer = new StringBuffer();
	 	         String line = null;
	 	         while ((line = bufferedReader.readLine()) != null) {
	 	            stringBuffer.append(line);
	 	         }
	 	         String resp = stringBuffer.toString();
	 	         if (stringBuffer!=null) {
	 	            try {
	 	                bufferedReader.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	         }
	 	         if (inputStream!=null) {
	 	            try {
	 	                inputStream.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	         }
	 	         if (outputStream!=null) {
	 	            try {
	 	                outputStream.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	        }
	 	        org.json.JSONObject xmlJSONObj = XML.toJSONObject(resp);  
	 	        org.json.JSONObject jsonObject =  xmlJSONObj.getJSONObject("xml");
	 	        Map<String,Object> map = new HashMap<>();
	 	        if(null != jsonObject){
	 	        	map.put("trade_state",jsonObject.getString("trade_state"));
	 	        	map.put("trade_state_desc",jsonObject.getString("trade_state_desc"));
	 	        }
	 	       ok(map);
	    	 }
	     }
	}
	public static void wxQrCode() throws Exception{
		 String UTF8 = "UTF-8";
	     String nonce_str=UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	     WxServer wxServier = WxServer.getServerByServerid(SERVICE_CODE);
	     String openId = params.get("openId");
	     String setId = params.get("paramId");
	     if(null != wxServier){
	    	 XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(setId));
	    	 if(null != xjlStampSet){
	    		 XjlDwFile xjlDwFile = XjlDwFile.queryXjlDwFileById(xjlStampSet.fileId);
	    		 String total_free = String.valueOf( Math.round(Double.parseDouble(xjlStampSet.totalPrice)*100));
	        	 String addressIp = UrlHelper.getIpAddr();
	        	 Map<String,String> _wxCon = new HashMap<>();
	        	 _wxCon.put("appid",wxServier.appId);
        		 _wxCon.put("attach","test");
        		 _wxCon.put("body",xjlDwFile.fileName);
        		 _wxCon.put("device_info","WEB");
        		 _wxCon.put("fee_type","CNY");
        		 _wxCon.put("mch_id","1496505462");
        		 _wxCon.put("nonce_str",nonce_str);
        		 _wxCon.put("notify_url","http://dw201709.com/sp");
        		 //_wxCon.put("openid",openId);
        		 _wxCon.put("out_trade_no",xjlStampSet.outTradeNo);
        		 _wxCon.put("spbill_create_ip",addressIp);
        		 _wxCon.put("total_fee",total_free);
        		 _wxCon.put("trade_type","NATIVE");
        	     String sign = WXPayUtil.generateSignature(_wxCon,API_KEY,SignType.MD5);
        	     _wxCon.put("sign",sign);
	        	 String reqBody = WXPayUtil.mapToXml(_wxCon);
	        	 URL httpUrl = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
	 	         HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
	 	         httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
	 	         httpURLConnection.setDoOutput(true);
	 	         httpURLConnection.setRequestMethod("POST");
	 	         httpURLConnection.setConnectTimeout(10*1000);
	 	         httpURLConnection.setReadTimeout(10*1000);
	 	         httpURLConnection.connect();
	 	         OutputStream outputStream = httpURLConnection.getOutputStream();
	 	         outputStream.write(reqBody.getBytes(UTF8));
	 	        //获取内容
	 	         InputStream inputStream = httpURLConnection.getInputStream();
	 	         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
	 	         final StringBuffer stringBuffer = new StringBuffer();
	 	         String line = null;
	 	         while ((line = bufferedReader.readLine()) != null) {
	 	            stringBuffer.append(line);
	 	         }
	 	         String resp = stringBuffer.toString();
	 	         if (stringBuffer!=null) {
	 	            try {
	 	                bufferedReader.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	         }
	 	         if (inputStream!=null) {
	 	            try {
	 	                inputStream.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	         }
	 	         if (outputStream!=null) {
	 	            try {
	 	                outputStream.close();
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            }
	 	        }
	 	        org.json.JSONObject xmlJSONObj = XML.toJSONObject(resp);  
	 	        org.json.JSONObject jsonObject =  xmlJSONObj.getJSONObject("xml");
	 	       Map<String,String> map = new HashMap<>();
	 	       Logger.info("......"+jsonObject);
	 	        if(null != jsonObject){
	 	        	if("SUCCESS".equals(jsonObject.get("result_code"))){
	 	        		 String urlCode = jsonObject.getString("code_url");
	 	        		 // 生成微信二维码，输出到response流中
	 	                //String icon = Weixin.class.getClassLoader().getResource("coffee_icon.png").getPath();
	 	        		 String icon = Play.roots.get(0).child("public").child("images").child("pc").getRealFile().getAbsolutePath()
	 	        				 +File.separator+"logo.png";
	 	                BufferedImage bufferedImage = MatrixToImageWriterWithLogo.genBarcode(urlCode, 512, 512, icon); // 二维码的内容，宽，高，二维码中心的图片地址
	 	                Calendar ca = Calendar.getInstance();
	 	      		    int year = ca.get(Calendar.YEAR);
	 	                String savePath = Play.roots.get(0).child("public").child("tmp").child("pay").getRealFile().getAbsolutePath()
	 	      				+File.separator+year+File.separator+openId+File.separator;
		 	      		File folder = new File(savePath);
		 	      	    if(!folder.exists()){
		 	      	    	Logger.info("dir not exists, create it ......");
		 	      			boolean flag = folder.mkdirs();
		 	      			Logger.info("result:"+flag);
		 	      		} 
	 	                ImageIO.write(bufferedImage, "jpg", new File(savePath+openId+".jpg"));
	 	                map.put("url", savePath+openId+".jpg");
	 	                map.put("result_code","true");
	 	                ok(map);
	 	        	}else{
	 	        		map.put("result_code", String.valueOf(jsonObject.get("result_code")));
	 	        		map.put("err_code_des",String.valueOf(jsonObject.get("err_code_des")));
	 	        		ok(map);
	 	        	}
	 	        }
	    	 }
	     }
	}
	public static void getWXPay() throws Exception{
	        String UTF8 = "UTF-8";
	        String nonce_str=UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	        WxServer wxServier = WxServer.getServerByServerid(SERVICE_CODE);
	        String openId = params.get("openId");
	        String setId = params.get("paramId");
	        if(null != wxServier){
	        	XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(setId));
	        	if(null != xjlStampSet){
	        		String total_free = String.valueOf( Math.round(Double.parseDouble(xjlStampSet.totalPrice)*100));
	        		String addressIp = UrlHelper.getIpAddr();
	        		Map<String,String> _wxCon = new HashMap<>();
	        		_wxCon.put("appid",wxServier.appId);
	        		_wxCon.put("attach","test");
	        		_wxCon.put("body","上品闪印-打印");
	        		_wxCon.put("device_info","WEB");
	        		_wxCon.put("fee_type","CNY");
	        		_wxCon.put("mch_id","1496505462");
	        		_wxCon.put("nonce_str",nonce_str);
	        		_wxCon.put("notify_url","http://dw201709.com/sp");
	        		_wxCon.put("openid",openId);
	        		_wxCon.put("out_trade_no",xjlStampSet.outTradeNo);
	        		_wxCon.put("spbill_create_ip",addressIp);
	        		_wxCon.put("total_fee",total_free);
	        		_wxCon.put("trade_type","JSAPI");
	        		String sign = WXPayUtil.generateSignature(_wxCon,API_KEY,SignType.MD5);
		        	 _wxCon.put("sign",sign);
		        	 String reqBody = WXPayUtil.mapToXml(_wxCon);
		        	 Logger.info(".................reqBody"+reqBody);
		 	        URL httpUrl = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
		 	        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		 	        httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
		 	        httpURLConnection.setDoOutput(true);
		 	        httpURLConnection.setRequestMethod("POST");
		 	        httpURLConnection.setConnectTimeout(10*1000);
		 	        httpURLConnection.setReadTimeout(10*1000);
		 	        httpURLConnection.connect();
		 	        OutputStream outputStream = httpURLConnection.getOutputStream();
		 	        outputStream.write(reqBody.getBytes(UTF8));
		 	      //获取内容
		 	        InputStream inputStream = httpURLConnection.getInputStream();
		 	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
		 	        final StringBuffer stringBuffer = new StringBuffer();
		 	        String line = null;
		 	        while ((line = bufferedReader.readLine()) != null) {
		 	            stringBuffer.append(line);
		 	        }
		 	        String resp = stringBuffer.toString();
		 	        if (stringBuffer!=null) {
		 	            try {
		 	                bufferedReader.close();
		 	            } catch (IOException e) {
		 	                e.printStackTrace();
		 	            }
		 	        }
		 	        if (inputStream!=null) {
		 	            try {
		 	                inputStream.close();
		 	            } catch (IOException e) {
		 	                e.printStackTrace();
		 	            }
		 	        }
		 	        if (outputStream!=null) {
		 	            try {
		 	                outputStream.close();
		 	            } catch (IOException e) {
		 	                e.printStackTrace();
		 	            }
		 	        }
		 	        org.json.JSONObject xmlJSONObj = XML.toJSONObject(resp);  
		 	        org.json.JSONObject jsonObject =  xmlJSONObj.getJSONObject("xml");
		 	        Map<String,String> map = new HashMap<>();
		 	        if(null != jsonObject){
		 	        	if("SUCCESS".equals(jsonObject.get("result_code"))){
		 	        		  Logger.info(".................ResultSet"+jsonObject);
		 	        		map.put("appId",wxServier.appId);
		 	        		map.put("nonceStr",String.valueOf(jsonObject.get("nonce_str")));
		 	        		map.put("package","prepay_id="+jsonObject.get("prepay_id"));
		 	        		map.put("signType","MD5");
		 	        		map.put("timeStamp", create_timestamp());
		 	        		sign = WXPayUtil.generateSignature(map,API_KEY,SignType.MD5);
			 	        	map.put("paySign",sign);
		 	        	}else{
		 	        		map.put("result_code", String.valueOf(jsonObject.get("result_code")));
		 	        		map.put("err_code_des",String.valueOf(jsonObject.get("err_code_des")));
		 	        	}
		 	        	
		 	        }
		 	       renderJSON(map);
	        	}
	        }
	}
	public static String getRequestXml(SortedMap<Object,Object> parameters){  
        StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        Set es = parameters.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = String.valueOf(entry.getKey());  
            String v = String.valueOf(entry.getValue());  
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {  
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");  
            }else {  
                sb.append("<"+k+">"+v+"</"+k+">");  
            }  
        }  
        sb.append("</xml>");  
        return sb.toString();  
    }  
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	private static String create_nonce_str() {
		return "Wm3WZYTPz0wzccnW";
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	public static void main(String[] args) {
		System.out.println(Long.toString(System.currentTimeMillis() / 1000));
	}
	
}
