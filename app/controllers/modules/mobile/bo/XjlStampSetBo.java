package controllers.modules.mobile.bo;

import models.modules.mobile.XjlStampSet;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlStampSetBo {

	public static XjlStampSet save(XjlStampSet xjlStampSet){
		xjlStampSet.setId = SeqUtil.maxValue("xjl_stamp_set","set_id");
		xjlStampSet.status ="0AA";
		xjlStampSet.createTime = DateUtil.getNowDate();
		
		xjlStampSet = xjlStampSet.save();
		return xjlStampSet;
	}
}
