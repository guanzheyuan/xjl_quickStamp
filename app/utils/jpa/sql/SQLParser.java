package utils.jpa.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import antlr.StringUtils;
import play.Logger;
import utils.DateUtil;
import utils.StringUtil;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;

public class SQLParser {

	public static SQLResult parseSQL(String sql, Map params) {

		char[] sqlCharTemp = sql.toCharArray();
		StringBuilder sqlInput = new StringBuilder();
		boolean isConstParam = false;
		boolean isDynamicParam = false;
		// SQl中单引号内字符不进行大小写转换
		for (char c : sqlCharTemp) {
			switch (c) {
			case '\'':
				sqlInput.append(c);
				isConstParam = !isConstParam;
				break;
			case ':':
				sqlInput.append(c);
				isDynamicParam = !isDynamicParam;
				break;
			case ']':
				sqlInput.append(c);
				isConstParam = false;
				isDynamicParam = false;
				break;
			default:
				if (isConstParam || isDynamicParam) {// 常数参数或者动态参数，不小写
					sqlInput.append(c);
				} else {
					sqlInput.append(Character.toLowerCase(c));
				}
			}
		}

		Logger.debug("++++ LowerCase SQL =%s", sqlInput.toString());

		char[] sqlChar = sqlInput.toString().toCharArray();

		StringBuilder sqlSB = new StringBuilder();
		StringBuilder itemSB = new StringBuilder();
		List<ParamObject> paramList = new ArrayList<ParamObject>();
		boolean choice = false;
		for (char c : sqlChar) {
			switch (c) {
			case '[':
				itemSB.append(" ");
				choice = true;
				break;
			case ']':
				if (choice) {
					String sqlText = itemSB.toString();
					String key = null;
					Boolean isLong = false;
					Boolean isTime = false;
					if (sqlText.contains("l:")) {
						key = sqlText.substring(sqlText.indexOf("l:") + 2)
								.trim();
						isLong = true;
					} else if (sqlText.contains("t:")) {
						key = sqlText.substring(sqlText.indexOf("t:") + 2)
								.trim();
						isTime = true;
					} else if (sqlText.contains(":")) {
						if (sqlText.contains("%")) {
							key = sqlText.substring(sqlText.indexOf(":") + 1,
									sqlText.lastIndexOf("%")).trim();
						} else {
							key = sqlText.substring(sqlText.indexOf(":") + 1)
									.trim();
						}
					}

					// 判断参数是否存在，正常，大小，驼峰三种方式分别查找，兼容手写笔误
					boolean paramExist = false;
					if (params.containsKey(key)) {
						paramExist = true;
					} else {
						key = StringUtil.underlineToCamel(key);
						if (params.containsKey(key)) {
							paramExist = true;
						} else {
							key = key.toUpperCase();
							if (params.containsKey(key)) {
								paramExist = true;
							}
						}
					}

					// 存在该参数
					if (paramExist) {
						ParamObject object = null;
						Object v = params.get(key);
						if (v != null && v.toString().length() > 0
								&& !"-1".equals(v)) {
							if (isLong) {
								sqlText = sqlText.substring(0,
										sqlText.indexOf("l:"))
										+ "?";
								object = new ParamObject(key, Long.valueOf(v
										.toString()));
							} else if (isTime) {
								sqlText = sqlText.substring(0,
										sqlText.indexOf("t:"))
										+ "?";
								object = new ParamObject(key,
										DateUtil.string2SQLDate(v.toString()));
							} else {
								sqlText = sqlText.substring(0,
										sqlText.indexOf(":"))
										+ "?";
								if (sqlText.contains("%")) {
									sqlText = sqlText.substring(0,
											sqlText.indexOf("%"))
											+ "?";
									object = new ParamObject(key, "%"
											+ params.get(key) + "%");
								} else {
									object = new ParamObject(key, v);
								}
							}
							if (object.getValue() != null
									&& object.getValue().toString().length() > 0) {
								sqlSB.append(sqlText);
								paramList.add(object);
							}
						}
					}
				}
				choice = false;
				itemSB.delete(0, itemSB.length());
				break;
			default:
				if (choice) {
					itemSB.append(c);
				} else {
					sqlSB.append(c);
				}
			}
		}
		SQLResult ret = new SQLResult();
		ret.setSql(sqlSB.toString());
		ret.setParams(paramList);

		Logger.debug("++++SQLResult SQL =%s", ret.getSql());
		// if (Logger.isDebugEnabled()){
		int i = 1;
		for (ParamObject obj : ret.getParams()) {
			Logger.debug("++++Param_%s =%s", i++, obj.getValue());
		}
		// }
		return ret;
	}

	public static void main(String[] args) {
		SQLParser parser = new SQLParser();
		parser.parseSQL(
				"select * from bfm_staff where 1=1 [and aa=:aa] [and bb=:bb]",
				null);
	}
}
