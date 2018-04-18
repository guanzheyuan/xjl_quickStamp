/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package utils.security.arithmetic;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.log4j.Logger;

import utils.AssertUtil;
import utils.security.SymmericEncryptionHandle;
import utils.security.exception.EncryptException;




/**
 * 对称密钥算法Des实现类
 * 
 * @author dawn
 * 
 */
public class DESIt implements SymmericEncryptionHandle {

	private String arithmetic = "DES";

	/** 日志对象 */
	private final static Logger log = Logger
			.getLogger(DESIt.class);

	/**
	 * 解密
	 */
	public byte[] decrypt(byte[] bInputArr, Key symmetricKey)
			throws EncryptException {
		AssertUtil.isNotEmpty(bInputArr);
		AssertUtil.isNotNull(symmetricKey);
		byte[] bOutputArr = doFinal(bInputArr, Cipher.DECRYPT_MODE,
				symmetricKey);

		return bOutputArr;
	}

	/**
	 * 加密
	 */
	public byte[] encrypt(byte[] bInputArr, Key symmetricKey)
			throws EncryptException {
		AssertUtil.isNotEmpty(bInputArr);
		AssertUtil.isNotNull(symmetricKey);
		byte[] bOutputArr = doFinal(bInputArr, Cipher.ENCRYPT_MODE,
				symmetricKey);

		return bOutputArr;
	}

	/**
	 * 获取Key
	 */
	public Key generateSymmetricKey() throws EncryptException {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg;
		Key key = null;
		try {
			kg = KeyGenerator.getInstance(arithmetic);
			kg.init(sr);
			key = kg.generateKey();
		} catch (NoSuchAlgorithmException e) {

			log.error(arithmetic + " no such alorithm", e);
			EncryptException encryptException = new EncryptException(arithmetic
					+ " no such alorithm", e);
			throw encryptException;
		}
		return key;
	}

	/**
	 * 进行实际加密解密操作
	 * 
	 * @param cipherMode
	 * @param key
	 * @return
	 */
	private byte[] doFinal(byte[] bInputArr, int cipherMode, Key key)
			throws EncryptException {
		byte[] bOutputArr = null;
		try {
			Cipher cipher = Cipher.getInstance(arithmetic);
			cipher.init(cipherMode, key);
			bOutputArr = cipher.doFinal(bInputArr);
		} catch (Exception e) {
			log
					.error(
							"encrypt or decrypt error in des arithmetic operation doFinal",
							e);
			EncryptException encryptException = new EncryptException(
					"encrypt or decrypt error in des arithmetic operation doFinal",
					e);
			throw encryptException;
		}
		return bOutputArr;
	}
}
