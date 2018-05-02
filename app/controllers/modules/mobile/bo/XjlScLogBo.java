package controllers.modules.mobile.bo;

import models.modules.mobile.XjlScLog;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlScLogBo {
	
	public static XjlScLog save(XjlScLog xjlScLog){
		xjlScLog.id = SeqUtil.maxValue("xjl_sc_log","id");
		xjlScLog.status ="0AA";
		xjlScLog.createTime = DateUtil.getNowDate();
		xjlScLog = xjlScLog.save();
		return xjlScLog;
	}
}
