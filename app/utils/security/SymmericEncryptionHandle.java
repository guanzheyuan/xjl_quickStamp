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

import utils.security.exception.EncryptException;


/**
 * 有密钥对称算法加密、解密
 * 
 * @author dawn
 * 
 */
public interface SymmericEncryptionHandle {

	/**
	 * 加密
	 * 
	 * @param bInputArr
	 *            明文
	 * @param symmetricKey
	 *            对称Key
	 * @return 密文
	 */
	byte[] encrypt(byte[] bInputArr, Key symmetricKey)throws EncryptException;

	/**
	 * 解密
	 * 
	 * @param bInputArr
	 *            密文
	 * @param symmeticKey
	 *            对称Key
	 * @return 明文
	 */
	byte[] decrypt(byte[] bInputArr, Key symmeticKey)throws EncryptException;

	/**
	 * 获取对称算法Key
	 * 
	 * @return 对称Key
	 */
	Key generateSymmetricKey()throws EncryptException;
}
