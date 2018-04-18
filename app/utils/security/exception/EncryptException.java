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
package utils.security.exception;

import java.lang.reflect.Method;
/**
 * 加密、解密异常
 * @author dawn
 *
 */
public class EncryptException extends Exception {

	// 支持序列化ID
	private static final long serialVersionUID = 5304894513427860577L;

	/**
	 * 支持无参构造器
	 * 
	 */
	public EncryptException() {
		super();
	}

	/**
	 * 支持有参数构造器
	 * 
	 * @param msg
	 */
	public EncryptException(String msg) {
		super(msg);
	}

	/**
	 * 支持嵌套子异常
	 * 
	 * @param cause
	 */
	public EncryptException(Throwable cause) {
		super(cause.toString());
		setCause(cause);

	}

	/**
	 * 支持外部错误消息嵌套子异常
	 * 
	 * @param msg
	 * @param cause
	 */
	public EncryptException(String msg, Throwable cause) {
		super(msg);
		setCause(cause);

	}

	private void setCause(Throwable cause) {
		if (true) {
			EncryptException.setCause(this, cause);
		}
	}

	/**
	 * 设置嵌套异常
	 * 
	 * @param exception
	 * @param cause
	 * @return
	 */
	private static Throwable setCause(Throwable exception, Throwable cause) {

		if (exception != null) {
			if (true) {
				try {
					Method initCauseMethod = exception.getClass().getMethod(
							"initCause", Throwable.class);
					initCauseMethod.invoke(exception, cause);
				} catch (Exception e) {
					// Ignore
				}
			}
		}
		return exception;
	}
}
