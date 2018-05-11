package models.modules.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_sc_toiletinfo")
public class XjlScToiletInfo extends GenericModel {

	@Id
	@Column(name = "id")
	public Long id;
	
	@Column(name = "toilet_name")
	public String toiletName;
	
	@Column(name = "toilet_code")
	public String toiletCode;
	
	@Column(name = "device_code")
	public String deviceCode;
	
	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	public static Map query(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_sc_toiletinfo order by create_time desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScToiletInfo> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlScToiletInfo.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static int delete(String id){
		String sql = "delete from xjl_sc_toiletinfo where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public static XjlScToiletInfo queryScToiletById(Long id){
		String sql = "select * from xjl_sc_toiletinfo where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScToiletInfo> data = ModelUtils.queryData(1, -1, ret,XjlScToiletInfo.class);
		if(data.isEmpty()){
			return null;
		}
		return data.get(0);
	}
	
	public static int modifyToilet(String toiletName,String id){
		String sql="update xjl_sc_toiletinfo set toilet_name='"+toiletName+"' where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
