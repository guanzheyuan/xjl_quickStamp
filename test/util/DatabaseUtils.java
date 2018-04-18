package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 姓名 E-mail: 邮箱 Tel: 电话
 * @version 创建时间：2017-3-21 上午10:57:38
 * @describe 类说明
 */
public class DatabaseUtils {

	//Postgresql9.3.6
	public static Connection pgDbOpenConnection() {
		java.sql.Connection con = null;
		// 驱动程序名
		String driver = "org.postgresql.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:postgresql://121.43.37.149:5432/xjl_dw";
		// MySQL配置时的用户名
		String user = "zzb";
		// MySQL配置时的密码
		String password = "zzb123";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed()){
				//System.out.println("Succeeded connecting to the Database!");
			}
			else{
				System.out.println("Failed connecting to the Database!");
			}
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库连接已打开！！");
		}
		return con;
	}
	//ORACLE
	public static Connection oracelDbOpenConnection() {
		java.sql.Connection con = null;
		// 驱动程序名
		String driver = "oracle.jdbc.driver.OracleDriver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:oracle:thin:@121.43.37.149:1521:orcl";
		// MySQL配置时的用户名
		String user = "system";
		// MySQL配置时的密码
		String password = "Ztesoft134";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed()){
				//System.out.println("Succeeded connecting to the Database!");
			}
			else{
				System.out.println("Failed connecting to the Database!");
			}
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库连接已打开！！");
		}
		return con;
	}

	public static void closeDatabase(Connection conn,PreparedStatement pstmt,String str) throws ClassNotFoundException {
		try {
			if (!conn.isClosed()){
				pstmt.close();
				conn.close();
			}
		} catch (SQLException e){
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库连接已关闭！！");
		}
	}
}
