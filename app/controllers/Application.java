package controllers;

import play.*;
import play.cache.Cache;
import play.mvc.*;
import java.util.*;
import controllers.modules.mobile.filter.MobileFilter;
import models.*;
import models.modules.mobile.WxServer;
public class Application extends Controller {
	private final static String WX_CODE = "gh_00e09da4fab0";
    public static void index() {
    	render("modules/xjldw/pc/scan_upload.html");
    }  
    public static void mIndex() {
    	WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
    	renderArgs.put("wxServer",wxServer);
    	renderArgs.put("flag",params.get("flag"));
    	render("modules/xjldw/pc/scan_upload.html");
    }
    
 // 根目录下的访问txt文件自动匹配到/public/txt/目录下对应的文件上
 	public static void txt() {
 		Logger.info("request.url1:"+request.url.split("sp")[0]);
 		Logger.info("request.url2:"+request.url.split("sp")[1]);
 		render("/public/txt" + request.url.split("sp")[1]);
 		//render("/public/txt" + request.url);
 	}

 	public static void timeout() {
 		render();
 	}
 	public static void login() {
 		
 	}
 	//微信绑定学生
 	public static void mlogin() {
 	}

}