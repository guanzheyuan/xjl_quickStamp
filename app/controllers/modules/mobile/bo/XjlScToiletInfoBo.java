package controllers.modules.mobile.bo;
import java.util.UUID;

import models.modules.mobile.XjlScToiletInfo;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlScToiletInfoBo {

	
	public static XjlScToiletInfo save(XjlScToiletInfo xjlScToiletInfo){
		xjlScToiletInfo.id=SeqUtil.maxValue("xjl_sc_toiletinfo","id");
		xjlScToiletInfo.createTime = DateUtil.getNowDate();
		xjlScToiletInfo.status = "1";
		xjlScToiletInfo.deviceCode = UUID.randomUUID().toString();
		xjlScToiletInfo = xjlScToiletInfo.save();
		return xjlScToiletInfo;
	}
	
	
	
}
