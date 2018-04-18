package utils;

import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEW;

import net.sf.json.JSONObject;
import controllers.comm.Sign;

/**
 * @author 姓名 E-mail: 邮箱 Tel: 电话
 * @version 创建时间：2017-8-13 下午6:58:35
 * @describe 类说明
 */
public class WxInterFace {

	public static void main(String[] args) {
		wxTemplateMessage(12l);
	}

	public static void wxTemplateMessage(Long orderId) {
		// 南京德诺
		String appId = "wx5f29a981bf8c03c2";
		String appSecret = "706c26dab5d53f81517c4414cbfff565";
		String access_token = Sign.getAccessToken(appId, appSecret,false);
		System.out.println("access_token:" + access_token);
		String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ access_token;
		System.out.println("URL:" + URL);
		Map<String, Object> map = new HashMap<String, Object>();
		//收信人标识
		map.put("touser", "oxh64jl9bm3tjuLQpfshrDAroW3A");
		//模板标识
		map.put("template_id", "1R6Iwe4mZj0JOt9kWSFmUmUNhAPZDKS1ouAdgSrJQt0");
		//跳小程序所需数据，不需跳小程序可不用传该数据
		Map<String, Object> mapminiprogram = new HashMap<String, Object>();
		mapminiprogram.put("appid", "wxceb1d8fa67f34102");
		mapminiprogram.put("pagepath", "/pages/index/index");
		map.put("miniprogram", mapminiprogram);
		//点击模板跳转地址
		map.put("url", "http://shopmall.16fi.cn/wechat/malladmin/index");
		
		Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "南京德诺模板消息");
		mapDataSon.put("color", "#FF0000");
		mapData.put("first", mapDataSon);
		
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "您已经成功购买中国最甜芒果广西田东桂七芒特级大果");
		mapDataSon.put("color", "#A020F0");
		mapData.put("keyword1", mapDataSon);
		
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "2017-08-15 15:41:23");
		mapDataSon.put("color", "#A020F0");
		mapData.put("keyword2", mapDataSon);
		
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "找券小诀窍：点击底部菜单【领券中心】，在搜索搜索关键字，领你想要的券");
		mapDataSon.put("color", "#A020F0");
		mapData.put("remark", mapDataSon);
		
		map.put("data", mapData);
		JSONObject json = JSONObject.fromObject(map);
		System.out.println("jsonRequest:" + json);
		JSONObject jsonReturn = HttpClientUtil.invoke(URL, "POST", json);
		System.out.println("jsonReturn:" + jsonReturn);
	}

}
