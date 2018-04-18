package models.modules.weixin;

import java.util.Date;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-29 上午9:55:34
 * @describe  类说明
 */
public class JsapiTicket {
	public JsapiTicket(Date rDate,String JTicket){
		this.requestDate = rDate;
		this.Jsapi_Ticket = JTicket;
	}
	//请求Jsapi_Ticket的时间
	private Date requestDate;
	//从微信上请求返回的jsapi_ticket
	private String Jsapi_Ticket;
	
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getJsapi_Ticket() {
		return Jsapi_Ticket;
	}
	public void setJsapi_Ticket(String jsapi_Ticket) {
		Jsapi_Ticket = jsapi_Ticket;
	}
	public String toString(){
		return "requestDate:" + requestDate+"|Jsapi_Ticket:"+Jsapi_Ticket;
	}
}
 