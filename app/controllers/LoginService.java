package controllers;
import java.util.Map;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import controllers.comm.BOResult;
import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.filter.MobileFilter;
import utils.CommonValidateUtil;
import utils.SecurityUtil;
import utils.StringUtil;
import utils.SysParamUtil;
import controllers.modules.mobile.filter.MobileFilter;

public class LoginService extends BaseController {

	public static void index() {
		// pc登录入口
		render("modules/xjldw/pc/scan_upload.html");
	}

	public static void mIndex() {
		  render("modules/xjldw/mobile/main.html");
	}
	public static void addStudent() {
		  render("modules/xjldw/mobile/my/student_bind.html");
	}
	
	public static void login() {
		
	}
	public static void logout() {
		Cache.delete(MobileFilter.getSessionKey());
		index();
	}

	public static void mlogout() {
		Cache.delete(MobileFilter.getSessionKey());
		mIndex();
	}

	//忘记密码
	public static void forgetPwd(){
		String MOBILE_SYSTEM_NAME = SysParamUtil.getGlobalParamByMask("MOBILE_SYSTEM_NAME");
        renderArgs.put("MOBILE_SYSTEM_NAME", MOBILE_SYSTEM_NAME);
		String TECHNICAL_SUPPORT = SysParamUtil.getGlobalParamByMask("TECHNICAL_SUPPORT");
        renderArgs.put("TECHNICAL_SUPPORT", TECHNICAL_SUPPORT);
		render("modules/zzb/mobile/forgetPwd.html");
	}
	
	
	//家长根据微信号绑定学生
 	public static void bindStudent() {
 	}

 	 /**
     * 绑定老师页面
     */
    public static void teacherBind(){
	    render("modules/xjldw/mobile/my/teacher_bind.html");
    }
}
