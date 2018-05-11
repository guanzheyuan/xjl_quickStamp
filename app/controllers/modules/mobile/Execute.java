package controllers.modules.mobile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.common.io.Files;
import com.mysql.fabric.xmlrpc.base.Array;

import controllers.comm.SessionInfo;
import controllers.modules.mobile.bo.WxUserBo;
import controllers.modules.mobile.bo.XjlDwFileBo;
import controllers.modules.mobile.bo.XjlScConfigBo;
import controllers.modules.mobile.bo.XjlScLogBo;
import controllers.modules.mobile.bo.XjlScToiletInfoBo;
import controllers.modules.mobile.bo.XjlStampSetBo;
import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxServer;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwFile;
import models.modules.mobile.XjlScConfig;
import models.modules.mobile.XjlScLog;
import models.modules.mobile.XjlScToiletInfo;
import models.modules.mobile.XjlStampSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import play.Logger;
import play.Play;
import play.cache.Cache;
import utils.CreateHtml;
import utils.CreateQRCode;
import utils.DateUtil;
import utils.FileUtil;
import utils.HttpClientUtil;
import utils.Preview;
import utils.StringUtil;
import utils.WechatTicket;
import utils.WxPushMsg;

public class Execute  extends MobileFilter {
	private final static String WX_CODE = "gh_00e09da4fab0";
	private final static String UPLOAD_URL="public/tmp/upload/";
	private final static String UPLOAD_ROOT_DIR="public";
	private final static String ROOT="/home/lls/";
	
	private final static String SUFFIX_DOC="doc";
	private final static String SUFFIX_DOCX="docx";
	private final static String SUFFIX_XLS="xls";
	private final static String SUFFIX_XLSX="xlsx";
	private final static String SUFFIX_PDF="pdf";
	
	
	private final static String TOLIETLISTURL="http://47.98.200.5:8080/watersaving/api/toilet/list";
	/**
	 * 初始化微信登录操作
	 */
	public static void initWeChat(){
		 String code = params.get("code");
		 Logger.info("得到code"+code);
	}
	
	/**
	 * PC扫码登录
	 */
	public static void toScanQrCode(){
		Logger.info("..................................进入扫码跳转页面");
		String code = params.get("code");
		//第一步 得到应用id和秘钥
		WxServer wxServer = WxServer.getServerByServerid(WX_CODE);
		if(null!=wxServer){
			//第二步 得到token和 openid
			String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxServer.appletAppId+"&secret="+wxServer.appletAppSecret+"&code="+code+"&grant_type=authorization_code";
			JSONObject json = HttpClientUtil.invoke(url, "POST", null);
			if(null != json){
				Logger.info("..............................."+json);
				String token = json.getString("access_token");
				String openId = json.getString("openid");
				WxUser 	wxUser = new WxUser();
				wxUser = WxUser.getUserByOpenId(openId);
				if(null == wxUser){
					url="https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openId;
					json = HttpClientUtil.invoke(url, "POST", null);
					if(null != json && json.containsKey("openid")){
						Logger.info("用户信息....................."+json);
						WxUser _wxUser = new WxUser();
						_wxUser.wxOpenId = json.getString("openid");
						_wxUser.nickName = json.getString("nickname");
						_wxUser.sex = json.getString("sex");
						_wxUser.sex = "1".equals(_wxUser.sex)?"男":"女";
						_wxUser.language = json.getString("language");
						_wxUser.city = json.getString("city");
						_wxUser.province = json.getString("province"); 
						_wxUser.country = json.getString("country");
						_wxUser.headImgUrl = getWXSmallHeadImage(json.getString("headimgurl"));
						_wxUser.isConcerned = "Y";
						_wxUser.openIdChanncel="web_grant";
						_wxUser.upOpenidTime= DateUtil.getNowDate();
						_wxUser.userType="2";
						_wxUser = WxUserBo.save(_wxUser);
						Logger.info("用户保存信息....................."+_wxUser.wxOpenId);
						_wxUser = WxUser.getFindByOpenId(openId);
		        		SessionInfo sessionInfo=new SessionInfo();
						sessionInfo.setWxUser(_wxUser);
						setSessionInfo(sessionInfo);
					}
				}else{
					SessionInfo sessionInfo=new SessionInfo();
					sessionInfo.setWxUser(wxUser);
					setSessionInfo(sessionInfo);
				}
			}
		}
	}
	
	
	/**
	 * 附件数据列表
	 */
	public static void uploadListPage(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		WxUser wxUser =  getWXUser();
		Map condition = params.allSimple();
		condition.put("wxOpenId",wxUser.wxOpenId);
		//留取参数 传入用户标记
		Map ret = XjlDwFile.queryXjlDwFileListByPage(condition, pageIndex, pageSize);
		ok(ret);
	}
	/**
	 * 上传附件
	 * @param req
	 * @param res
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 */
	public static void doUploader(File file) throws IOException, ParserConfigurationException, TransformerException{
		Logger.info("进入........................................................................................."+file);
		WxUser wxUser =  getWXUser();
		//开头得到缓存用户信息
		String wxOpenId = wxUser.wxOpenId;
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		String savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR).child("tmp").child("upload").getRealFile().getAbsolutePath()
				+File.separator+year+File.separator+wxOpenId+File.separator;
		File folder = new File(savePath);
	    if(!folder.exists()){
	    	Logger.info("dir not exists, create it ......");
			boolean flag = folder.mkdirs();
			Logger.info("result:"+flag);
		} 
	    String savePath_1 = Play.roots.get(0).child(UPLOAD_ROOT_DIR).child("tmp").child("upload").getRealFile().getAbsolutePath()
				+File.separator+year+File.separator+wxOpenId+File.separator+"html"+File.separator;
	    folder = new File(savePath_1);
	    if(!folder.exists()){
	    	Logger.info("dir not exists, create it ......");
			boolean flag = folder.mkdirs();
			Logger.info("result:"+flag);
		} 
	    
