package controllers.modules.mobile;
import java.util.HashMap;
import java.util.Map;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxServer;
import models.modules.mobile.WxUser;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Http;
import utils.StringUtil;
import utils.WxPushMsg;
public class Skip extends MobileFilter {
	private final static String WX_CODE = "gh_00e09da4fab0";
	
	
	public static void toLoginFilter(){
		WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
    	renderArgs.put("wxServer",wxServer);
    	renderArgs.put("flag",params.get("flag"));
		render("modules/xjldw/pc/scan_upload.html");
	}
	
	public static void toLoginMobileFilter(){
		renderArgs.put("flag",params.get("flag"));
		render("modules/xjldw/pc/scan_upload.html");
	}
	/**
	 * 跳转预览试图
	 */
	public static void toPreview(){
		render("modules/xjldw/pc/preview.html");
	}
	
	/**
	 * 跳转到打印设置页面
	 */
	public static void toStampConfig(){
		renderArgs.put("fileId",params.get("paramId"));
		render("modules/xjldw/pc/stamp_config.html");
	}
	
	/**
	 * 跳转到支付页面
	 */
	public static void toStampPayMent(){
		renderArgs.put("setId",params.get("paramId"));
		render("modules/xjldw/pc/pay_ment.html");
	}
	
	/**
	 * 跳转到个人中心
	 */
	public static void toOrder(){
		render("modules/xjldw/pc/order.html");
	}
	
	
	
  /** -----------------------------------------闪冲-----------------------------------------------------**/
	
	public static void toRush(){
		render("modules/xjldw/rush/sc_rush.html");
	}
	public static void toRushChat(){
		renderArgs.put("status",params.get("status"));
		render("modules/xjldw/rush/sc_chat.html");
	}
	public static void toRushChat1(){
		render("modules/xjldw/rush/sc_chat1.html");
	}
	public static void toRushWater(){
		renderArgs.put("time", params.get("time"));
		render("modules/xjldw/rush/sc_waterList.html");
	}
	public static void toScLog(){
		render("modules/xjldw/rush/sc_log.html");
	}
	public static void toScMaintain(){
		render("modules/xjldw/rush/sc_maintain.html");
	}
	public static void toScConfig(){
		render("modules/xjldw/rush/sc_config.html");
	}
}
