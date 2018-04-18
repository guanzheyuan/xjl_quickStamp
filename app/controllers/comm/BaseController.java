package controllers.comm;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Box.Filler;

import net.sf.json.JSONObject;

import org.nutz.lang.Files;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import utils.FileUploadPathUtil;
import utils.HttpClientUtil;
import utils.StringUtil;
import utils.imageUtils;
import controllers.modules.weixin.utils.AccessTokenHolder;

public class BaseController extends Controller {
	//数据库名称 ｜oracle ｜postgresql
	static String dataBaseName = "";
    @Before
    static void checkBefore() {
		try {
			if(StringUtil.isEmpty(dataBaseName)){
				dataBaseName = play.db.DB.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
			}
		} catch (SQLException e) {
			Logger.error("------------ERROR------------"+e.getMessage());
			e.printStackTrace();
		}
    }
    
    @After
    static void checkAfter(){
    	
    }

    protected static String getWebRoot() {
        String webRoot = "";
        String host = request.remoteAddress;
        int port = request.port;
        String ctx = Play.ctxPath;
        webRoot += host;
        if (port != 80) {
            webRoot += ":" + port;
        }
        if (ctx == null || "/".equals(ctx)) {
            ctx = "";
        }
        webRoot += ctx;
        return webRoot;
    }

    /**
     * for remoteCall result
     *
     * @param rs
     */
    protected static void ok(JSResult rs) {
        renderJSON(rs);
    }

    protected static void ok() {
        renderJSON(new JSResult());
    }

    protected static void ok(Object data) {
        JSResult js = new JSResult();
        js.setData(data);
        renderJSON(js, new TimestampTypeAdapter());
    }

    protected static void nok(Object data) {
        JSResult js = new JSResult();
        js.setRet("-1");
        js.setDesc(data.toString());
        // js.setData(data);
        renderJSON(js);
    }

    protected static void okText(Object data) {
        renderText(data.toString());
    }


