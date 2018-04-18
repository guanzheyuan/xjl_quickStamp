package models.modules.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import play.db.jpa.JPA;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;
/**
 * 模型常用工具
 * @author lilisheng
 *
 */
public class ModelUtils {
	/**
	 * 创建sql结果对象
	 * @param condition 查询条件
	 * @param sql 基本的sql语句
	 * @return
	 */
	public static SQLResult createSQLResult(Map<String, String> condition, String sql) {
		return SQLParser.parseSQL(sql, condition);
	}
	/**
	 * 从数据库中查询数据出来，不指定返回类型，默认是object[]
	 * @param pageIndex 当前页
	 * @param pageSize 每页大小
	 * @param ret createSQLResult创建的对象
	 * @return
	 */
	public static List queryData(int pageIndex, int pageSize, SQLResult ret) {
		Query query = JPA.em().createNativeQuery(ret.getSql());
		return queryData(pageIndex, pageSize, ret, query);
	}
	/**
	 * 不进行分页查询所有数据
	 * @param ret
	 * @return
	 */
	public static List queryData(SQLResult ret) {
		Query query = JPA.em().createNativeQuery(ret.getSql());
		return queryData(1, 0, ret, query);
	}
	/**
	 * 指定返回类型
	 * @param pageIndex
	 * @param pageSize 如果pagesize小于1，则不进行分页
	 * @param ret
	 * @param clazz
	 * @return
	 */
	public static List queryData(int pageIndex, int pageSize, SQLResult ret, Class clazz) {
		Query query = JPA.em().createNativeQuery(ret.getSql(),clazz);
		return queryData(pageIndex, pageSize, ret, query);
	}
	/**
	 * 查询所有的列表，不进行分页
	 * @param ret
	 * @param clazz
	 * @return
	 */
	public static List queryData(SQLResult ret, Class clazz) {
		Query query = JPA.em().createNativeQuery(ret.getSql(),clazz);
		return queryData(1, 0, ret, query);
	}
	private static List queryData(int pageIndex, int pageSize, SQLResult ret, Query query) {
		int i = 1;
		for (ParamObject o : ret.getParams()) {
			query.setParameter(i++, o.getValue());
		}
		if (pageSize < 1){
			return query.getResultList();
		} else {
			return query.setFirstResult((pageIndex - 1) * pageSize)
				.setMaxResults(pageSize).getResultList();
		}
	}
	/**
	 * 创建返回map对象，map里面有两个属性total和data，这里主要完成创建map和total的自动获取
	 * @param ret createSQLResult创建的对象
	 * @param data 数据集合
	 * @return
	 */
	public static Map createResultMap(SQLResult ret, List data) {
		Map hm = new HashMap();
		Query query2 = JPA.em().createNativeQuery(ret.getCountSql());
		int j = 1;
		for (ParamObject o : ret.getParams()) {
			query2.setParameter(j++, o.getValue());
		}
		List<Object> countRet = query2.getResultList();
		long total = Long.parseLong(countRet.get(0).toString());
		hm.put("total", total);
		hm.put("data", data);
		return hm;
	}
	/**
	 * 对于不需要分页的，使用这个方法，直接返回data的大小作为total
	 * @param data
	 * @return
	 */
	public static Map createResultMap(List data) {
		Map hm = new HashMap();
		long total = data.size();
		hm.put("total", total);
		hm.put("data", data);
		return hm;
	}
	/**
	 * 删除数据
	 * @param condition 用于delete的where条件部分
	 * @param sql 删除sql语句
	 * @return
	 */
	public static int executeDelete(Map<String, String> condition, String sql) {
		SQLResult ret = createSQLResult(condition, sql);
		Query query = JPA.em().createNativeQuery(ret.getSql());
		int i = 1;
		for (ParamObject o : ret.getParams()) {
			query.setParameter(i++, o.getValue());
		}
		return query.executeUpdate();
	}
}
