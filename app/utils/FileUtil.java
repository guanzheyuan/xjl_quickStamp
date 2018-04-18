package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import play.Logger;


public class FileUtil {

	/**
	 * 目录不存在，则创建
	 * @param path
	 * @return
	 */
	public static boolean DirsNotExistMkdirs(String path) {
		File dir = new File(path);
		// 创建文件夹
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return true;
	}
	/***
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	/***
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean fileExist(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	 public static String FormetFileSize(long fileS) {//转换文件大小
	        String fileSizeString = "";
	        if (fileS < 1024) {
	            fileSizeString = Math.round((double) fileS) + "B";
	        } else if (fileS < 1048576) {
	            fileSizeString = Math.round((double) fileS / 1024) + "K";
	        } else if (fileS < 1073741824) {
	            fileSizeString = Math.round((double) fileS / 1048576) + "M";
	        } else {
	            fileSizeString = Math.round((double) fileS / 1073741824) +"G";
	        }
	        return fileSizeString;
	     }
	 
	 /**
	     * 复制文件
	     * @param fromFile
	     * @param toFile
	     * <br/>
	     * 2016年12月19日  下午3:31:50
	     * @throws IOException 
	     */
	    public static boolean copyFile(File fromFile,File toFile){
	    	boolean flag = true;
	        FileInputStream ins = null;
			try {
				ins = new FileInputStream(fromFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.getMessage());
				flag = false;
			}
	        FileOutputStream out = null;
			try {
				out = new FileOutputStream(toFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.getMessage());
				flag = false;
			}
	        byte[] b = new byte[1024];
	        int n=0;
	        try {
				while((n=ins.read(b))!=-1){
				    out.write(b, 0, n);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.getMessage());
				flag = false;
			}
	        try {
				ins.close();
				 out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.getMessage());
				flag = false;
			}
	        return flag;
	    }
}
