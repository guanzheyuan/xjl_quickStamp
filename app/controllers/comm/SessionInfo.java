package controllers.comm;
import java.io.Serializable;

import models.modules.mobile.WxUser;
public class SessionInfo implements Serializable {
	
	private String deviceFlag;
	private WxUser wxUser;

	public WxUser getWxUser() {
		return wxUser;
	}
	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}
	public String getDeviceFlag() {
		return deviceFlag;
	}

	public void setDeviceFlag(String deviceFlag) {
		this.deviceFlag = deviceFlag;
	}
}
