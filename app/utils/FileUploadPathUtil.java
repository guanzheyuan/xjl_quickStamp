package utils;

import java.io.File;
import java.util.Calendar;

import play.Play;

public class FileUploadPathUtil {

	public static final String UPLOAD_ROOT_DIR = "_web_";

	public static String getUploadPath() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		String savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR)
				.child("images").getRealFile().getAbsolutePath()
				+ File.separator
				+ year
				+ File.separator
				+ month
				+ File.separator + day + File.separator;
		File saveDir = new File(savePath);
		if (!saveDir.exists())
			saveDir.mkdirs();
		return savePath;
	}
	/**
	 * 获取文件上传路径
	 * 
	 * @return
	 */
	public static String getUploadPath(String imageType) {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		String savePath =null;
		if(StringUtil.isNotEmpty(imageType)) {
			 savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR)
					.child("images").child(imageType).getRealFile().getAbsolutePath()
					+ File.separator
					+ year
					+ File.separator
					+ month
					+ File.separator + day + File.separator;
		}else{
			 savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR)
					.child("images").getRealFile().getAbsolutePath()
					+ File.separator
					+ year
					+ File.separator
					+ month
					+ File.separator + day + File.separator;
		}
		
		File saveDir = new File(savePath);
		if (!saveDir.exists())
			saveDir.mkdirs();
		return savePath;
	}
	
	public static String getUploadPath(String userId,String imageType) {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		String savePath =null;
		if(StringUtil.isNotEmpty(imageType)) {
			 savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR)
					.child("images").child(imageType).child(userId).getRealFile().getAbsolutePath()
					+ File.separator
					+ year
					+ File.separator
					+ month
					+ File.separator + day + File.separator;
		}else{
			 savePath = Play.roots.get(0).child(UPLOAD_ROOT_DIR)
					.child("images").child(userId).getRealFile().getAbsolutePath()
					+ File.separator
					+ year
					+ File.separator
					+ month
					+ File.separator + day + File.separator;
		}
		File saveDir = new File(savePath);
		if (!saveDir.exists())
			saveDir.mkdirs();
		return savePath;
	}
	/***
	 * 返回项目的绝对路径，真实路径
	 * 示例：G:\Android\zzb
	 * @return
	 */
	public static String getRealPath(){
		return Play.roots.get(0).getRealFile().getAbsolutePath();
	}
	
	/***
	 * 从文件路径中取得文件名，兼容widows系统和linux系统下的\或/
	 * @param filePath
	 * @return 返回文件名
	 */
	public static String getFileNameForPath(String filePath){
		if (filePath.lastIndexOf("/")>0) {
			return filePath.substring(filePath.lastIndexOf("/")+1);
		}else if(filePath.lastIndexOf("\\")>0){
			return filePath.substring(filePath.lastIndexOf("\\")+1);
		}
		return filePath;
	}
}
