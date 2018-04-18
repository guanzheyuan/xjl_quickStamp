package controllers;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import controllers.comm.BaseController;
import models.modules.mobile.XjlDwFile;
import models.modules.mobile.XjlStampSet;
import net.sf.json.JSONObject;
import play.Logger;
import utils.AlipayConfig;

public class AliPay extends BaseController {

	
	/**
	 * 阿里下单PC
	 * @throws AlipayApiException 
	 */
	public static void doUnifiedOrder() throws AlipayApiException{
		 AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,AlipayConfig.APPID,AlipayConfig.RSA_PRIVATE_KEY,AlipayConfig.FORMAT,AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE); //获得初始化的AlipayClient
		 AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		 String setId = params.get("paramId");
		 XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(setId));
		 if(null != xjlStampSet){
			 String returnUrl = "http://dw201709.com/sp?flag=PC";
			 String notify_url = "http://dw201709.com/sp?flag=PC";
			 alipayRequest.setReturnUrl(returnUrl);//前台通知
			 alipayRequest.setNotifyUrl(notify_url);//后台回调
			 XjlDwFile xjlDwFile = XjlDwFile.queryXjlDwFileById(xjlStampSet.fileId);
			 JSONObject bizContent = new JSONObject();
			 bizContent.put("out_trade_no", xjlStampSet.outTradeNo);
			 bizContent.put("total_amount", xjlStampSet.totalPrice);
			 bizContent.put("subject",xjlDwFile.realName);
			 bizContent.put("seller_id",AlipayConfig.PID);
			 bizContent.put("product_code","FAST_INSTANT_TRADE_PAY");
			 bizContent.put("body", "打印支付");
			 String biz = bizContent.toString().replaceAll("\"", "'");
			 alipayRequest.setBizContent(biz);
			 Logger.info("--------业务参数"+alipayRequest.getBizContent());
			 String form ="FAIL";
			 try {
				  form  = alipayClient.pageExecute(alipayRequest).getBody();
				  Logger.info("-----------form"+form);
			 }catch(AlipayApiException e){
				 Logger.debug("支付宝构造表单失败"+e.getMessage());
			 }
			 ok(form);
		 }
	}
}
