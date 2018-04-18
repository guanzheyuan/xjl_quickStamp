package utils;

import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;

public class MyConfig implements WXPayConfig {

	@Override
	public String getAppID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMchID() {
		// TODO Auto-generated method stub
		return null;
	}

}
