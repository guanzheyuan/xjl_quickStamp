package controllers.modules.weixin.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import models.modules.mobile.WxServer;
import net.sf.json.JSONObject;
import play.Logger;
import play.cache.Cache;

public class AccessTokenHolder {

	public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s"
			+ "&secret=%s";

	public static String getAccessTokenByAppId(String appId) {
		WxServer server = WxServer.find("from WxServer where appId=?", appId)
				.first();
		return getAccessToken(server.wxCode);
	}

	public static String getAccessToken(String wxCode) {

		Logger.info("serverId = " + wxCode);
		String token = null;//(String) Cache.get(wxCode + "_token");
		Logger.info("getcache"+Cache.get(wxCode + "_token"));
		if (token == null) {
			WxServer server = WxServer.getServerByServerid(wxCode);
			if (server != null) {
				String appId = server.appId;
				String appSecret = server.appSecret;



				token = getToken(appId, appSecret);
				Logger.info("---appId----:"+appId);
				Logger.info("---appSecret----:"+appSecret);
				Logger.info("---settoken----:"+token);
				Cache.set(wxCode + "_token", token, "2h");
			}
		}
//		play.Logger.info("------token is: %s", token);
		Logger.info("returntoken:"+token);
		return token;
	}

	public static String getAccessTokenByOpenId(String openId) {
		String wxCode = "";

		WxServer server = WxServer.findById(1l);
		Logger.info("server:"+server.wxCode);
		wxCode = server.wxCode;
		return getAccessToken(wxCode);
	}

		public static String getToken(String appid, String appsecret) {
			String accessToken = null;
			try {
				String requestStr = String.format(GET_ACCESS_TOKEN, appid,
						appsecret);

				Logger.info("GET_ACCESS_TOKEN STR  =\n " + requestStr);

				URL urlGet = new URL(requestStr);
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
				InputStream is = http.getInputStream();
				int size = is.available();
				byte[] jsonBytes = new byte[size];
				is.read(jsonBytes);
				String message = new String(jsonBytes, "UTF-8");
				JSONObject tokenJson = JSONObject.fromObject(message);
				Logger.info("----tokenJson----"+tokenJson);

				accessToken = tokenJson.getString("access_token");

				Logger.info("<<TOKEN result = >>token response json  =\n "
						+ tokenJson);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error(e,"");
				Logger.debug("<<TOKEN result = >> Fail to get token");
			}
			return accessToken;
		}
}