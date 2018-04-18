package utils;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import utils.security.digest.CipherDigest;
import utils.security.exception.EncryptException;

/**
 * 加解密
 * 
 * @author Liyb
 *
 */
public class SecurityUtil {

	public static final Logger logger = Logger.getLogger(SecurityUtil.class);

	static Cipher eCipher = null;

	static Cipher dCipher = null;

	static byte[] bkey = "uuuBOSSS".getBytes();

	static byte[] biv = { (byte) 0xCE, (byte) 0x35, (byte) 0x5, (byte) 0xD,
			(byte) 0x98, (byte) 0x91, (byte) 0x8, (byte) 0xA };

	static AlgorithmParameterSpec paramSpec = null;

	static SecretKey keySpec = null;

	static String encoding = "UTF-8";

	static {
		try {
			eCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			dCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			paramSpec = new IvParameterSpec(biv);
			keySpec = new SecretKeySpec(bkey, "DES");

			dCipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
			eCipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 加密
	 * @param inputStr
	 * @return
	 */
	public static String encrypt(String inputStr)  {
		if (inputStr == null || eCipher == null)
			return null;
		try {
			return byteArr2HexStr(eCipher.doFinal(inputStr.getBytes()));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR--------encrypt-----"+e.getMessage());
			return "";
		} catch (BadPaddingException e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR--------encrypt-----"+e.getMessage());
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR--------encrypt-----"+e.getMessage());
			return "";
		}
	}

	/***
	 * 解密
	 * @param inputStr
	 * @return
	 */
	public static String decrypt(String inputStr){
		if (inputStr == null || dCipher == null)
			return null;
		try {
			return new String(dCipher.doFinal(hexStr2ByteArr(inputStr)));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR------decrypt-------"+e.getMessage());
			return "";
		} catch (BadPaddingException e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR------decrypt-------"+e.getMessage());
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			play.Logger.error("---------------ERROR------decrypt-------"+e.getMessage());
			return "";
		}
	}

	public static String MD5(String encryptString) {
		String digestName = "md5Digest";
		CipherDigest it = CipherDigest.instance(digestName);

		// utf_8输出
		byte[] utf_8_output = null;
		try {
			synchronized (it) {
				utf_8_output = it.encrypt(ConvertUtil.str2Byte(encryptString,
						encoding));
			}
		} catch (EncryptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String utf_8_encrypt = ConvertUtil.bytesToHexString(utf_8_output);

		return utf_8_encrypt;
	}

	/**
	 * 对字符串先进行Base64加密后再进行MD5加密
	 * 
	 * @param inputStr
	 * @return
	 * @throws BaseAppException
	 */
	public static String base64AndMD5(String inputStr) {
		// 参数校验
		AssertUtil.isNotEmpty(inputStr);

		String result = null;
		try {
			CipherDigest it = null;
			byte[] default_output = null;
			it = CipherDigest.instance("base64Digest");
			default_output = it
					.encrypt(ConvertUtil.str2Byte(inputStr, "utf-8"));

			String after_base64 = ConvertUtil.byte2Str(default_output, "utf-8");

			it = CipherDigest.instance("md5Digest");
			default_output = it.encrypt(ConvertUtil.str2Byte(after_base64,
					"utf-8"));

			result = ConvertUtil.bytesToHexString(default_output);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}

		return result;
	}

	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}

		return sb.toString();

	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public static void main(String[] args) throws Exception {
//		String a = SecurityUtil.base64AndMD5("11");
//		System.out.println(a);
//		String b = SecurityUtil.MD5("11");
//		System.out.println(b);

		String c = SecurityUtil.encrypt("");
		System.out.println(c.length()+"|"+c);
		String d = SecurityUtil.decrypt(c);
		System.out.println(d);
	}
}
