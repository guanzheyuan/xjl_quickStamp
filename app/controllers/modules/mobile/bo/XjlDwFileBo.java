package controllers.modules.mobile.bo;

import play.Logger;
import models.modules.mobile.XjlDwFile;
import utils.DateUtil;
import utils.FileUploadPathUtil;
import utils.SeqUtil;
import utils.StringUtil;
/**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-09-14 上午11:38:53
 * @describe  类说明
*/
public class XjlDwFileBo {
	// 增加、编辑
	public static XjlDwFile save(XjlDwFile xjlDwFile) {
		if (xjlDwFile.fileId != null) {
		}
		if (xjlDwFile.fileId == null) {
			xjlDwFile.fileId = SeqUtil.maxValue("xjl_dw_file", "file_id");
			xjlDwFile.status = "0AA";
			xjlDwFile.createTime = DateUtil.getNowDate();
		}
		xjlDwFile = xjlDwFile.save();
		return xjlDwFile;
	}
	
	/**
	 * 增加
	 * @param imageUrl 上传文件保存路径
	 * @param userId 上传人标识
	 * @param imageType 文件类型
	 * @return 0:成功
	 */
	public static XjlDwFile saveImage(String imageUrl,String wxOpenId) {
		XjlDwFile xjlDwFile = new XjlDwFile();
		xjlDwFile.fileName = FileUploadPathUtil.getFileNameForPath(imageUrl);
		xjlDwFile.fileUrl = imageUrl;
		xjlDwFile.fileType="1";
		xjlDwFile.wxOpenId=wxOpenId;
		xjlDwFile.fileId = SeqUtil.maxValue("xjl_dw_file", "file_id");
		xjlDwFile.createTime = DateUtil.getTimestamp();
		xjlDwFile.status = "0AA";
		try {
			xjlDwFile.save();
		} catch (Exception e) {
	        Logger.error("记录上传图片文件失败图片信息：imageUrl="+imageUrl);
		}
		return xjlDwFile;
	}
	
	// 删除
	public static XjlDwFile del(XjlDwFile xjlDwFile) {
		if (xjlDwFile != null) {
			xjlDwFile.status = "0XX";
			xjlDwFile = xjlDwFile.save();
			return xjlDwFile;
		}
		return null;
	}
}