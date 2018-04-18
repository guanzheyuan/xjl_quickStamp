package models.modules.weixin;

import java.util.Date;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-29 上午9:55:13
 * @describe  类说明
 */
public class AccessToken {
	public AccessToken(Date rDate,String access_token){
		requestDate = rDate;
		accessToken = access_token;
	}

	private Date requestDate;
	private String accessToken;
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
 