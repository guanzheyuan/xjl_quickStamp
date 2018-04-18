package utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AlipayConfig {
	// 商户appid
		public static String APPID = "2018041102536780";
		// 私钥 pkcs8格式的
		public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDI5KsKGe76EGoywKYXz7vS+9LD/l9jWsYjGOjv4g1rJI0LSzp4YdRFTr3PfKqTH95oCEOnJZq/4Z7EpExPWFlOGnnjyhEXqfEToeMvfGOh0y4nK3Fx88BzAO4MmHt/YCiGn5sSwlWNkCMk2ONOejDnTE4JW18y5rt2DYEoe3Kdg1nxVFmH7ZRMcgYZEPzvliUihDUQVkGezKoXjNmEPaaZcoRecA9+sXGrtHJSbKtNZdjD15NwPMuoSkqIYTAFNXbnry/XdwSvE65o3mFsEpGCYiJBFO5gB585S2qe18ZUMtVfhBScH9fK3H9UbXtEjoLCX0gjm48Qa/1tfLF1Ppd/AgMBAAECggEBAITPF3wAFQrgWzTeQ2+ud59Nt9K2kd0fH6oIR8Z+jRxt94il3SvEkQ6Ytcc7xZCqGuBvxjn0FFASGmIptAEoiTGnMzmyVoZsZOHzvjugYkWTVQm0ILWSzYG59aYCEHLu4RvO3DgUnXaO+YzcjblN8pKW0WhCLbd67idnWiGnKuiRxZQwRa8FmcyDzIqvUjus8Dettfhw8woFf7cooYV47OCJUac7lzwa0moiJRDOKOiJ4LDnBycXajo/pBPQ5g2R60AYk9iac0m9Mt97Dp9erPSHPHBYYDiPro69wWUvv/jBgAnAhrB97wom0MQpxDSeSKuhN383Xe5cdjU99WyZPIECgYEA/+eQcrD5SmOxdoBBX1bhZ+O+d5bz9ByF04wEuKR8N8PiLHkAMcjSGJCZlO3iJXKH/X0GAYGX1/Kjnnali8UJ+C1SZJNK5IsJEDqvEnVN58+GIzKr/rOGkp6SpOEbg4Zgp9WSTDlMQ3wmzinXXvUZAcEzJpsJ2uewXStVSFvAHSECgYEAyPfZ2OxG7D5xQpF/MWD66VgS6OPskbmUMjt0ge37KKdqcaGZDAA9KciOpPuVa5GnPbwGbcs/d+OWBP+QWvTbndHGE09Wie3L+21ogQUVTYQVxArxTjUXCmppEz7tl3j5rcAEeaoX7AoKLlGIh71tvn2uVxBMbGfS2AO7ySLcgJ8CgYAz6tbzbdQls3Qdhoe8v/ObEClkOcvkiqTb9lw+2aAZ8DPCsgnID+YP9cYy4NAwqLvOQb8mLxSPSzrGu2t6n88V+pvsq7VWCJ8eG8nJBON5VDDykyUx11pGBMXbjY/3bxvM8RdtP3KpflahHPwE19Tq9jdabbniH384Z8I1m+euAQKBgQCqNuLYjCb16V1cmIc2d4GNKf+6KXIxaxFOCnV/puVXZZUOcWSDeaBUR34p1ryTw2X3L83yKHe1P5nM2fiWUVXBse8mY8hUQojGh8A/puKIKRuWU/5V0zmUpNQibOYnqVswArbHpX1leots97m/3n2ZHjIPo8pW9wjp1R+0GEbS2QKBgC0ourRdHMb6AGUOcAcIT/dQZBrHJDlji7vCPaGh2+9Ey4LXK7VkwMPhfqph4w+4lUEFaHsDlT5KP3kLJx6OuV5NkuDZBo0YNKUQN96y37LNYiabPznK/AfTYVBMQJL29Uwsm1DnQLUSq10n1QCdvqkC02JXuSzFxaN4mLik1y78";
		// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		public static String notify_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/notify_url.jsp";
		// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
		public static String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";
		// 请求网关地址
		public static String URL = "https://openapi.alipay.com/gateway.do";
		// 编码
		public static String CHARSET = "UTF-8";
		// 返回格式
		public static String FORMAT = "json";
		// 支付宝公钥
		public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyOSrChnu+hBqMsCmF8+70vvSw/5fY1rGIxjo7+INaySNC0s6eGHURU69z3yqkx/eaAhDpyWav+GexKRMT1hZThp548oRF6nxE6HjL3xjodMuJytxcfPAcwDuDJh7f2Aohp+bEsJVjZAjJNjjTnow50xOCVtfMua7dg2BKHtynYNZ8VRZh+2UTHIGGRD875YlIoQ1EFZBnsyqF4zZhD2mmXKEXnAPfrFxq7RyUmyrTWXYw9eTcDzLqEpKiGEwBTV2568v13cErxOuaN5hbBKRgmIiQRTuYAefOUtqntfGVDLVX4QUnB/Xytx/VG17RI6Cwl9II5uPEGv9bXyxdT6XfwIDAQAB";
		// 日志记录目录
		public static String log_path = "/log";
		// RSA2
		public static String SIGNTYPE = "RSA2";
		
		//收款方账号
		public static final String SELLER_ID="spkj1110@163.com";
		
		public static final String PID="2088031504627637";
		 
         
}
