package models.modules.weixin;

import java.util.Date;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-5-17 下午10:50:01
 * @describe  类说明
 */
public class AppletSessionKey {
	public AppletSessionKey(Date rDate,String session_key){
		requestDate = rDate;
		sessionKey = session_key;
	}
	private Date requestDate;
	private String sessionKey;
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}
 