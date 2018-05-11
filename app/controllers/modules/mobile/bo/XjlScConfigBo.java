package controllers.modules.mobile.bo;

import models.modules.mobile.XjlScConfig;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlScConfigBo {

	
	public static XjlScConfig save(XjlScConfig xjlScConfig){
		xjlScConfig.id = SeqUtil.maxValue("xjl_sc_config","id");
		xjlScConfig.status ="0AA";
		xjlScConfig.createTime = DateUtil.getNowDate();
		xjlScConfig = xjlScConfig.save();
		return xjlScConfig;
	}
}
