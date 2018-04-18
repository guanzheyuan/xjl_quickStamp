package models.modules.mobile;
import java.sql.*;
import java.util.Date;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "xjl_dw_file")
public class XjlDwFile extends GenericModel{

	@Id
	@Column(name = "FILE_ID")
	public Long fileId;

	@Column(name = "FILE_NAME")
	public String fileName;

	@Column(name = "FILE_URL")
	public String fileUrl;

	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;

	@Column(name = "FILE_TYPE")
	public String fileType;

	@Column(name = "SAVE_FOLDER")
	public String saveFolder;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;

	@Column(name = "DELETE_TIME")
	public Date deleteTime;

	@Column(name = "UP_CHANNEL")
	public String upChannel;
	
	@Column(name="FILE_SIZE")
	public String fileSize;
	
	@Column(name="REAL_NAME")
	public String realName;

	@Transient
	public String  htmlName; 
	public static Map queryXjlDwFileListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from xjl_dw_file a where status='0AA' and WX_OPEN_ID='"+condition.get("wxOpenId")+"'";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwFile> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwFile.class);
		return ModelUtils.createResultMap(ret, data);
	}
	/**
	 * 根据fileId得到一个file对象
	 * @param fileId
	 * @return
	 */
	public static XjlDwFile queryXjlDwFileById(Long fileId) {
			String sql = "select * ";
			sql += "from xjl_dw_file a where FILE_ID = [l:fileId] ";
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("fileId", String.valueOf(fileId));
			SQLResult ret = ModelUtils.createSQLResult(condition, sql);
			List<XjlDwFile> data = ModelUtils.queryData(1, -1, ret,XjlDwFile.class);
			if (data.isEmpty()){
				throw new RuntimeException("没有找到对应的文件:" + fileId);
			} else {
				return data.get(0);
			}
	}
	public static int modifyFile (Long fileId){
		String sql="update xjl_dw_file set status='0XX' where FILE_ID="+fileId+"";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
