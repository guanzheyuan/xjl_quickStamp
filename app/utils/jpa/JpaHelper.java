package utils.jpa;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import play.db.helper.SqlQuery;
import play.db.jpa.JPA;
import utils.jpa.sql.SQLParser;
import exception.BaseAppException;
import exception.ExceptionHandler;

public class JpaHelper {

	private JpaHelper() {
	}

	public static Query execute(String sql, Object... params) {
		Query query = JPA.em().createQuery(sql);
		int index = 0;
		for (Object param : params) {
			query.setParameter(++index, param);
		}
		return query;
	}

	public static Query executeList(String sql, List<Object> params) {
		Query query = JPA.em().createQuery(sql);
		int index = 0;
		for (Object param : params) {
			query.setParameter(++index, param);
		}
		return query;
	}

	public static Query execute(SqlQuery query) {
		return executeList(query.toString(), query.getParams());
	}

	public <T> List<T> selectList(String selectSql, Class<?> retClass,
			Map condition) throws BaseAppException {
		SQLResult sqlResult = SQLParser.parseSQL(selectSql, condition);
		Query query = JPA.em().createNativeQuery(sqlResult.getSql(), retClass);
		int index = 0;
		for (Object object : sqlResult.getParams()) {
			query.setParameter(++index, object.toString());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> T selectObject(String selectSql, Class<?> retClass)
			throws BaseAppException {
		return (T) JPA.em().createNativeQuery(selectSql, retClass)
				.getSingleResult();
	}

	public Query querySetParameter(Map condition, Query query) {
		if (condition != null) {
			Integer doctortype_id = (Integer) condition.get("doctortype_id");
			if (doctortype_id != null && doctortype_id != 0) {
				query.setParameter("doctortype_id", doctortype_id);
			}
			Integer department_id = (Integer) condition.get("department_id");
			if (department_id != null && department_id != 0) {
				query.setParameter("department_id", department_id);
			}
		}
		return query;
	}

}
