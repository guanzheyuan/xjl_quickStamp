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
package utils.security.digest;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import utils.security.AsymmericEncryptionHandle;
import utils.security.EncryptionHandle;
import utils.security.SymmericEncryptionHandle;
import utils.security.arithmetic.Base64It;
import utils.security.arithmetic.DESIt;
import utils.security.arithmetic.MD5It;
import utils.security.arithmetic.SHAIt;
import utils.security.exception.EncryptException;

/**
 *  获取加密、解密Digest
 * @author dawn
 * 
 */
public class CipherDigest implements AsymmericEncryptionHandle,
		EncryptionHandle, SymmericEncryptionHandle {

	/** 日志对象 */
	private final static Logger logger = Logger
			.getLogger(CipherDigest.class);

	private static Map digestMap = new HashMap();

	private static byte[] rwLock = new byte[0];

	private static boolean isInitDigestMap = false;

	private EncryptionHandle encryptionHandle = null;

	private AsymmericEncryptionHandle asymmericEncryptionHandle = null;

	private SymmericEncryptionHandle symmericEncryptionHandle = null;

	private String name = null;

	protected CipherDigest(String name) {
		this(name, null, null, null);
	}

	protected CipherDigest(String name, EncryptionHandle encryptionHandle) {

		this(name, encryptionHandle, null, null);

	}

	protected CipherDigest(String name,
			SymmericEncryptionHandle symmericEncryptionHandle) {
		this(name, null, null, symmericEncryptionHandle);
	}

	protected CipherDigest(String name,
			AsymmericEncryptionHandle asymmericEncryptionHandle) {

		this(name, null, asymmericEncryptionHandle, null);
	}

	protected CipherDigest(String name, EncryptionHandle encryptionHandle,
			AsymmericEncryptionHandle asymmericEncryptionHandle,
			SymmericEncryptionHandle symmericEncryptionHandle) {
		setName(name);
		setEncryptionHandle(encryptionHandle);
		setAsymmericEncryptionHandle(asymmericEncryptionHandle);
		setSymmericEncryptionHandle(symmericEncryptionHandle);
	}

	/**
	 * 获取Digest实例
	 * 
	 * @param cryptDigestName
	 * @return
	 */
	public static CipherDigest instance(String cryptDigestName) {
		registerAllDigest();
		CipherDigest digest = null;
		synchronized (rwLock) {
			digest = (CipherDigest) digestMap.get(cryptDigestName);
		}
		return digest;
	}

	/**
	 * 注册所有的Digest
	 * 
	 * @return
	 */
	private static boolean registerAllDigest() {
		if (!isInitDigestMap) {
			// ע��
			synchronized (rwLock) {
				registerDigest(new CipherDigest("base64Digest", new Base64It()));
				registerDigest(new CipherDigest("desDigest", new DESIt()));
				registerDigest(new CipherDigest("md5Digest", new MD5It()));
				registerDigest(new CipherDigest("shaDigest", new SHAIt()));
			}
			isInitDigestMap = true;
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 注册Digest
	 * 
	 * @param digestImpl
	 * @return
	 */
	public static boolean registerDigest(CipherDigest digestImpl) {
		if (digestImpl != null) {
			String digestName = digestImpl.getName();
			digestMap.put(digestName, digestImpl);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �������

	 * 
	 * @param name
	 */
	protected void setName(String name) {
		this.name = name;

	}

	/**
	 * ��ȡ���

	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public EncryptionHandle getHandle() {
		return encryptionHandle;
	}

	public void setEncryptionHandle(EncryptionHandle encryptionHandle) {
		this.encryptionHandle = encryptionHandle;
	}

	public void setAsymmericEncryptionHandle(
			AsymmericEncryptionHandle asymmericEncryptionHandle) {
		this.asymmericEncryptionHandle = asymmericEncryptionHandle;
	}

	public void setSymmericEncryptionHandle(
			SymmericEncryptionHandle symmericEncryptionHandle) {
		this.symmericEncryptionHandle = symmericEncryptionHandle;
	}

	public byte[] encrypt(byte[] bInput) throws EncryptException {
		if (encryptionHandle != null) {
			return encryptionHandle.encrypt(bInput);
		} else {

			logger
					.error(getName()
							+ " not support this operation! because encryptionHandle is null! by encrypt(byte[] bInput) ");

			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not {SHA,MD5,Base64} ! ");
		}

	}

	public byte[] decrypt(byte[] bInput) throws EncryptException {
		if (encryptionHandle != null) {
			return encryptionHandle.decrypt(bInput);
		} else {

			logger
					.error(getName()
							+ " not support this operation! because encryptionHandle is null! by decrypt(byte[] bInput) ");

			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not {SHA,MD5,Base64} ! ");

		}
	}

	public byte[] encrypt(byte[] sInput, Key encryptKey)
			throws EncryptException {
		if (asymmericEncryptionHandle != null) {
			return asymmericEncryptionHandle.encrypt(sInput, encryptKey);
		}
		if (symmericEncryptionHandle != null) {
			return symmericEncryptionHandle.encrypt(sInput, encryptKey);
		} else {

			logger
					.error(getName()
							+ " not support this operation! because asymmericEncryptionHandle is null and symmericEncryptionHandle is null ! by encrypt(byte[] sInput, Key encryptKey) ");

			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not an asymmetric or symmetric algorithm ! ");
		}
	}

	public KeyPair generateAsymmericKey() throws EncryptException {

		if (asymmericEncryptionHandle != null) {
			return asymmericEncryptionHandle.generateAsymmericKey();
		} else {
			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not an asymmetric algorithm ! ");
		}
	}

	public byte[] decrypt(byte[] sInput, Key decryptKey)
			throws EncryptException {
		if (symmericEncryptionHandle != null) {
			return symmericEncryptionHandle.decrypt(sInput, decryptKey);
		}
		if (asymmericEncryptionHandle != null) {
			return asymmericEncryptionHandle.decrypt(sInput, decryptKey);
		} else {
			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not an  algorithm ! ");
		}
	}

	public Key generateSymmetricKey() throws EncryptException {
		if (symmericEncryptionHandle != null) {
			return symmericEncryptionHandle.generateSymmetricKey();
		} else {
			throw new UnsupportedOperationException(getName()
					+ " not support this operation because " + getName()
					+ " is not an symmetric algorithm ! ");
		}
	}
}
