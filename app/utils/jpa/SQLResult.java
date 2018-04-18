package utils.jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLResult {
	private String sql;
	private String countSql;
	private List<ParamObject> params = new ArrayList<ParamObject>();

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<ParamObject> getParams() {
		return params;
	}

	public void setParams(List<ParamObject> params) {
		this.params = params;
	}

	public String getCountSql() {
		
		String a = "";
		if(sql.toLowerCase().indexOf("group by")<0){
			//非统计类的正常查询列表
			a = sql.substring(sql.indexOf("select"), 6) + " count(1) cnt "
					+ sql.substring(sql.indexOf("from"));
			if (a.toLowerCase().lastIndexOf("order by") > 0) {
				a = a.substring(0, a.lastIndexOf("order by"));
			}
		}
		else if(sql.toLowerCase().indexOf("group by")>0){
			//统计类，兼容非统计类的正常查询列表
			a += "select count(1) cnt from(";
			if(sql.toLowerCase().lastIndexOf("order by") > 0){
				a += sql.substring(0, sql.toLowerCase().lastIndexOf("order by"));
			}
			a += ") foo ";
		}
		return a;
	}
	
	// java.sql.Clob类型转换成String类型
	public static String ClobToString(Clob clob) throws SQLException, IOException {
		String reString = "";
		Reader is = clob.getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	public static void main(String[] args) {
		SQLResult sr = new SQLResult();
		sr.setSql("select * from bfm_staff where 1=1 and b=? order by a desc");
		System.out.println(sr.getCountSql());
	}

}
