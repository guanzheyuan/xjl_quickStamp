package util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import utils.DateUtil;

public class GenEntityUtil {
	private String[] dbcolnames; // 数据库里列名数组
	private String[] colnames; // 列名数组
	private String[] colTypes; // 列名类型数组
	private int[] colSizes; // 列名大小数组
	private boolean f_util = false; // 是否需要导入包java.util.*
	private boolean f_sql = false; // 是否需要导入包java.sql.*

	public static void main(String[] args) {
		String packagePath = "models.modules.mobile";
		String tableName = "xjl_dw_notice";
		String primayKey = "notice_id";
		new GenEntityUtil(packagePath, tableName);
		packagePath = "controllers.modules.wechat.bo";
		GenEntityBoUtil(packagePath,tableName,primayKey);
	}
	
	public static void GenEntityBoUtil(String packagePath, String tableName,String primayKey){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("package controllers.modules.wechat.bo;\r\n");
		strBuffer.append("\r\n");
		strBuffer.append("import models.modules.wechat."+GenEntityUtil.initcap(tableName)+";\r\n");
		strBuffer.append("import utils.DateUtil;\r\n");
		strBuffer.append("import utils.SeqUtil;\r\n");
		strBuffer.append("/**\r\n");
		strBuffer.append(" * @author    姓名   E-mail: 邮箱  Tel: 电话\r\n");
		strBuffer.append(" * @version   创建时间："+DateUtil.getDateTime(new Date(), "yyyy-MM-dd ahh:mm:ss")+"\r\n");
		strBuffer.append(" * @describe  类说明\r\n");
		strBuffer.append("*/\r\n");
		strBuffer.append("public class "+GenEntityUtil.initcap(tableName.concat("_bo"))+" {\r\n");
		strBuffer.append("\t// 增加、编辑\r\n");
		strBuffer.append("\tpublic static "+GenEntityUtil.initcap(tableName)+" save("+GenEntityUtil.initcap(tableName)+" "+GenEntityUtil.getCamelStrForCol(tableName)+") {\r\n");
		strBuffer.append("\t\tif ("+GenEntityUtil.getCamelStrForCol(tableName)+"."+GenEntityUtil.getCamelStrForCol(primayKey)+" != null) {\r\n");
		//strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".version += 1;\r\n");
		//strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".updateTime = DateUtil.getTimestamp();\r\n");
		strBuffer.append("\t\t}\r\n");
		strBuffer.append("\t\tif ("+GenEntityUtil.getCamelStrForCol(tableName)+"."+GenEntityUtil.getCamelStrForCol(primayKey)+" == null) {\r\n");
		strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+"."+GenEntityUtil.getCamelStrForCol(primayKey)+" = SeqUtil.maxValue(\""+tableName+"\", \""+primayKey+"\");\r\n");
		strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".status = \"0AA\";\r\n");
		strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".createTime = DateUtil.getNowDate();\r\n");
		//strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".version = 0l;\r\n");
		strBuffer.append("\t\t}\r\n");
		strBuffer.append("\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+" = "+GenEntityUtil.getCamelStrForCol(tableName)+".save();\r\n");
		strBuffer.append("\t\treturn "+GenEntityUtil.getCamelStrForCol(tableName)+";\r\n");
		strBuffer.append("\t}\r\n");
		strBuffer.append("\t// 删除\r\n");
		strBuffer.append("\tpublic static "+GenEntityUtil.initcap(tableName)+" del("+GenEntityUtil.initcap(tableName)+" "+GenEntityUtil.getCamelStrForCol(tableName)+") {\r\n");
		strBuffer.append("\t\tif ("+GenEntityUtil.getCamelStrForCol(tableName)+" != null) {\r\n");
		//strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".updateTime = DateUtil.getNowDate();\r\n");
		//strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".version += 1;\r\n");
		strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+".status = \"0XX\";\r\n");
		strBuffer.append("\t\t\t"+GenEntityUtil.getCamelStrForCol(tableName)+" = "+GenEntityUtil.getCamelStrForCol(tableName)+".save();\r\n");
		strBuffer.append("\t\t\treturn "+GenEntityUtil.getCamelStrForCol(tableName)+";\r\n");
		strBuffer.append("\t\t}\r\n");
		strBuffer.append("\t\treturn null;\r\n");
		strBuffer.append("\t}\r\n");
		strBuffer.append("}");
		try {
			String path = System.getProperty("user.dir") + "/app/"
					+ packagePath.replaceAll("\\.", "/");
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String resPath = path + "/" + initcap(tableName.concat("_bo")) + ".java";
			System.out.println("resPath=" + resPath);
			FileUtils.writeStringToFile(new File(resPath), strBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GenEntityUtil(String packagePath, String tableName) {
		// 得到数据库连接
		Connection conn = null; // 得到数据库连接
		//ORACLE
//		conn = DatabaseUtils.oracelDbOpenConnection(); 
		//PG
		conn = DatabaseUtils.pgDbOpenConnection();
		PreparedStatement pstmt = null;
		String strsql = "select * from " + tableName;
		try {
			pstmt = conn.prepareStatement(strsql);
			ResultSetMetaData rsmd = pstmt.getMetaData();
			int size = rsmd.getColumnCount(); // 共有多少列
			dbcolnames = new String[size];
			colnames = new String[size];
			colTypes = new String[size];
			colSizes = new int[size];
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				dbcolnames[i] = rsmd.getColumnName(i + 1);
				colnames[i] = this.getCamelStrForCol(rsmd.getColumnName(i + 1));
				colTypes[i] = rsmd.getColumnTypeName(i + 1);
				if (colTypes[i].equalsIgnoreCase("datetime")) {
					f_util = true;
				}
				if (colTypes[i].equalsIgnoreCase("image")
						|| colTypes[i].equalsIgnoreCase("text")
						||colTypes[i].equalsIgnoreCase("timestamp")) {
					f_sql = true;
				}
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
			}
			try {
				String content = parse(colnames, colTypes, colSizes,
						packagePath, tableName);
				String path = System.getProperty("user.dir") + "/app/"
						+ packagePath.replaceAll("\\.", "/");
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				String resPath = path + "/" + initcap(tableName) + ".java";
				System.out.println("resPath=" + resPath);
				FileUtils.writeStringToFile(new File(resPath), content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DatabaseUtils.closeDatabase(conn, pstmt, null);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析处理(生成实体类主体代码)
	 */
	private String parse(String[] colNames, String[] colTypes, int[] colSizes,
			String packagePath, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + packagePath + ";");
		sb.append("\r\n");
		if (f_util) {
			sb.append("import java.util.Date;\r\n");
		}
		if (f_sql) {
			sb.append("import java.sql.*;\r\n");
		}
		sb.append("import java.util.Date;\r\n");
		sb.append("import java.util.*;\r\n");
		sb.append("\r\n");
		sb.append("import javax.persistence.Column;\r\n");
		sb.append("import javax.persistence.Entity;\r\n");
		sb.append("import javax.persistence.Id;\r\n");
		sb.append("import javax.persistence.Query;\r\n");
		sb.append("import javax.persistence.Table;\r\n");
		sb.append("\r\n");
		sb.append("import play.db.jpa.GenericModel;\r\n");
		sb.append("import play.db.jpa.JPA;\r\n");
		sb.append("import play.db.jpa.Model;\r\n");
		sb.append("import utils.jpa.ParamObject;\r\n");
		sb.append("import utils.jpa.SQLResult;\r\n");
		sb.append("import utils.jpa.sql.SQLParser;\r\n");
		sb.append("\r\n");
		sb.append("@Entity\r\n");
		sb.append("@Table(name = \""+tableName+"\")\r\n");
		sb.append("public class " + initcap(tableName) + " extends GenericModel{\r\n\r\n");
		processAllAttrs(sb);
//		processAllMethod(sb);
		processQuerObjectListMethod(sb,tableName);
		sb.append("}\r\n");
		//System.out.println(sb.toString());
		return sb.toString();

	}

//	/**
//	 * 生成所有的方法
//	 * 
//	 * @param sb
//	 */
//	private void processAllMethod(StringBuffer sb) {
//		for (int i = 0; i < colnames.length; i++) {
//			sb.append("\tpublic void set" + initcap(colnames[i]) + "("
//					+ sqlType2JavaType(colTypes[i]) + " " + colnames[i]
//					+ "){\r\n");
//			sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");
//			sb.append("\t}\r\n\r\n");
//
//			sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"
//					+ initcap(colnames[i]) + "(){\r\n");
//			sb.append("\t\treturn " + colnames[i] + ";\r\n");
//			sb.append("\t}\r\n\r\n");
//		}
//	}

	/**
	 * 解析输出属性
	 * 
	 * @return
	 */
	private void processAllAttrs(StringBuffer sb) {
		for (int i = 0; i < colnames.length; i++) {
			if(i==0){
				sb.append("\t@Id\r\n");
			}
			sb.append("\t@Column(name = \""+dbcolnames[i].toUpperCase()+"\")\r\n");
			sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " "
					+ colnames[i] + ";\r\n\r\n");
		}
	}
	
	/***
	 * 解析输出查询列表方法
	 * @param sb
	 */
	private void processQuerObjectListMethod(StringBuffer sb,String tableName) {
		sb.append("\tpublic static Map query"+initcap(tableName)+"ListByPage(Map<String, String> condition,\r\n");
		sb.append("\t\tint pageIndex, int pageSize) {\r\n");
		sb.append("\t\tString sql = \"select * \";\r\n");
		sb.append("\t\tsql += \"from "+tableName+" a \";\r\n");
		sb.append("\t\tSQLResult ret = SQLParser.parseSQL(sql, condition);\r\n");
		sb.append("\t\tQuery query = JPA.em().createNativeQuery(ret.getSql(), "+initcap(tableName)+".class);\r\n");
		sb.append("\t\tint i = 1;\r\n");
		sb.append("\t\tfor (ParamObject o : ret.getParams()) {\r\n");
		sb.append("\t\t\tquery.setParameter(i++, o.getValue());\r\n");
		sb.append("\t\t}\r\n");
		sb.append("\t\tList<"+initcap(tableName)+"> data = query.setFirstResult((pageIndex - 1) * pageSize)\r\n");
		sb.append("\t\t\t.setMaxResults(pageSize).getResultList();\r\n");
		sb.append("\t\tQuery query2 = JPA.em().createNativeQuery(ret.getCountSql());\r\n");
		sb.append("\t\tint j = 1;\r\n");
		sb.append("\t\tfor (ParamObject o : ret.getParams()) {\r\n");
		sb.append("\t\t\tquery2.setParameter(j++, o.getValue());\r\n");
		sb.append("\t\t}\r\n");
		sb.append("\t\tList<Object> countRet = query2.getResultList();\r\n");
		sb.append("\t\tlong total = Long.parseLong(countRet.get(0).toString());\r\n");
		sb.append("\t\tMap hm = new HashMap();\r\n");
		sb.append("\t\thm.put(\"total\", total);\r\n");
		sb.append("\t\thm.put(\"data\", data);\r\n");
		sb.append("\t\treturn hm;\r\n");
		sb.append("\t}\r\n");
	}

	/**
	 * 把输入字符串的首字母改成大写
	 * 
	 * @param str
	 * @return
	 */
	public static String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return getCamelStr(new String(ch));
	}

	// 例：portal_user --> PortalUser (表名)
	public static String getCamelStr(String s) {
		while (s.indexOf("_") > 0) {
			int index = s.indexOf("_");
			// System.out.println(s.substring(index+1, index+2).toUpperCase());
			s = s.substring(0, index)
					+s.substring(index + 1, index + 2).toUpperCase()
					+s.substring(index + 2);
		}
		return s;
	}
	// 例：user_name --> userName (列名)
	public static String getCamelStrForCol(String string) {
		while(string.startsWith("_")){
			string = string.substring(1);
		}
		while(string.endsWith("_")){
			string = string.substring(0,string.length()-1);
		}
		if(string.indexOf("_")>0){
			String []arrayStr = string.split("_");
			string = "";
			for (int i=0;i<arrayStr.length;i++) {
				if(arrayStr[i].length()==0){
					continue;
				}
				if("".equals(string)){
					string += arrayStr[i].toLowerCase();
				}else{
					string += arrayStr[i].substring(0,1).toUpperCase() + arrayStr[i].substring(1).toLowerCase();
				}
			}
		}else{
			string = string.toLowerCase();
		}
		return string;
	}

	private String sqlType2JavaType(String sqlType) {
		System.out.println("-----------:sqlType"+sqlType);
		if (sqlType.equalsIgnoreCase("bit")) {
			return "bool";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")
				|| sqlType.equalsIgnoreCase("integer")) {
			return "int";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("decimal")
				|| sqlType.equalsIgnoreCase("real")) {
			return "double";
		} else if (sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney")) {
			return "double";
		} else if (sqlType.equalsIgnoreCase("varchar")
				|| sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar")
				|| sqlType.equalsIgnoreCase("nchar")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			return "Date";
		}

		else if (sqlType.equalsIgnoreCase("image")) {
			return "Blob";
		} else if (sqlType.equalsIgnoreCase("text")) {
			return "Clob";
		}
		//pg
		else if (sqlType.equalsIgnoreCase("numeric")) {
			return "Long";
		}
		//oracle
		else if (sqlType.equalsIgnoreCase("VARCHAR2")) {
			return "String";
		}else if (sqlType.equalsIgnoreCase("CLOB")) {
			return "String";
		}else if(sqlType.equalsIgnoreCase("NUMBER")){
			return "Long";
		}else if(sqlType.equalsIgnoreCase("Timestamp")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("DATE")){
			return "Date";
		}
		
		return null;
	}


}
