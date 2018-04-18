package controllers.modules.mobile.bo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import play.Logger;
import controllers.comm.BOResult;
import controllers.comm.SessionInfo;
import models.modules.mobile.WxUser;
import utils.DateUtil;
import utils.SeqUtil;
import utils.StringUtil;
/**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-09-14 下午02:43:11
 * @describe  类说明
*/
public class WxUserBo {
	// 增加、编辑
	public static WxUser save(WxUser wxUser) {
		if (wxUser.wxOpenId != null) {
		}
		if (wxUser.createTime == null) {
			wxUser.status = "0AA";
			wxUser.createTime = DateUtil.getNowDate();
			wxUser.upOpenidTime = DateUtil.getNowDate();
		}
		wxUser = wxUser.save();
		return wxUser;
	}
	// 删除
	public static WxUser del(WxUser wxUser) {
		if (wxUser != null) {
			wxUser.status = "0XX";
			wxUser = wxUser.save();
			return wxUser;
		}
		return null;
	}
	/***
	 * 判断一个人是否关注过某过公众号，在我们系统中存有记得
	 * @param openid 企业标识
	 * @param vnoId 商户标识
	 * @return 
	 */
	public static WxUser readWxUserByOpenIdAndSchoolId(String openid,Long schoolId){
		WxUser wxUser = null;
		if(StringUtil.isNotEmpty(openid)&&schoolId!=null){
			Object [] paramsObject = {openid,schoolId};
			wxUser = WxUser.find("from WxUser where status='0AA' and wxOpenId=? and schoolId=? ", paramsObject).first();
		}
		return wxUser;
	}
	public static BOResult Login(String usercode, String passwd, String openid) {
		BOResult bOResult = new BOResult();
		WxUser user = WxUser.find("from WxUser where status='0AA' and wxPhone=?", usercode).first();
		if (user != null) {
			//Long userId = user.userId;
			String pwd = user.userPwd;
			String md5pwd = null;
			try {
				//md5pwd = SecurityUtil.base64AndMD5(userId.toString() + passwd);
				md5pwd = passwd;
			} catch (Exception e) {
				Logger.error(e, "password validate fail.");
			}
			if (pwd.equals(md5pwd)) {
				SessionInfo sessionInfo = new SessionInfo();
				sessionInfo.setWxUser(user);
				bOResult.addValue(sessionInfo);
			} else {
				bOResult.setError("-1", "用户或者密码错误");
			}

		} else {
			bOResult.setError("-1", "该用户不存在");
		}
		return bOResult;
	}
}