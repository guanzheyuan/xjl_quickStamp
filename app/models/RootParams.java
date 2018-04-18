package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.StringUtil;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "root_params")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class RootParams extends GenericModel {
	@Id
	@Column(name = "param")
	public Long param;

	@Column(name = "param_name")
	public String paramName;

	@Column(name = "current_value")
	public String currentValue;

	@Column(name = "comments")
	public String comments;

	@Column(name = "mask")
	public String mask;

	@Column(name = "visible")
	public String visible;

	@Column(name = "sort")
	public String sort;

	@Column(name = "is_reserved")
	public String isReserved;

	@Column(name = "vno_id")
	public Long vnoId;
	
	public RootParams(){
		
	}
	
	public RootParams(String mask,String currentValue,String comments){
		this.comments = comments;
		this.currentValue = currentValue;
		this.mask = mask;
	}
	
	public static RootParams findByMask(String mask) {
		return RootParams.find("byMask", mask).first();
	}
	
	//查询列表
	public static Map queryRootParamsList(Map<String, String> condition,
			int pageIndex, int pageSize) {
		String sql = "select a.* ";
		sql += "from root_params a ";
		sql += "where 1=1 and a.visible='Y' ";
		if(StringUtil.isNotEmpty(condition.get("searchKeyWord"))){
			String searchKeyWord = condition.get("searchKeyWord");
			sql += "and (a.mask like '%"+searchKeyWord+"%' or a.comments like '%"+searchKeyWord+"%') ";
		}
		sql += "order by param asc ";
		SQLResult ret = SQLParser.parseSQL(sql, condition);
		Query query = JPA.em().createNativeQuery(ret.getSql(), RootParams.class);
		int i = 1;
		for (ParamObject o : ret.getParams()) {
			query.setParameter(i++, o.getValue());
		}
		List<RootParams> list = query.setFirstResult((pageIndex - 1) * pageSize)
				.setMaxResults(pageSize).getResultList();
		Query query2 = JPA.em().createNativeQuery(ret.getCountSql());
		int j = 1;
		for (ParamObject o : ret.getParams()) {
			query2.setParameter(j++, o.getValue());
		}
		List<Object> countRet = query2.getResultList();
		long cnt = Long.parseLong(countRet.get(0).toString());
		Map hm = new HashMap();
		hm.put("total", cnt);
		hm.put("data", list);
		return hm;
	}
}
