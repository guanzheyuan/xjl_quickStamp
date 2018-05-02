package models.modules.mobile;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_sc_log")
public class XjlScLog extends GenericModel {

	@Id
	@Column(name = "id")
	public Long id;
	
	@Column(name = "method_name")
	public String methodName;
	
	@Column(name = "error_desc")
	public String errorDesc;
	
	@Column(name = "error_code")
	public String errorCode;
	
	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	
	public static Map query(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_sc_log where status='0AA' order by create_time desc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlScLog> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlScLog.class);
		if(!data.isEmpty()){
			
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
}
