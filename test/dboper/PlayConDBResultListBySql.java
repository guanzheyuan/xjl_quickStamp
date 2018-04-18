package dboper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayConDBResultListBySql {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sqlString;
		List<String> list;
		Map objectMap;
		int index;
		//oracle
		sqlString = "select role,sysdate from dba_roles";
		list = getListBySqlForOracleDb(sqlString);
		index = 0;
		for (Object string : list) {
			System.out.print(">>>>>>>>>>>>>");
			objectMap = (HashMap)string;
			System.out.println(objectMap.get("role")+"|"+objectMap.get("sysdate"));
			++index;
			if(index>5){
				break;
			}
		}
		index = 0;
		//Mysql
		sqlString = "select counter_id,counter_name,counter_code,wx_code from portal_counter order by counter_id desc";
		list = getListBySqlForMysqlDb(sqlString);
		for (Object string : list) {
			System.out.print(">>>>>>>>>>>>>");
			objectMap = (HashMap)string;
			System.out.println(objectMap.get("counter_id")+"|"+objectMap.get("counter_name".toLowerCase()));
			++index;
			if(index>5){
				break;
			}
		}
		index = 0;
		//Mysql 打印参数
		sqlString = "select counter_id,counter_name,counter_code,wx_code from portal_counter order by counter_id desc";
		list = getListBySqlHaveParamForMysqlDb(sqlString);
		index = 0;
		
		//postgresql
		sqlString = "select a.user_id,a.user_code,a.nick_name,a.create_time,a.role_id,b.role_name ";
		sqlString += "from zzb_user a ";
		sqlString += "left join zzb_role b on a.role_id=b.role_id ";
		list = getListBySqlForPostgresDb(sqlString);
		
		for (Object string : list) {
			objectMap = (HashMap)string;
			System.out.println(objectMap.get("user_id")+"|"+objectMap.get("nick_name".toLowerCase())+">>>"+string);
			++index;
			if(index>5){
				break;
			}
		}
	}
//	public static String beanToJson(Object obj) {
//        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, 
//                SerializerFeature.UseISO8601DateFormat);
//    }
//    
//    @SuppressWarnings("unchecked")
//    public static <T> T jsonToBean(String json, Class<?> clazz) {
//        return (T) JSON.parseObject(json, clazz);
//    }
	/**
	 * 将ResultSet转为List
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static List resultSetToList(ResultSet rs)
			throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i)); // 所有字段名为小字
			}
			list.add(rowData);
		}
		return list;
	}
	public static List<String> getListBySqlForOracleDb(String sql) {
		List list = null;
		java.sql.Connection con;
		// 驱动程序名
		String driver = "oracle.jdbc.driver.OracleDriver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:oracle:thin:@121.43.37.149:1521:orcl";
		// MySQL配置时的用户名
		String user = "system";
		// MySQL配置时的密码
		String password = "Ztesoft134";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = (Statement) con.createStatement();
			// 要执行的SQL语句
			// 3.ResultSet类，用来存放获取的结果集！！
			ResultSet rs = statement.executeQuery(sql);
			System.out.println("数据库查询成功");
			list = resultSetToList(rs);
			rs.close();
			con.close();
		} catch (ClassNotFoundException e){
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e){
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
		return list;
	}
	public static List<String> getListBySqlForPostgresDb(String sql) {
		List list = null;
		java.sql.Connection con;
		// 驱动程序名
		String driver = "org.postgresql.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:postgresql://121.42.145.100:5432/zzb_test_16fi_20161102";
		// MySQL配置时的用户名
		String user = "zzb";
		// MySQL配置时的密码
		String password = "zzb123";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = (Statement) con.createStatement();
			// 要执行的SQL语句
			// 3.ResultSet类，用来存放获取的结果集！！
			ResultSet rs = statement.executeQuery(sql);
			System.out.println("数据库查询成功");
			list = resultSetToList(rs);
			rs.close();
			con.close();
		} catch (ClassNotFoundException e){
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e){
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
		return list;
	}
	public static List<String> getListBySqlForMysqlDb(String sql) {
		List list = null;
		java.sql.Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:mysql://2014wyx.gotoip1.com:3306/2014wyx";
		// MySQL配置时的用户名
		String user = "2014wyx";
		// MySQL配置时的密码
		String password = "1qazWSX";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = (Statement) con.createStatement();
			// 要执行的SQL语句
			// 3.ResultSet类，用来存放获取的结果集！！
			ResultSet rs = statement.executeQuery(sql);
			System.out.println("数据库查询成功");
			list = resultSetToList(rs);
			rs.close();
			con.close();
		} catch (ClassNotFoundException e){
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e){
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
		return list;
	}
	public static List<String> getListBySqlHaveParamForMysqlDb(String sql) {
		List list = null;
		java.sql.Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:mysql://2014wyx.gotoip1.com:3306/2014wyx";
		// MySQL配置时的用户名
		String user = "2014wyx";
		// MySQL配置时的密码
		String password = "1qazWSX";
		// 遍历查询结果集
		try {//驱动注册  
            Class.forName(driver);  
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed()){
				System.out.println("Succeeded connecting to the Database!");
				LoggableStatement log = new LoggableStatement(con,"select counter_id,counter_name,counter_code,wx_code from portal_counter where counter_id=? order by counter_id desc;" );  
	            log.setObject(1, "373");
	            System.out.println(log.getQueryString());  
	            log.execute();  
			}
        } catch (ClassNotFoundException ex) { 
        	ex.printStackTrace();  
        	System.out.println("Oh,not"); 
        } catch (SQLException e) {  
        	e.printStackTrace();
        	System.out.println("Oh,not"); 
        }  
        
		return list;
	}

}
