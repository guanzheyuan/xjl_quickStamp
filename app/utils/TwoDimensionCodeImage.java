package utils;

import java.awt.image.BufferedImage;

import jp.sourceforge.qrcode.data.QRCodeImage;

public class TwoDimensionCodeImage implements QRCodeImage {

	BufferedImage bufImg; 
	
	public TwoDimensionCodeImage(BufferedImage bufImg){
		this.bufImg = bufImg;
	}
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return bufImg.getHeight();
	}

	@Override
	public int getPixel(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return bufImg.getRGB(arg0, arg1);
	}

	@Override
	public int getWidth() {
		return  bufImg.getWidth();
	}

}
