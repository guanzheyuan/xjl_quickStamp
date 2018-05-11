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
@Table(name = "xjl_sc_config")
public class XjlScConfig extends GenericModel {

	
	@Id
	@Column(name = "id")
	public Long id;
	
	@Column(name = "time_quantum")
	public String timeQuantum;
	
	@Column(name = "interval")
	public String interval;
	
	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	
	public static Map query(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_sc_config where status = '0AA' and interval is null order by create_time desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScConfig> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlScConfig.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static Map queryNotTime(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_sc_config where status ='0AA' and interval is null order by create_time desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScConfig> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlScConfig.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static Map queryInterval(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_sc_config where status='0AA' and interval is not null order by create_time desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScConfig> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlScConfig.class);
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static int modifyInterval(String timeQuantum,String interval){
		String sql = "update xjl_sc_config set interval='"+interval+"' where time_quantum = '"+timeQuantum+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public static int modifyTimeQuanTumById(String timeQuanTum,String id){
		String sql = "update xjl_sc_config set time_quantum = '"+timeQuanTum+"' where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public  static int  modifyIntervalById(String timeQuanTum ,String interval,String id){
		String sql = "update xjl_sc_config set time_quantum='"+timeQuanTum+"', interval='"+interval+"' where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public static int modifyIntervalIsNull(Long id){
		String sql = "update xjl_sc_config set interval=null where id = '"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	public static int modifyStatus(Long id){
		String sql = "update xjl_sc_config set status='0XX' where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public static XjlScConfig queryScConfigById(Long id){
		String sql = "select * from xjl_sc_config where id='"+id+"'";
		Map<String, String> condition = new HashMap<String, String>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScConfig> data = ModelUtils.queryData(1, -1, ret,XjlScConfig.class);
		if(data.isEmpty()){
			return null;
		}
		return data.get(0);
	}
	
	
}
