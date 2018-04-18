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
package exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.i18n.Messages;
import utils.StringUtil;
import utils.ValidateUtil;

/**
 * <p>
 * Title: Prodigy
 * </p>
 * 
 * <p>
 * Description: 异常籄1�7/p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: ztesoft
 * </p>
 * 
 * @author lu.zhen
 * @version 0.1
 */
public final class BaseAppException extends Exception implements Serializable {

	private static final long serialVersionUID = -27020729327620212L;

	public static final int INNER_ERROR = 1;

	public static final int BUSS_ERROR = 0;

	private int id;

	private String code;

	private String desc;

	private String localeMessage;

	private Date time;

	private int type;

	public BaseAppException() {
		super();
	}

	public BaseAppException(String code) {
		this(code, null, INNER_ERROR, null, null, null, null);
	}

	public BaseAppException(String code, String msg) {
		this(code, msg, INNER_ERROR, null, null, null, null);
	}

	public BaseAppException(String code, String msg, String arg0) {
		this(code, msg, INNER_ERROR, null, arg0, null, null);
	}

	public BaseAppException(String code, Throwable cause) {
		this(code, null, INNER_ERROR, cause, null, null, null);
	}

	public BaseAppException(String code, int errorType, Throwable cause) {
		this(code, null, errorType, cause, null, null, null);
	}

	public BaseAppException(String code, String msg, int errorType) {
		this(code, msg, errorType, null, null, null, null);
	}

	public BaseAppException(String code, String param1, Throwable cause) {
		this(code, null, INNER_ERROR, cause, param1, null, null);
	}

	public BaseAppException(String code, String param1, String param2,
			Throwable cause) {
		this(code, null, INNER_ERROR, cause, param1, param2, null);
	}

	/**
	 * 为了可对异常信息进行参数替换，扩展了String arg0,String arg1,String arg2 三个参数
	 * 
	 * @param errorCode
	 * @param message
	 * @param errorType
	 * @param cause
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public BaseAppException(String errorCode, String message, int errorType,
			Throwable cause, String arg0, String arg1, String arg2) {
		/** @todo* */

		super(message, cause);

		List<String> list = new ArrayList<String>(3);
		if (arg0 != null) {
			list.add(arg0);
		}
		if (arg1 != null) {
			list.add(arg1);
		}
		if (arg2 != null) {
			list.add(arg2);
		}
		String[] args = null;

		if (list.size() > 0) {
			args = new String[list.size()];
			int i = 0;
			for (String s : list) {
				args[i++] = s;
			}
		}

		this.code = errorCode;
		this.desc = message;
		BaseAppException beCause = ExceptionUtil
				.getFirstBaseAppException(cause);
		if (beCause == null) {
			this.type = errorType;
		} else {
			this.type = beCause.getType();
		}

		try {
			if (code == null) {
				this.localeMessage = "";
			} else {
				this.localeMessage = Messages.get(code);

				if (StringUtil.isEmpty(localeMessage)) {
					this.localeMessage = errorCode;
					this.code = "S-COMMON-9999";
				}
			}
			if (args != null && args.length > 0) {
				this.localeMessage = this.replaceArgs(localeMessage, args);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param s
	 * @param args
	 * @return
	 */
	private String replaceArgs(String s, String args[]) {
		int i = 0;
		if (s != null && args != null && args.length > 0) {
			StringBuilder sb = new StringBuilder();
			Pattern p = Pattern.compile("\\{(.*?)\\}");
			Matcher m = p.matcher(s);
			while (m.find()) {
				s = s.replaceFirst("\\{(.*?)\\}", args[i++]);
			}
			sb.append(s);
			return sb.toString();
		}

		return "";
	}

	/**
	 * 锟斤拷锟矫达拷位锟斤拷息锟斤拷识.
	 * 
	 * @param id
	 *            int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 锟斤拷锟矫达拷锟斤拷锟斤拷锟ￄ1�7.
	 * 
	 * @param code
	 *            String
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 锟斤拷锟矫达拷锟斤拷锟斤拷锟斤拷锟斤拷息.
	 * 
	 * @param desc
	 *            String
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 锟斤拷锟矫达拷锟斤拷锟斤拷时锟斤拄1�7.
	 * 
	 * @param time
	 *            Date
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * 锟斤拷施锟斤拷锟斤拷锟斤拷锟斤拷.
	 * 
	 * @param type
	 *            int
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 锟矫碉拷锟斤拷锟斤拷锟斤拷息锟斤拷识.
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * 锟矫碉拷锟斤拷锟斤拷锟斤拷锟ￄ1�7.
	 * 
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷.
	 * 
	 * @return String
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 锟矫碉拷锟斤拷锟斤拷锟斤拷时锟斤拄1�7.
	 * 
	 * @return Date
	 */
	public Date getTime() {
		if (time == null) {
			time = new Date();
		}
		return time;
	}

	/**
	 * 锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷.
	 * 
	 * @return int
	 */
	public int getType() {
		return type;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (code != null) {
			sb.append("errorCode = [");
			sb.append(code);
			sb.append("] errorDesc = [");

			if (localeMessage != null) {
				sb.append(localeMessage);
			}
			sb.append("]");

			if (desc != null) {
				sb.append("  Describing= [");
				sb.append(desc);
				sb.append("]");
			}
		}
		for (int i = 0; i < getStackTrace().length; i++) {
			sb.append("\r\n\tat ");
			sb.append(getStackTrace()[i]);
		}
		Throwable cause = this.getCause();

		while (cause != null) {
			sb.append("\r\nCause by: ");

			sb.append(cause.toString());

			for (int i = 0; i < cause.getStackTrace().length; i++) {
				sb.append("\r\n\tat ");
				sb.append(cause.getStackTrace()[i]);
			}

			cause = cause.getCause();
			if (cause != null) {
				sb.append("\r\nCaused by: ");
			}
		}
		return sb.toString();
	}

	public String toStringNonTrace() {
		StringBuilder sb = new StringBuilder();
		sb.append("errorCode = [");
		if (ValidateUtil.validateNotEmpty(getCode())) {
			sb.append(getCode());
		}
		sb.append("] errorDesc = [");

		if (getLocaleMessage() != null) {
			sb.append(getLocaleMessage());
		}
		sb.append("]");
		if (getDesc() != null) {
			sb.append("  describing= [");
			sb.append(getDesc());
			sb.append("]");
		}
		Throwable cause = getCause();
		if (cause != null) {
			while (true) {
				if (cause.getCause() != null) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
		}
		if (cause != null) {
			sb.append(" cause = [");
			sb.append(cause.getClass().getName());
			sb.append(":");
			sb.append(cause.getMessage());
			sb.append("]");
		}
		return sb.toString();
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (message == null) {
			message = getLocaleMessage();
			if (code != null) {
				message = new StringBuilder().append('[').append(code)
						.append("] ").append(message).toString();
			}
		} else if (code != null) {
			message = new StringBuilder().append('[').append(code).append("] ")
					.append(message).toString();
		}
		return message;
	}

	public String getLocaleMessage() {
		return localeMessage;
	}

	public void setLocaleMessage(String localeMessage) {
		this.localeMessage = localeMessage;
	}

	public String getDetailMessage() {
		StringBuilder content = new StringBuilder();
		if (code != null) {
			content.append('[').append(code).append("] ");
		}
		if (localeMessage != null) {
			content.append('[').append(localeMessage.trim()).append("] ");
		}
		String message = super.getMessage();
		if (message != null) {
			content.append('[').append(message.trim()).append("] ");
		}
		return content.toString();
	}
}
