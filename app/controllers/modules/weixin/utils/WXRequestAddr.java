package controllers.modules.weixin.utils;

public class WXRequestAddr {

	/**
	 * 发送消息
	 */
	public static final String SEND_CUSTOM_MSG = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

	/**
	 * 群发消息
	 */
	public static final String BATCH_CUSTOM_MSG = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=%s";
	/**
	 * 用户管理
	 */
	public static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

	/**
	 * 创建菜单
	 */
	public static final String POST_MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
	/**
	 * 查询菜单
	 */
	public static String POST_MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
	/**
	 * 删除菜单
	 */
	public static String POST_MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";

	/** 创建二维码ticket **/
	public static String POST_QR_TICKET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

	/** 获取二维码 **/
	public static String POST_QR_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	
	/** 获得JS API调用的ticket **/
	public static String POST_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	
	public static String GET_DOWNLOAD_MEDIA ="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";


	public static void main(String[] args) {

		System.out.println(String.format(GET_USER_INFO, 1, 2, 3));
	}
}
