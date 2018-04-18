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

import utils.security.exception.EncryptException;



/**
 * 无密钥算法加密、解密

 * 
 * @author dawn
 * 
 */
public interface EncryptionHandle {

	/**
	 * 加密
	 * 
	 * @param bInputArr
	 *            明文
	 * @return 密文
	 */
	byte[] encrypt(byte[] bInputArr)throws EncryptException;

	/**
	 * 解密
	 * 
	 * @param sInput
	 *            密文
	 * @return 明文
	 */
	byte[] decrypt(byte[] bInputArr)throws EncryptException;

}
