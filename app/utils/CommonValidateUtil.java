package utils;

 /**
 * @author    liminzhi   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-22 下午1:54:07
 * @describe  常用的一些检验类
 */
public class CommonValidateUtil {

	/***
	 * 较验是否是手机
	 * @param userAgent 客户端获取的heads.get("user-agent")|request.headers.get("user-agent")
	 * @return true:是手机端，false:不是手机端
	 */
	public static boolean isMobile(String userAgent){
		
		return userAgent.toLowerCase().indexOf("mobile")!=-1;
	}
	/**
	 * 较验是否是ipad
	 * @param userAgent
	 * @return
	 */
	public static boolean isPad(String userAgent){
		return userAgent.toLowerCase().indexOf("iPad")!=-1;
	}
	/**
	 * 较验是否是pc
	 * @param userAgent
	 * @return
	 */
	public static boolean isPc(String userAgent){
		if(isMobile(userAgent)||isPad(userAgent)){
			return false;
		}else{
			return false;
		}
	}
	/***
	 * 较验是否是在微信中
	 * @param userAgent
	 * @return 是微信返回true，不是微信返回false
	 */
	public static boolean isWechat(String userAgent){
		return userAgent.indexOf("MicroMessenger")!=-1;
	}
	
	public static String isWhatEqu(String userAgent){
		String device;
		if (userAgent.contains("Android")) {
			device = "Android";
		} else if (userAgent.contains("iPhone")) {
			device = "iPhone";
		} else if (userAgent.contains("iPad")) {
			device = "iPad";
		} else {
			device = "unknow";
		}
		return device;
	}

	/***
	 * 判断字符串是否是由数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
		java.util.regex.Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	public static void main(String []args){
		System.out.println(isNumeric("2342342342"));
	}
}
 