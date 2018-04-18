package utils;

import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;

import java.net.URL;

import java.util.LinkedHashMap;

import java.util.Locale;

import java.util.Map;

public class T1 {

	public static void main(String[] args) throws
			Throwable {

		String downloadUrl = "http://avatar.csdn.net/6/E/1/1_javalover00000.jpg";

		URL url = new URL(downloadUrl);

		// 打开HttpURLConnection

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// 设置 HttpURLConnection的断开时间

		conn.setConnectTimeout(15000);

		// 设置 HttpURLConnection的请求方式

		conn.setRequestMethod("GET");

		// 设置 HttpURLConnection的接收的文件类型

		conn.setRequestProperty(

				"Accept",

				"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "

						+ "application/x-shockwave-flash, application/xaml+xml, "

						+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "

						+ "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");

		// 设置 HttpURLConnection的接收语音

		conn.setRequestProperty("Accept-Language", Locale.getDefault()
				.toString());

		// 指定请求uri的源资源地址

		conn.setRequestProperty("Referer", downloadUrl);

		// 设置 HttpURLConnection的字符编码

		conn.setRequestProperty("Accept-Charset", "UTF-8");

		// 检查浏览页面的访问者在用什么操作系统（包括版本号）浏览器（包括版本号）和用户个人偏好

		// conn.setRequestProperty(

		// "User-Agent",

		// "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2;"

		// + " Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; "

		// + ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152;"

		// + " .NET CLR 3.5.30729)");

		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");

		conn.setRequestProperty("Connection", "Keep-Alive");

		conn.connect();

		printResponseHeader(conn);

	}

	private static void printResponseHeader(HttpURLConnection http)
			throws UnsupportedEncodingException {

		Map<String, String> header = getHttpResponseHeader(http);

		for (Map.Entry<String, String> entry : header.entrySet()) {

			String key = entry.getKey() != null ? entry.getKey() + ":" : "";

			System.out.println(key + entry.getValue());

		}

	}

	private static Map<String, String> getHttpResponseHeader(

	HttpURLConnection http) throws UnsupportedEncodingException {

		Map<String, String> header = new LinkedHashMap<String, String>();

		for (int i = 0;; i++) {

			String mine = http.getHeaderField(i);

			if (mine == null)

				break;

			header.put(http.getHeaderFieldKey(i), mine);

		}

		return header;

	}

}