package models.modules.mobile;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.LoggerFactory;

import play.db.jpa.GenericModel;
import utils.StringUtil;
import utils.jpa.SQLResult;

@Entity
@Table(name = "wx_user")
public class WxUser extends GenericModel {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(WxUser.class);
	@Id
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId; 

//	@Column(name = "company_id")
//	public Long companyId;

	@Column(name = "NICK_NAME")
	public String nickName;

	@Column(name = "HEAD_IMG_URL")
	public String headImgUrl;

	@Column(name = "SEX")
	public String sex;

	@Column(name = "LANGUAGE")
	public String language;

	@Column(name = "COUNTRY")
	public String country;

	@Column(name = "PROVINCE")
	public String province;

	@Column(name = "CITY")
	public String city;

	@Column(name = "WX_PHONE")
	public String wxPhone;
	
	@Column(name = "USER_PWD")
	public String userPwd;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Column(name = "UP_OPENID_TIME")
	public Date upOpenidTime;

	@Column(name = "IS_CONCERNED")
	public String isConcerned;
	
	@Column(name = "OPEN_ID_CHANNCEL")
	public String openIdChanncel;
	
	@Column(name = "user_type")
	public String userType;
	
	@Transient
	public WxServer wxServer;
	
	
	/**
	 * 通过微信编号得到用户信息
	 * @param openid
	 * @return
	 */
	public static WxUser getFindByOpenId(String openid){
		log.debug("getFindByOpenId方法openId:" + openid);
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        if(StringUtil.isNotEmpty(openid)){
            condition.put("wxOpenId", openid);
        }
        Map returnMap = queryWxUserListByPage(condition,pageIndex,pageSize);
        List<WxUser> retData = (List<WxUser>)returnMap.get("data");
        if(retData.isEmpty()){
        	throw new RuntimeException("没有该用户:"+openid);
        }else{
        	log.debug("........................."+retData.get(0));
        	log.debug("一共查询符合条件的数据有:" + retData.size());
        	WxUser wxUser = retData.get(0);
        	log.debug("........................."+retData.get(0).nickName);
        	return wxUser;
        }
	}
	
	public static WxUser getUserByOpenId(String openid){
		log.debug("getFindByOpenId方法openId:" + openid);
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        if(StringUtil.isNotEmpty(openid)){
            condition.put("wxOpenId", openid);
        }
        Map returnMap = queryWxUserListByPage(condition,pageIndex,pageSize);
        List<WxUser> retData = (List<WxUser>)returnMap.get("data");
        if(retData.isEmpty()){
        	return null;
        }else{
        	log.debug("一共查询符合条件的数据有:" + retData.size());
        	WxUser wxUser = retData.get(0);
        	log.debug("........................."+wxUser.nickName);
        	return wxUser;
        }
	}
	/**
	 * 查询用户
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryWxUserListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from wx_user a ";
		sql += "where 1=1 and status='0AA' ";
		if(StringUtil.isNotEmpty(condition.get("wxOpenId"))){
			String searchKeyWord = condition.get("wxOpenId");
			sql += "and a.wx_open_id='"+searchKeyWord+"' ";
	    }
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUser> data = ModelUtils.queryData(pageIndex, pageSize, ret, WxUser.class);
		return ModelUtils.createResultMap(ret, data);
	}
}
