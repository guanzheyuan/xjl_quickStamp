package controllers.comm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import play.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author 姓名 E-mail: 邮箱 Tel: 电话
 * @version 创建时间：2017-7-30 下午5:34:50
 * @describe 类说明
 */
public class CutPictureUtil {

	/**
	 * 
	 * @param file
	 * @param directoryFileName
	 * @param width
	 * @param height
	 * @param proportion
	 * @return
	 */
	public static boolean compressImageByWidth(File file,
			String directoryFileName, int width, boolean proportion) {
		return compressImage(file, directoryFileName, width, 0, proportion);
	}

	/**
	 * 
	 * @param file
	 * @param directoryFileName
	 * @param width
	 * @param height
	 * @param proportion
	 * @return
	 */
	public static boolean compressImageByHeigth(File file,
			String directoryFileName, int height, boolean proportion) {
		return compressImage(file, directoryFileName, 0, height, proportion);
	}

	public static boolean compressImage(File file, String directoryFileName,
			int width, int height, boolean proportion) {
		boolean ret = false;
		FileOutputStream fileOutputStream = null;
		try {
			if (file == null || directoryFileName == null) {
				return ret;
			}

			fileOutputStream = new FileOutputStream(new File(directoryFileName));

			Image image = ImageIO.read(file);
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);

			System.out.println("height:[" + height + "],width:[" + width + "]");
			System.out.println("Imageheight:[" + image.getHeight(null)
					+ "],ImageWidth:[" + image.getWidth(null) + "]");
			Logger.debug("height:[" + height + "],width:[" + width + "]");
			Logger.debug("Imageheight:[" + image.getHeight(null)
					+ "],ImageWidth:[" + image.getWidth(null) + "]");

			if (image.getWidth(null) == -1) {
				return ret;
			}

			int newWidth = 0;
			int newHeight = 0;

			if (image.getWidth(null) > width || image.getHeight(null) > height) {
				if (proportion) {
					if (height > 0) {
						Logger.debug("cal rate by hight");

						if (imageHeight < height) {
							float rate = (float) height / imageHeight;
							newWidth = (int) (imageWidth * rate);
							// throw new
							// BaseException("source pic width is:["+image.getHeight(null)+"],but want to scale to ["+height+"]");
						} else {
							float rate = (float) imageHeight / height;
							newWidth = (int) (imageWidth / rate);
						}
						newHeight = height;
						// if(newWidth>1024)
						// {
						// ret=false;
						// return ret;
						// }
						//
						// newWidth=image.getWidth(null)/rate;
					}

					else if (width > 0) {
						Logger.debug("cal rate by width 1 imageWidth:"
								+ imageWidth + ",width:" + width
								+ ",newHeight:" + newHeight);

						if (imageWidth < width) {
							float rate = (float) width / imageWidth;
							newHeight = (int) (imageHeight * rate);//
							// throw new
							// BaseException("source pic width is:["+image.getWidth(null)+"],but want to scale to ["+width+"]");
						} else {
							//
							float rate = (float) imageWidth / width;
							newHeight = (int) (imageHeight / rate);//
						}

						newWidth = width;
						Logger.debug("cal rate by width 2 imageWidth:"
								+ imageWidth + ",width:" + width
								+ ",newHeight:" + newHeight);
						// if(newHeight>1024)
						// {
						// ret=false;
						// return ret;
						// }

					}

				} else {
					newWidth = width;
					newHeight = height;
				}
			} else {
				newWidth = image.getWidth(null);
				newHeight = image.getHeight(null);
			}

			Logger.debug("newWidth:[" + newWidth + "],newHeight:[" + newHeight
					+ "]");
			BufferedImage bufferedImage = new BufferedImage(newWidth,
					newHeight, BufferedImage.TYPE_INT_RGB);
			// 解决上传透明背景的png图片时自动填充黑色的问题
			Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, newWidth, newHeight);
			g2.setPaint(Color.RED);

			bufferedImage.getGraphics().drawImage(
					image.getScaledInstance(newWidth, newHeight,
							image.SCALE_SMOOTH), 0, 0, null);

			JPEGImageEncoder encoder = JPEGCodec
					.createJPEGEncoder(fileOutputStream);
			// JPEGEncodeParam jep =
			// JPEGCodec.getDefaultJPEGEncodeParam(fileOutputStream);
			// 压缩质量
			// jep.setQuality(1F, true);
			// encoder.encode(bufferedImage,jep);
			encoder.encode(bufferedImage);
			fileOutputStream.close();

			Logger.debug("write file to [" + directoryFileName + "] sucess");

			ret = true;

		} catch (Exception e) {
			Logger.error("xxxxxxxxxxxxxxx----Generate small picture failure"
					+ e.getMessage());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(ret){
				file.deleteOnExit();
			}
		}
		return ret;
	}
	public static void main(String[] args){
		try {
			File f=new File("f:/1qaz.jpg");
		    boolean flag=CutPictureUtil.compressImageByHeigth(f, "f:/20150214_140253.jpg", 200, true);
		    System.out.print(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
