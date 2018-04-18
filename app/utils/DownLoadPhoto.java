package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DownLoadPhoto {

	public static String downloadPhoto(String mediaId) {

		// GetExistAccessToken getExistAccessToken =
		// GetExistAccessToken.getInstance();
		// String accessToken = getExistAccessToken.getExistAccessToken();
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;

		// String url =
		// "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
		// + accessToken + "&media_id=" + mediaId;
		String url = "http://img11.360buyimg.com/n1/s130x130_jfs/t469/221/1443288214/90091/c0c0b784/54d9c91cN6ee5e44b.jpg";
		String photoType = null;
		String fullFilePathName = null;
		try {

			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();

			// 获取文件转化为byte流

			inputStream = http.getInputStream();
			//
			Map map = http.getHeaderFields();
			String content_type = (String) ((List) map.get("Content-Type"))
					.get(0);
			photoType = content_type.split("/")[1];
			System.out.println("photo_type:" + content_type);
			//
			byte[] data = new byte[1024];
			int len = 0;
			fullFilePathName = "f://" + new Date().getTime() + "." + photoType;
			fileOutputStream = new FileOutputStream(fullFilePathName);

			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return fullFilePathName;
	}

	public static void main(String[] args) {
		downloadPhoto("");
		Calendar ca = Calendar.getInstance() ;
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) ;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		System.out.println(year+","+month+","+day);
	}
	
}
