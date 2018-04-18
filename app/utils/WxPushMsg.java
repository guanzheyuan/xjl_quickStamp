package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.comm.Sign;
import models.modules.mobile.WxServer;
import models.modules.mobile.WxUser;
import net.sf.json.JSONObject;
import play.Logger;

public class WxPushMsg {

	/**
	 * 模版消息
	 * @param title
	 */
	public static void wxMsgPusheTmplate(String templateId,String url,Map<String, Object> mapData,String wxopenid){
		WxServer service = new WxServer();
		int pageIndex = 1;
		int pageSize = 999999999;
		Map<String, String> condition = new HashMap<>();
		List<WxServer> data  = (List<WxServer>) service.queryWxServerListByPage(condition, pageIndex, pageSize).get("data");
		if(null != data && data.size() >0){
			Logger.info("得到服务开发者ID："+data.get(0).appId);
			String appId  = data.get(0).appId;
			String appSecret = data.get(0).appSecret;
			String access_token = Sign.getAccessToken(appId, appSecret,true);
			System.out.println("access_token:" + access_token);
			Logger.info("msgPush access_token:"+access_token);
			String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ access_token;
			Logger.info("msgPush URL:"+access_token);
			//组装收信人标记
			Map<String, Object> map = new HashMap<String, Object>();
			//收信人标识
			map.put("touser",wxopenid);
			//模板标识
			map.put("template_id",templateId);
			//点击模板跳转地址
			map.put("url", url);
			//组装数据
			map.put("data", mapData);
			Logger.info("得到参数："+mapData);
			JSONObject json = JSONObject.fromObject(map);
			Logger.info("msgPush jsonRequest:"+json);
			JSONObject jsonReturn = HttpClientUtil.invoke(URL, "POST", json);
			Logger.info("msgPush jsonReturn:"+jsonReturn);
		}
	}
}
