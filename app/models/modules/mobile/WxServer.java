package models.modules.mobile;
import java.sql.*;
import java.util.Date;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;


import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "wx_server")
public class WxServer extends GenericModel{

	@Id
	@Column(name = "WX_SERVER_ID")
	public Long wxServerId;

	@Column(name = "WX_CODE")
	public String wxCode;

	@Column(name = "WX_NAME")
	public String wxName;

	@Column(name = "WX_QR_CODE")
	public String wxQrCode;

	@Column(name = "APP_ID")
	public String appId;

	@Column(name = "APP_SECRET")
	public String appSecret;

	@Column(name = "PAY_KEY")
	public String payKey;

	@Column(name = "MCH_ID")
	public String mchId;

	@Column(name = "ACCESS_TOKEN")
	public String accessToken;

	@Column(name = "KEY_VALUE")
	public String keyValue;

	@Column(name = "TOKEN")
	public String token;

	@Column(name = "APPLET_NAME")
	public String appletName;

	@Column(name = "APPLET_CODE")
	public String appletCode;

	@Column(name = "APPLET_QR_CODE")
	public String appletQrCode;

	@Column(name = "APPLET_APP_ID")
	public String appletAppId;

	@Column(name = "APPLET_APP_SECRET")
	public String appletAppSecret;

	@Column(name = "APPLET_PAY_KEY")
	public String appletPayKey;

	@Column(name = "APPLET_MCH_ID")
	public String appletMchId;

	@Column(name = "SERVER_DOMAIN_NAME")
	public String serverDomainName;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	

	public static Map queryWxServerListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from wx_server a ";
		SQLResult ret = SQLParser.parseSQL(sql, condition);
		Query query = JPA.em().createNativeQuery(ret.getSql(), WxServer.class);
		int i = 1;
		for (ParamObject o : ret.getParams()) {
			query.setParameter(i++, o.getValue());
		}
		List<WxServer> data = query.setFirstResult((pageIndex - 1) * pageSize)
			.setMaxResults(pageSize).getResultList();
		Query query2 = JPA.em().createNativeQuery(ret.getCountSql());
		int j = 1;
		for (ParamObject o : ret.getParams()) {
			query2.setParameter(j++, o.getValue());
		}
		List<Object> countRet = query2.getResultList();
		long total = Long.parseLong(countRet.get(0).toString());
		Map hm = new HashMap();
		hm.put("total", total);
		hm.put("data", data);
		return hm;
	}
	
	public static WxServer getServerByServerid(String wxCode) {

		String sql = "select s.* from  wx_server s where s.wx_code= ? ";
		List<WxServer> list = JPA.em().createNativeQuery(sql, WxServer.class)
				.setParameter(1, wxCode).getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}
}
