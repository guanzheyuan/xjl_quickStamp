package controllers.modules.mobile.bo;

import models.modules.mobile.WxServer;
import utils.DateUtil;
import utils.SeqUtil;
import utils.StringUtil;
/**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-09-14 下午03:07:57
 * @describe  类说明
*/
public class WxServerBo {
	// 增加、编辑
	public static WxServer save(WxServer wxServer) {
		if (wxServer.wxServerId != null) {
		}
		if (wxServer.wxServerId == null) {
			wxServer.wxServerId = SeqUtil.maxValue("wx_server", "wx_server_id");
			wxServer.status = "0AA";
			wxServer.createTime = DateUtil.getNowDate();
		}
		wxServer = wxServer.save();
		return wxServer;
	}
	// 删除
	public static WxServer del(WxServer wxServer) {
		if (wxServer != null) {
			wxServer.status = "0XX";
			wxServer = wxServer.save();
			return wxServer;
		}
		return null;
	}
	
	/***
	 * 根据公众号的原始ID查找
	 * @param wxCode 原始ID
	 * @return
	 */
	public static WxServer readWxServerByWxCode(String wxCode){
		WxServer wxServer = null;
		if(StringUtil.isNotEmpty(wxCode)){
			wxServer = WxServer.find("from WxServer where status='0AA' and wxCode=?", wxCode).first();
		}
		return wxServer;
	}
}