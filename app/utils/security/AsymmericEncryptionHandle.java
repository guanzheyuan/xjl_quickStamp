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
package utils.security;

import java.security.Key;
import java.security.KeyPair;

import utils.security.exception.EncryptException;


/**
 * 有密钥非对称算法处理接口
 * 
 * @author dawn
 * 
 */
public interface AsymmericEncryptionHandle {

	/**
	 * 加密
	 * 
	 * @param bInputArr
	 *            明文
	 * @param keyPair
	 *            非对称Key
	 * @return 密文
	 */
	byte[] encrypt(byte[] bInputArr, Key publicKey)throws EncryptException;

	/**
	 * 解密
	 * 
	 * @param sInput
	 *            密文
	 * @param keyPair
	 *            非对称Key
	 * @return 明文
	 */

	byte[] decrypt(byte[] bInputArr, Key privateKey)throws EncryptException;

	/**
	 * 生成非对称key
	 * 
	 * @return 非对称Key
	 */

	KeyPair generateAsymmericKey()throws EncryptException;
}
