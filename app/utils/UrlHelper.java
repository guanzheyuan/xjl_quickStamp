package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import play.Logger;
import play.mvc.Http.Request;


public class UrlHelper {
	/**
	 * 连接分页参数,组成一个字符串
	 * @param name
	 * @param values
	 * @return 参数字符
	 */
	static String buildPair(String name, String[] values) {
		StringBuffer buffer = new StringBuffer();
   		buffer.append(name).append("=").append(values[0]);
		return buffer.toString();
	}
    /**
     * 重写分页url,包含参数     
     * @param params
     * @return 分页url
     */
	public static String buildUrlParams(Map<String,String []> params) {
		if (params == null || params.size() < 1) {
			return "";
		} else {
			StringBuffer suffix = new StringBuffer();
			Iterator iter = params.keySet().iterator();
			while (iter.hasNext()) {
				String name = iter.next().toString();
				if("action".equals(name)||"controller".equals(name)||"page".equals(name)) continue;
				String[] values = params.get(name);
				suffix.append(buildPair(name, values));
				suffix.append("&");
			}
			String result =  suffix.toString();
			return result;
		}
	}

    public static Map<String, Object> getParams(Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Iterator<String> iter = params.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if("page".equals(key)) continue;
            result.put(key, params.get(key));
        }
        return result;
    }
    /**
	* 获取客户端IP 
	* @param request
	* @return
	*/
	public static String getIpAddr() {
		Object ip = Request.current().headers.get("X-Forwarded-For".toLowerCase());
        if (ip == null  || "unknown".equalsIgnoreCase(ip.toString())) {
            ip = Request.current().headers.get("Proxy-Client-IP".toLowerCase());
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.toString())) {
            ip = Request.current().headers.get("WL-Proxy-Client-IP".toLowerCase());
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.toString())) {
            ip = Request.current().headers.get("HTTP_CLIENT_IP".toLowerCase());
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.toString())) {
            ip = Request.current().headers.get("HTTP_X_FORWARDED_FOR".toLowerCase());
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip.toString())) {
            ip = Request.current().remoteAddress;
            //Logger.error(">>>>>>>>>>>>>>>>>>>>>没有取到IP地址，user-agent："+Request.current().headers.get("user-agent").toString());
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }else{
        	if(ip.toString().contains("[")&&ip.toString().contains("]")){
        		ip=ip.toString().substring(1,ip.toString().length()-1);	
        	}
        }
        return ip.toString();
    }
	/**
	* 获取客户端浏览器
	* @param request
	* @return
	*/
	public static String getBrowser() {
		String OsObject = "未知";
		String userAgent = Request.current().headers.get("user-agent").toString().toLowerCase();
        if(userAgent.indexOf(".NET")>0||userAgent.indexOf("MSIE")>0) {  
     	   OsObject = "IE";
        }
        else if(userAgent.indexOf("chrome")>0){  
     	   OsObject = "Chrome";  
        }
        else if(userAgent.indexOf("qqbrowser")>0){  
     	   OsObject = "QQBrowser";  
        }
        else if(userAgent.indexOf("firefox")>0){  
     	   OsObject = "Firefox";  
        }
        else if(userAgent.indexOf("iemobile")>0){  
     	   OsObject = "IEMobile";
        }
        else if(userAgent.indexOf("ucbrowser")>0){  
     	   OsObject = "UC";  
        }
        else if(userAgent.indexOf("safari")>0) {  
     	   OsObject = "Safari";  
        }
        else if(userAgent.indexOf("camino")>0){  
     	   OsObject = "Camino";  
        }
        else if(userAgent.indexOf("gecko")>0){  
     	   OsObject = "Gecko";  
        }
        return OsObject;
    }
}