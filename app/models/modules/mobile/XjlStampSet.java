package models.modules.mobile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.Play;
import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_stamp_set")
public class XjlStampSet extends GenericModel {

	@Id
	@Column(name = "SET_ID")
	public Long setId;
	
	@Column(name = "FILE_ID")
	public Long fileId;
	
	@Column(name = "COPIES")
	public String  copies;
	
	@Column(name = "SCOPE")
	public String scope;
	
	@Column(name = "PAPER_TYPE")
	public String paperType;
	
	@Column(name = "COLOUR")
	public String colour;
	
	@Column(name = "FACE")
	public String face;
	
	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	
	@Column(name = "TOTAL_PRICE")
	public String totalPrice;
	
	@Column(name = "out_trade_no")
	public String outTradeNo;
	
	@Column(name = "pay_state")
	public String payState;
	
	@Transient
	public String filePath;
	
	
	/**
	 * 根据主键得到打印设置
	 * @param setId
	 * @return
	 */
	public static XjlStampSet queryXjlStampSetById(Long setId){
		String sql="select * from xjl_stamp_set where SET_ID = [l:setId]";
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("setId", String.valueOf(setId));
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlStampSet> data = ModelUtils.queryData(1, -1, ret,XjlStampSet.class);
		if(data.isEmpty()){
			return null;
		}else {
			return data.get(0);
		}
	}
	
	public static Map query(Map<String, String> condition,
			int pageIndex, int pageSize){
		String sql = "select * from xjl_stamp_set where WX_OPEN_ID = '"+condition.get("wxOpenId")+"'and  status='0AA' order by CREATE_TIME desc ";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlStampSet> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlStampSet.class);
		if(!data.isEmpty()){
			Calendar ca = Calendar.getInstance();
			int year = ca.get(Calendar.YEAR);
			String savePath = Play.roots.get(0).child("public").child("tmp").child("QRcode").getRealFile().getAbsolutePath()
					+File.separator+year+File.separator;
			for (XjlStampSet xjlStampSet : data) {
				xjlStampSet.filePath = "/sp/public/tmp/QRcode/"+year+File.separator+xjlStampSet.wxOpenId+File.separator+xjlStampSet.outTradeNo+File.separator+xjlStampSet.outTradeNo+".png";
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static int modifyPayStatus(Long setId){
		String sql ="update xjl_stamp_set set pay_state='1'  where SET_ID = '"+setId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