    /***
     * 上传图片，如果传了oldFileDelete参数，则删除旧的文件，如果不传，则只上传文件
     * @param fileToUpload 要上传的文件
     * @param oldFileDelete 旧图片的路径
     * @param userId 上传人标识
     * @param advertType 图片文件类型
     */
    public static void uploadAppletImage(File name,String imageType) throws IOException {
        String fileUrlPath = "";//上传文件在服务器上保存的相对路径 /home/shopmall/mall/_web_/images/wx_head/2017/8/24/1503566642424.png
    	String savePath = null;  //上传图片保存的路径及文件名
  	    savePath = FileUploadPathUtil.getUploadPath("wx_head");
  		Logger.info("savePathsavePathsavePath："+savePath);
   		Logger.info("imageTypeimageTypeimageTypeimageType："+imageType);
   		
   	    //裁剪方式,w:按宽，h:按高
        String scaleMethod = "w";//"h"
        Integer width = 0;
        Integer height = 0;
		if(("wx_head").equals(imageType)){
			//商户Logo
			scaleMethod = "w";
			width = 750 ;
			height = 750;
		}else{
			Image image = ImageIO.read(name);
			width = image.getWidth(null);
			height = image.getHeight(null);
		}
   		if(name != null) {
            try {
            	// 取上传图片文件的后缀名
                String suffix = Files.getSuffixName(name).toLowerCase();
                //生成随机文件名
                String fileName = new Date().getTime() + "." + suffix;
                File filePath2 = new File(savePath + fileName);
                Files.copyFile(name, filePath2);
                fileUrlPath = filePath2.getAbsolutePath();

                //裁剪生成目标图片
            	String destFileName = new Date().getTime() + "." + suffix;
            	String destFileNamePath = savePath + destFileName;
            	//裁剪
            	boolean flag = cutPicture(fileUrlPath, destFileNamePath, scaleMethod, width, height);
            	if(flag){
//            		//裁剪成功
//            		//删除原图
            		File deleteFile = new File(fileUrlPath);
            		if(deleteFile.exists()){
            			flag = deleteFile.delete();
            			if(flag){
//            				//原图删除成功
                    		fileUrlPath = destFileNamePath;
                    		Logger.info("原图删除成功，裁剪后的图片："+destFileNamePath);
            			}else{
//            				//原图删除失败，删除裁剪的图片
            				deleteFile = new File(destFileNamePath);
            				if(deleteFile.exists()){
           					   deleteFile.delete();
            				}
            			}
            		}else{
//            			//原图丢失，则使用裁剪的图片
                		fileUrlPath = destFileNamePath;
                		Logger.info("原图丢失，则使用裁剪的图片："+fileUrlPath);
            		}
            	}
            	
            	
            	
            } catch (Exception e) {
    	        Logger.error("上传error!",e.toString());
                e.printStackTrace();
            }
        }
        try {
            if (fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR)>0) {
            	fileUrlPath = File.separator+ fileUrlPath.substring(fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));
			}
		} catch (Exception e) {
	        Logger.error("文件上传失败 ",e.toString());
		}

        //如果上传图片的时候指定了旧图片（或文件），则上传成功后删除原文件
        //(oldFileDelete);
      
        //记录上传的图片
       // ImagesBo.saveImage(fileUrlPath,null,"wx_head","PHONE");
  		Logger.info("fileUrlPathfileUrlPath："+fileUrlPath);
        renderText(fileUrlPath);
    }
    public static void upload(File fileToUpload,String oldFileDelete,String userId,String imageType) throws IOException {
    	Logger.info("imageType："+imageType);
    	//上传图片保存的路径及文件名
        String savePath = null;
        if (userId!=null) {
            //上传图片的时候如果传入上传人的标识，则生成的文件路径中要包含上传人标识
        	savePath = FileUploadPathUtil.getUploadPath(userId,imageType);
		}else {
			savePath = FileUploadPathUtil.getUploadPath(imageType);
		}
        //上传文件在服务器上保存的相对路径
        String fileUrlPath = "";
        //裁剪方式,w:按宽，h:按高
        String scaleMethod = "";//"h"
        Integer width = 0;
        Integer height = 0;
		if(("vno_logo").equals(imageType)){
			//商户Logo
			scaleMethod = "w";
			width = 350 ;
			height = 350;
		}else if(("avdert_slides").equals(imageType)){
			//幻灯片广告管理
			scaleMethod = "w";
			width = 750;
			height = 376;
		}else if(("avdert_slides").equals(imageType)){
			//LOGO广告管理
			scaleMethod = "w";
			width = 750;
			height = 200;
		}else if(("goods_pic").equals(imageType)){
			//商品图片
			scaleMethod = "w";
			width = 750;
			height = 750;
		}else if(("btn_pic").equals(imageType)){
			//首页基本介绍左\右按钮图片
			scaleMethod = "w";
			width = 352;
			height = 170;
		}else if(("class_icon").equals(imageType)){
			//首页基本介绍左\右按钮图片
			scaleMethod = "w";
			width = 100;
			height = 100;
		}else{
			Image image = ImageIO.read(fileToUpload);
			width = image.getWidth(null);
			height = image.getHeight(null);
		}
		if (fileToUpload != null) {
            try {
            	// 取上传图片文件的后缀名
                String suffix = Files.getSuffixName(fileToUpload).toLowerCase();
                //生成随机文件名
                String fileName = new Date().getTime() + "." + suffix;
                File filePath = new File(savePath + fileName);
                Files.copyFile(fileToUpload, filePath);
                fileUrlPath = filePath.getAbsolutePath();

                //裁剪生成目标图片
            	String destFileName = new Date().getTime() + "." + suffix;
            	String destFileNamePath = savePath + destFileName;
            	//裁剪
            	boolean flag = cutPicture(fileUrlPath, destFileNamePath, scaleMethod, width, height);
            	if(flag){
            		//裁剪成功
            		//删除原图
            		File deleteFile = new File(fileUrlPath);
            		if(deleteFile.exists()){
            			flag = deleteFile.delete();
            			if(flag){
            				//原图删除成功
                    		fileUrlPath = destFileNamePath;
            			}else{
            				//原图删除失败，删除裁剪的图片
            				deleteFile = new File(destFileNamePath);
            				if(deleteFile.exists()){
            					deleteFile.delete();
            				}
            			}
            		}else{
            			//原图丢失，则使用裁剪的图片
                		fileUrlPath = destFileNamePath;
            		}
            	}
            } catch (Exception e) {
    	        Logger.error("上传error!",e.toString());
                e.printStackTrace();
            }
        }
        try {
            if (fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR)>0) {
            	fileUrlPath = File.separator+ fileUrlPath.substring(fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));
			}
		} catch (Exception e) {
	        Logger.error("文件上传失败 ",e.toString());
		}

        //如果上传图片的时候指定了旧图片（或文件），则上传成功后删除原文件
        deleteFile(oldFileDelete);
      
        //记录上传的图片
       // ImagesBo.saveImage(fileUrlPath,userId,imageType,"PC");
        renderText(fileUrlPath);
    }
    /**
     * 按指定方式裁剪图片
     * @param srcFilePath 原图绝对路径
     * @param destFileName 生成的裁剪图片绝对路径
     * @param scaleMethod 裁剪方式，w:按宽，h:按高
     * @param width 按宽方式裁剪最终裁剪生成图片的宽
     * @param height 按高方式裁剪最终裁剪生成图片的度
     * @return 返回裁剪后的图片路径
     * @throws Exception
     */
    public static boolean cutPicture(String srcFilePath, String destFileName, String scaleMethod, Integer width, Integer height) throws Exception {
		boolean flag=false;
		File f = new File(srcFilePath);
		int intWidth = 0;
		int intHeigth = 0;
		if ("w".equals(scaleMethod)){
			intWidth = width;
			flag=CutPictureUtil.compressImageByWidth(f, destFileName, intWidth, true);
			Logger.debug("generate [" + destFileName + "] success scan by width...liminzhi");
		}else{
			intHeigth = height;
			flag=CutPictureUtil.compressImageByHeigth(f, destFileName, intHeigth, true);
			Logger.debug("generate [" + destFileName + "] success scan by height...");
		}
		return flag;
	}
    
    protected static void deleteFile(String oldFileDelete){
    	if (oldFileDelete!=null) {
        	//如果旧图片不是系统默认图片，则执行删除操作
        	if (!oldFileDelete.startsWith("/public/")) {
    			//要删除的文件真实绝对路径
        		String oldFileDeleteRealPath = FileUploadPathUtil.getRealPath() + oldFileDelete;
        		Logger.info("要删除的文件： ",oldFileDeleteRealPath);
            	//System.out.println("要删除的文件路径："+oldFileDelete);
            	File delfile = new File(oldFileDeleteRealPath);
        		if (delfile.exists()){
        	    	if(delfile.delete()){
        	    		//记录删除的图片
        	    		Logger.info("删除文件成功： "+oldFileDelete);
            			//ImagesBo.deleteImage(oldFileDelete);
        	    	}else {
        		        Logger.error("文件删除失败，文件相对路径： "+oldFileDelete);
					}
        		}else{
        	    	//System.out.println("要删除的文件不存在no");
        		}
			}
		}
    }
    /***
     * 在线编辑器（Ueditor）上传图片
     * @param fileToUpload
     */
    public static void upload4Ueditor(File fileToUpload) {
        Logger.info("-----------------fileToUpload:"+fileToUpload.isFile());
        HashMap hm = new HashMap();
        String savePath = FileUploadPathUtil.getUploadPath();
        String fileUrlPath = "";
        if (fileToUpload != null) {
            try {
                // start
                String suffix = Files.getSuffixName(fileToUpload).toLowerCase();
                String fileName = new Date().getTime() + "." + suffix;
                // end
                File filePath = new File(savePath + fileName);
                Files.copyFile(fileToUpload, filePath);
                fileUrlPath = filePath.getAbsolutePath();
                Logger.info("-----------------fileUrlPath:"+fileUrlPath);
              //裁剪生成目标图片
            	String destFileName = new Date().getTime() + "." + suffix;
            	String destFileNamePath = savePath + destFileName;
                Image image = ImageIO.read(filePath);
                String scaleMethod = "w";
                int width = image.getWidth(null);
            	width = width>750 ? 750 : width;
                int height = image.getHeight(null);
            	//裁剪
            	boolean flag = cutPicture(fileUrlPath, destFileNamePath, scaleMethod, width, height);
            	if(flag){
            		//裁剪成功
            		//删除原图
            		File deleteFile = new File(fileUrlPath);
            		if(deleteFile.exists()){
            			flag = deleteFile.delete();
            			if(flag){
            				//原图删除成功
                    		fileUrlPath = destFileNamePath;
            			}else{
            				//原图删除失败，删除裁剪的图片
            				deleteFile = new File(destFileNamePath);
            				if(deleteFile.exists()){
            					deleteFile.delete();
            				}
            			}
            		}else{
            			//原图丢失，则使用裁剪的图片
                		fileUrlPath = destFileNamePath;
            		}
            	}
            	
            	if (fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR)>0) {
                    fileUrlPath = File.separator+ fileUrlPath.substring(fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));
    			}
            	//记录是谁上传的-在线编辑器里
            	String userId = null;
            	if(Cache.get(session.getId()+"_admin")!=null){
        			//已经登录
            		SessionInfo sessionInfo = (SessionInfo) Cache.get(session.getId()+"_admin");
  
                    sessionInfo = null;
        		}

                //记录上传的图片
               // ImagesBo.saveImage(fileUrlPath,null,"Ueditor","PC");
                hm.put("error", 0);
                hm.put("message", "上传成功!");
                hm.put("url", fileUrlPath);

            } catch (IOException e) {
                hm.put("error", 1);
                hm.put("message", "上传error!");
                e.printStackTrace();
            } catch (Exception e) {
                hm.put("error", 1);
                hm.put("message", "上传error!");
				e.printStackTrace();
			}
        }
        renderJSON(hm);
    }
    /***
     * 在线编辑器（Kindeditor）上传图片
     * @param fileToUpload
     */
    public static void upload4Kindeditor(File fileToUpload) {
        HashMap hm = new HashMap();
        String savePath = FileUploadPathUtil.getUploadPath();
        String fileUrlPath = "";
        if (fileToUpload != null) {
            try {
                // start
                String suffix = Files.getSuffixName(fileToUpload).toLowerCase();
                String fileName = new Date().getTime() + "." + suffix;
                // end
                File filePath = new File(savePath + fileName);
                Files.copyFile(fileToUpload, filePath);
                fileUrlPath = filePath.getAbsolutePath();
                
                //裁剪生成目标图片
            	String destFileName = new Date().getTime() + "." + suffix;
            	String destFileNamePath = savePath + destFileName;
                Image image = ImageIO.read(filePath);
                String scaleMethod = "w";
                int width = image.getWidth(null);
            	width = width>750 ? 750 : width;
                int height = image.getHeight(null);
            	//裁剪
            	boolean flag = cutPicture(fileUrlPath, destFileNamePath, scaleMethod, width, height);
            	if(flag){
            		//裁剪成功
            		//删除原图
            		File deleteFile = new File(fileUrlPath);
            		if(deleteFile.exists()){
            			flag = deleteFile.delete();
            			if(flag){
            				//原图删除成功
                    		fileUrlPath = destFileNamePath;
            			}else{
            				//原图删除失败，删除裁剪的图片
            				deleteFile = new File(destFileNamePath);
            				if(deleteFile.exists()){
            					deleteFile.delete();
            				}
            			}
            		}else{
            			//原图丢失，则使用裁剪的图片
                		fileUrlPath = destFileNamePath;
            		}
            	}
            	
            	if (fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR)>0) {
                    fileUrlPath = File.separator+ fileUrlPath.substring(fileUrlPath.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));
    			}
            	//记录是谁上传的-在线编辑器里
            	String userId = null;
            	if(Cache.get(session.getId()+"_admin")!=null){
        			//已经登录
            		SessionInfo sessionInfo = (SessionInfo) Cache.get(session.getId()+"_admin");
                    sessionInfo = null;
        		}
            	
                //记录上传的图片
               // ImagesBo.saveImage(fileUrlPath,userId,"Kindeditor","PC");
                hm.put("error", 0);
                hm.put("message", "上传成功!");
                hm.put("url", fileUrlPath);

            } catch (IOException e) {
                hm.put("error", 1);
                hm.put("message", "上传error!");
                e.printStackTrace();
            } catch (Exception e) {
                hm.put("error", 1);
                hm.put("message", "上传error!");
                e.printStackTrace();
            }
        }
        renderJSON(hm);
    }

    /**
     * 文件下载
     *
     * @param fullFilePath 如：/apk/WifiSdkSampleCMCCTest.apk
     */
    public static void download(String fullFilePath) {
        File apkfile = Play.getFile(fullFilePath);
        System.out.println("apkfile.getName()"+apkfile.getName());
        renderBinary(apkfile, apkfile.getName());
    }

    public static void main(String[] args) {
        String fileUrlPath = "/data/ppcar/ppcar_v1.0/_web_/images/2015/5/13/1431505220775.jpg";
        fileUrlPath = File.separator
                + fileUrlPath.substring(fileUrlPath
                .indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));

        System.out.println(fileUrlPath);

    }

    public static String OAuthopenId() {
        String openId = params.get("openId");
        String vnoId = "1";

        Logger.info("+++openId=%s", openId);
        Logger.info("+++RegistController OAuthopenId vnoId=%s", vnoId);

        if (openId == null && vnoId == null) {
            Logger.fatal("[FATAL]vnoId and openId are both null");
            return null;
        }

        if (openId != null) {
            return openId;
        }

       // WxServer server = WxServer.findById(Long.parseLong(vnoId));
        //String appId = server.appId;
        //String secret = server.appSecret;
        
        String appId = null;
        String secret = null;

        Logger.info("appId = " + appId);
        Logger.info("secret = " + secret);

        Logger.info("没获取到openId,后台自动重定向Oauth一次，并缓存到session中！");
        String code = params.get("code");
        String url = "http://" + request.domain + request.url;
        Logger.info("url = " + url);
        Logger.info("code = " + code);
        if (code == null) {
            String redirectURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + appId
                    + "&redirect_uri="
                    + url
                    + "&response_type=code&scope=snsapi_base&state=1"
                    + "#wechat_redirect";
            redirect(redirectURL);
        } else {
            url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                    + appId + "&secret=" + secret + "&code=" + code
                    + "&grant_type=authorization_code";
            JSONObject json = HttpClientUtil.invoke(url, "POST", null);
            Logger.info("openid json = " + json);
            if (json.containsKey("openid")) {
                openId = json.getString("openid");
                params.remove("code");// 一次性，用完作废
                params.put("openId", openId);
            }

            if (openId == null) {
                Logger.info("BaseController.java...获取openid失败，请重试重试");
                //renderText("获取openid失败，请重试重试");
            }
            Logger.info("openid = " + openId);
        }
        return openId;
    }
    public static String downloadPhoto(String mediaId, String openId,String type){
    	return downloadPhoto(mediaId,openId,type,null);
    }
    public static String downloadPhoto(String mediaId, String openId,String type,String userId) {
        if (mediaId.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR) >= 0) {
            return mediaId;
        }

        String suffix = "_liveA";
        if ("A".equalsIgnoreCase(type)) {
            suffix = "_liveA";
        } else if ("B".equalsIgnoreCase(type)) {
            suffix = "_liveB";
        } else if ("H".equalsIgnoreCase(type)) {
            suffix = "_headpic";
        }

          
        String accessToken = AccessTokenHolder.getAccessTokenByOpenId(openId);
        Logger.info("微信sdk---accessToken:" + accessToken);
        Logger.info("accessToken:" + accessToken);
        HttpURLConnection http = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                + accessToken + "&media_id=" + mediaId;
        String photoType = null;
        String fullFilePathName = null;

        try {
            URL urlGet = new URL(url);

            http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);

            http.setConnectTimeout(30 * 1000);
            http.setReadTimeout(30 * 1000);

            http.connect();
            // 获取文件转化为byte流
            inputStream = http.getInputStream();
            //
            Map map = http.getHeaderFields();
            String content_type = (String) ((List) map.get("Content-Type"))
                    .get(0);
            photoType = content_type.split("/")[1];
            Logger.info("photo_type:" + content_type);

            String savePath = FileUploadPathUtil.getUploadPath(userId);

            fullFilePathName = savePath + new Date().getTime() + suffix + "."
                    + photoType;
            //
            fileOutputStream = new FileOutputStream(fullFilePathName);
            play.Logger.info("====order images savePath is:%s",
                    fullFilePathName);

            int len = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            fileOutputStream.flush();

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
            http.disconnect();

        }
        String firstPath = fullFilePathName;
        Logger.info("++upload pic fullFilePathName = " + fullFilePathName);
        fullFilePathName = File.separator
                + fullFilePathName.substring(fullFilePathName
                .indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));

        Logger.info("++after upload pic fullFilePathName = " + fullFilePathName);
        //略缩图
        String _suffix = firstPath.substring(firstPath.lastIndexOf(".")+1,firstPath.length());
        String fileName = firstPath.substring(firstPath.lastIndexOf("/"), firstPath.length());
        String small_path = Play.roots.get(0).child("_web_").child("images").getRealFile().getAbsolutePath()+"/small"+fileName;
        Logger.info("略缩图后缀名："+_suffix);
        imageUtils.compress(firstPath, small_path,0.2f,1f, _suffix);
        return fullFilePathName;
    }


}