	    String type = file.getName().substring(file.getName().lastIndexOf("."),file.getName().length());
	    String newName = UUID.randomUUID().toString()+type;
	    String outPath = UPLOAD_URL+year+File.separator+wxOpenId+File.separator+newName;
		Files.copy(file, Play.getFile(outPath));
		Logger.info("执行上传文件到服务器完成");
		XjlDwFile xjlDwFile = new XjlDwFile();
		xjlDwFile.fileName=newName;
		xjlDwFile.realName = file.getName();
		xjlDwFile.fileUrl=outPath;
		xjlDwFile.wxOpenId=wxOpenId;
		xjlDwFile.fileType=file.getName().substring(file.getName().lastIndexOf("."),file.getName().length());
		FileInputStream fis = new FileInputStream(new File(savePath+newName));
	    long s= fis.available();
	    Logger.info("long.........................................................."+s);
		xjlDwFile.fileSize = FileUtil.FormetFileSize(s);
		XjlDwFileBo.save(xjlDwFile);
		//输出html版本
		String htmlName = newName.substring(0,newName.lastIndexOf("."));
		outPath = ROOT+Play.roots.get(0)+File.separator+UPLOAD_URL+year+File.separator+wxOpenId+File.separator+"html"+File.separator+htmlName+".html";
		Logger.info("type...................................................."+type.substring(1, type.length()));
		toSwitchFile(type.substring(1, type.length()),savePath+newName,outPath);
	}
	
	/**
	 * 删除附件
	 */
	public static void delFile(){
		XjlDwFile xjlDwFile = XjlDwFile.queryXjlDwFileById(Long.parseLong(params.get("fileId")));
		Logger.info("see:"+ROOT+Play.roots.get(0)+File.separator+xjlDwFile.fileUrl);
		FileUtil.deleteFile(ROOT+Play.roots.get(0)+File.separator+xjlDwFile.fileUrl);
		XjlDwFile.modifyFile(Long.parseLong(params.get("fileId")));
	}
	
	
	/**
	 * 通过附件编号得到详细信息
	 */
	public static void queryFileById(){
		XjlDwFile xjlDwFile = XjlDwFile.queryXjlDwFileById(Long.parseLong(params.get("fileId")));
		if(null != xjlDwFile){
			xjlDwFile.htmlName = xjlDwFile.fileName.substring(0,xjlDwFile.fileName.lastIndexOf("."))+".html";
		}
		Logger.info(".................."+params.get("flag"));
		ok(xjlDwFile);
	}
	
	/**
	 * 提交打印配置
	 */
	public static void saveStampSet(){
		WxUser wxUser =  getWXUser();
		XjlStampSet xjlStampSet = new XjlStampSet();
		xjlStampSet.fileId = Long.parseLong(String.valueOf(params.get("fileId")));
		xjlStampSet.copies = params.get("copies");
		xjlStampSet.scope = params.get("scope");
		xjlStampSet.paperType = params.get("paperType");
		xjlStampSet.colour = params.get("colour");
		xjlStampSet.face = params.get("face");
		xjlStampSet.totalPrice = params.get("totalPrice");
		xjlStampSet.wxOpenId=wxUser.wxOpenId;
		xjlStampSet.outTradeNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
		xjlStampSet.payState="2";
		xjlStampSet = XjlStampSetBo.save(xjlStampSet);
		
		//生成订单二维码
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		String savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR).child("tmp").child("QRcode").getRealFile().getAbsolutePath()
				+File.separator+year+File.separator+wxUser.wxOpenId+File.separator+xjlStampSet.outTradeNo;
		File folder = new File(savePath);
	    if(!folder.exists()){
	    	Logger.info("dir not exists, create it ......");
			boolean flag = folder.mkdirs();
			Logger.info("result:"+flag);
		}
	    savePath +=File.separator+xjlStampSet.outTradeNo+".png";
	    CreateQRCode QRCode = new CreateQRCode();
	    QRCode.encoderQRCode(xjlStampSet.outTradeNo, savePath, "png");  
	    
		ok(xjlStampSet);
	}
	
	
	/**
	 * 验证是否登录
	 */
	public static void getSessionForUser(){
		WxUser wxUser =  getWXUser();
		ok(wxUser);
	}
	
	/**
	 * 主键获取
	 */
	public static void queryStampSetById(){
		XjlStampSet xjlStampSet = XjlStampSet.queryXjlStampSetById(Long.parseLong(String.valueOf(params.get("setId"))));
		ok(xjlStampSet);
	}
	
	/**
	 * 得到微信公众号二维码
	 */
	public static void getQrCodePath(){
		String path = WechatTicket.createQrCode();
		ok(path);
	}
	
	
	/**
	 * 查询订单
	 */
	public static void queryStampSet(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		WxUser wxUser =  getWXUser();
		condition.put("wxOpenId",wxUser.wxOpenId);
		Map map = XjlStampSet.query(condition, pageIndex, pageSize);
		ok(map);
	}
	
	/**
	 * 支付修改
	 */
	public static void modifyPayStatus(){
		Long setId = Long.parseLong(String.valueOf(params.get("setId")));
		int ret = XjlStampSet.modifyPayStatus(setId);
		ok(ret);
	}
	/**
	 * 
	 * @param suffix
	 * @param savePath
	 * @param outPath
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void toSwitchFile(String suffix,String savePath,String outPath) throws IOException, ParserConfigurationException, TransformerException{
		Logger.info("doc"+SUFFIX_DOC.equals(suffix.toLowerCase()));
		Logger.info("docx"+SUFFIX_DOCX.equals(suffix.toLowerCase()));
		if(SUFFIX_DOC.equals(suffix.toLowerCase())){
			Preview.wordToHtml(savePath, outPath);
		}else if(SUFFIX_DOCX.equals(suffix.toLowerCase())){
			Preview.word07ToHtml(savePath, outPath);
		}else if(SUFFIX_XLS.equals(suffix.toLowerCase()) || SUFFIX_XLSX.equals(suffix.toLowerCase())){
			String htmlUrl = Play.roots.get(0).child(UPLOAD_ROOT_DIR).child("tmp").child("upload").getRealFile().getAbsolutePath()+File.separator+"excel.html";
			boolean flag = FileUtil.copyFile(new File(htmlUrl),new File(outPath));
			if(flag){
				String body = Preview.readExcelToHtml(savePath,true);
				if(StringUtil.isNotEmpty(body)){
					CreateHtml.createHtml(outPath, body);
				}
			}
		}
	}
	
	/**
	 * 得到微信头像，使用小图
	 * @param headImageUrl
	 * @return
	 */
	private static String getWXSmallHeadImage(String headImageUrl){
		if (headImageUrl.endsWith("/0")){
			return headImageUrl.substring(0, headImageUrl.length()-2)+"/132";
		} else {
			return headImageUrl;
		}
	}
	private static void setSessionInfo(SessionInfo sessionInfo){
		Cache.set(getSessionKey(), sessionInfo);
	}
	
	/**——----------------------------------RUSH---------------------------------------------——**/
	
	public static void saveScLog(){
		 String errorCode = params.get("errorCode");
		 String errorDesc = params.get("errorDesc");
		 String methodName = params.get("methodName");
		 XjlScLog xjlScLog = new XjlScLog();
		 xjlScLog.errorCode = errorCode;
		 xjlScLog.errorDesc = errorDesc;
		 xjlScLog.methodName = methodName;
		 XjlScLogBo.save(xjlScLog);
	}
	
	public static void queryScLog(){
		Map<String,String> condition = new HashMap<>();
		Map map = XjlScLog.query(condition,1,1000000);
		ok(map);
	}
	
	/**
	 * 配置时间段
	 */
	public static void timeQuantumConfig(){
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String interval = params.get("interval");
		XjlScConfig xjlScConfig = new XjlScConfig();
		xjlScConfig.timeQuantum = startTime+"-"+endTime;
		xjlScConfig.interval = interval;
		XjlScConfigBo.save(xjlScConfig);
	}
	
	/**
	 * 配置时间段
	 */
	public static void modifyInterval(){
		String timeQuanTum = params.get("timeQuantum");
		String interval = params.get("interval");
		XjlScConfig.modifyInterval(timeQuanTum, interval);
	}
	
	/**
	 * 查询时间配置
	 */
	public static void queryScConfig(){
		Map<String,String> condition = new HashMap<>();
		Map map = XjlScConfig.query(condition, 1, 1000000);
		ok(map);
	}
	
	/**
	 * 查询未配置时间间隔
	 */
	public static void queryScConfigNotinterval(){
		Map<String,String> condition = new HashMap<>();
		Map map = XjlScConfig.queryNotTime(condition, 1, 1000000);
		ok(map);
	}
	
	/**
	 * 查询已配置间隔时间
	 */
	public static void queryInterval(){
		Map<String,String> condition = new HashMap<>();
		Map map =XjlScConfig.queryInterval(condition, 1, 1000000);
		ok(map);
	}
	
	/**
	 * 通过id查询单条信息
	 */
	public static void queryConfigById(){
		String id = params.get("id");
		XjlScConfig xjlconfig = XjlScConfig.queryScConfigById(Long.valueOf(id));
		ok(xjlconfig);
	}
	/**
	 * 根据id删除
	 */
	public static void modifyStatus(){
		String id = params.get("id");
		XjlScConfig.modifyStatus(Long.valueOf(id));
	}
	
	/**
	 * 根据id清空配置
	 */
	public static void modifyItervalIsNull(){
		String id = params.get("id");
		XjlScConfig.modifyIntervalIsNull(Long.valueOf(id));
	}
	
	/**
	 * 根据ID编辑时间段
	 */
	public static void modifyTimeQuanTunById(){
		String id = params.get("id");
		String timeQuanTum = params.get("timeQuanTum");
		XjlScConfig.modifyTimeQuanTumById(timeQuanTum,id);
	}
	
	/**
	 * 根据ID编辑间隔时间
	 */
	public static void modifyItervalById(){
		String id = params.get("id");
		String iterval = params.get("iterval");
		String timeQuanTum = params.get("timeQuanTum");
		XjlScConfig.modifyIntervalById(timeQuanTum,iterval, id);
	}
	
	
	public static void querytoilet(){
		Map<String,String> condition = new HashMap<>();
		Map ret = XjlScToiletInfo.query(condition, 1, 100000000);
		ok(ret);
	}
	/**
	 * 新增
	 */
	public static void saveToilet(){
		String toiletName = params.get("toiletName");
		XjlScToiletInfo xjlScToiletInfo = new XjlScToiletInfo();
		xjlScToiletInfo.toiletName = toiletName;
		xjlScToiletInfo.toiletCode="AAB";
		XjlScToiletInfoBo.save(xjlScToiletInfo);
	}
	
	
	/**
	 * 删除卫生间
	 */
	public static void delToilet(){
		String id = params.get("id");
		XjlScToiletInfo.delete(id);
	}
	
	/**
	 * 根据主键查询
	 */
	public static void queryToiletById(){
		String id = params.get("id");
		XjlScToiletInfo xjlScToiletInfo = XjlScToiletInfo.queryScToiletById(Long.valueOf(id));
		ok(xjlScToiletInfo);
	}
	
	/**
	 * 通过主键修改卫生间名称
	 */
	public static void modifyToilet(){
		String id = params.get("id");
		String toiletName = params.get("toiletName");
		XjlScToiletInfo.modifyToilet(toiletName, id);
	}
	
}
