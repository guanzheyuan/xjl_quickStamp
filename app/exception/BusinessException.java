/**
 * Copyright (C) 2003-2011 ZTESoft. All rights reserved.
 */
package exception;


/**
 * 业务异常基类
 * @author wang.xiang23
 * @since 2011-8-22
 */
public class BusinessException extends BaseException {

	/**
	 * 
	 */
	public BusinessException() {
	}

	/**
	 * @param message
	 */
	public BusinessException(String code) {
		this.code = code;
	}

	/**
	 * @param cause
	 */
	public BusinessException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param code
	 * @param message
	 * @param cause
	 */
	public BusinessException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	/**
	 * @param code
	 * @param message
	 */
	public BusinessException(String code, String message) {
		super(code, message);
	}

	/**
	 * @param code
	 * @param cause
	 */
	public BusinessException(String code, Throwable cause) {
		super(code, cause);
	}

}
