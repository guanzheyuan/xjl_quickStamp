package controllers.comm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.nutz.lang.Files;

import play.Logger;
import utils.FileUploadPathUtil;

public class KE extends BaseController {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	private static String[] imageTypes = { "gif", "jpg", "jpeg", "png", "bmp" };
	private static String[] fileTypes = { "doc", "docx", "xls", "xlsx", "ppt",
			"htm", "html", "txt", "zip", "rar", "gz", "bz2" };
	private static String[] mediaTypes = { "swf", "flv", "mp3", "wav", "wma",
			"wmv", "mid", "avi", "mpg", "asf", "rm", "rmvb" };
	private static String[] flashTypes = { "swf", "flv" };
	private static Map<String, String[]> m = new HashMap<String, String[]>();
	static {
		m.put("image", imageTypes);
		m.put("file", fileTypes);
		m.put("flash", flashTypes);
		m.put("media", mediaTypes);
	}

	public static void uploadjson(File imgFile) {
		String savePath = FileUploadPathUtil.getUploadPath();
		String dirname = params.get("dir");// file,image,multiimage,media
		if (dirname == null) {
			dirname = "image";
		}
		Logger.info("+++++dirname= " + dirname);
		Logger.info("+++++File savePath++++ = " + savePath);
		// 最大文件大小
		long maxSize = 20000000;
		if (imgFile != null) {
			try {
				// 检查文件大小
				if (imgFile.length() > maxSize) {
					Logger.info("上传文件大小超过限制");
					renderText(getError("上传文件大小超过限制"));
					return;
				}

				Logger.info("fileToUpload path = " + imgFile.getAbsolutePath());
				// 检查扩展名
				String suffix = Files.getSuffixName(imgFile).toLowerCase();

				if (!Arrays.asList(m.get(dirname)).contains(suffix)) {
					Logger.info("上传文件扩展名是不允许的扩展名");
					renderText(getError("上传文件扩展名是不允许的扩展名"));
					return;
				}

				String fileName = df.format(new Date()) + "_"
						+ new Random().nextInt(1000) + "." + suffix;
				File filePath = new File(savePath + fileName);
				Files.copyFile(imgFile, filePath);

				String fileUrlPath = filePath.getAbsolutePath();

				fileUrlPath = File.separator
						+ fileUrlPath.substring(fileUrlPath
								.indexOf(FileUploadPathUtil.UPLOAD_ROOT_DIR));
				Logger.info("fileUrlPath = " + fileUrlPath);
				JSONObject obj = new JSONObject();
				obj.put("error", 0);
				obj.put("url", fileUrlPath);
				renderText(obj.toString());
				return;
			} catch (Exception e) {
				e.printStackTrace();
				renderText(getError("上传失败"));
				return;
			}

		} else {
			Logger.info("请选择文件");
			renderText(getError("请选择文件"));
			return;
		}
	}

	private static String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toString();
	}
}
