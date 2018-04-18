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
package utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import utils.DateUtil;
import utils.ValidateUtil;
import exception.BaseAppException;
import exception.ExceptionHandler;

/**
 * BO对象
 * 
 * @author dawn
 * 
 */
public class DynamicDict implements java.io.Serializable {
	private static final long serialVersionUID = 7996383561512782942L;

	public String serviceName;



	public HashMap<String, Object> valueMap;

	public List<String> nullValueKeyList = null;

	private int VISIT_FLAG = 0;

	private boolean isBOMode = false;

	private boolean isSuccess; // 成功标志

	private String msg;// 错误或提示信息

	private String msgCode;// 异常信息编码

	private String type; // 标识bo来源，主要区分前台是长研还是南研的bo

	private String channel;// 调用服务的渠道标识,标记调用服务的渠道，比如WEB，INTERFACE（接口）等

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DynamicDict() {
		valueMap = new HashMap<String, Object>();
		isBOMode = true;
		nullValueKeyList = new ArrayList<String>();
	}

	public void destroy() {
		if (valueMap != null) {
			valueMap.clear();
			valueMap = null;
		}
		if (nullValueKeyList != null) {
			nullValueKeyList.clear();
			nullValueKeyList = null;
		}

	}

	public void clear() {
		if (valueMap != null) {
			valueMap.clear();
		}
		if (nullValueKeyList != null) {
			nullValueKeyList.clear();
		}

	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) throws BaseAppException {
		this.serviceName = serviceName;
	}

	public String toString() {
		return valueMap.toString();
	}

	private static final String DEFAULT_ENCODING = "UTF-8";

	// key为空或null
	public static final int KEY_IS_EMPTY_STATE = -1;

	// key对应的值存在不为null
	public static final int VALUE_EXIST_IS_NOT_NULL_STATE = 1;

	// key对应的值存在为null
	public static final int VALUE_EXIST_IS_NULL_STATE = 2;

	// key对应的值不存在
	public static final int VALUE_NOT_EXIST_STATE = 3;

	public int getStateOfValue(String key) {
		if (!ValidateUtil.validateNotEmpty(key)) {
			return KEY_IS_EMPTY_STATE;
		} else {
			boolean isExist = this.valueMap.containsKey(key);
			if (isExist) {
				return valueMap.get(key) == null ? VALUE_EXIST_IS_NULL_STATE
						: VALUE_EXIST_IS_NOT_NULL_STATE;
			} else {
				for (int i = 0; i < nullValueKeyList.size(); i++) {
					String nullValueKey = (String) nullValueKeyList.get(i);
					if (nullValueKey.equals(key)) {
						return VALUE_EXIST_IS_NULL_STATE;
					}
				}
				return VALUE_NOT_EXIST_STATE;
			}
		}
	}

	public boolean existValueIsNull(String key) {
		return getStateOfValue(key) == VALUE_EXIST_IS_NULL_STATE;
	}

	/**
	 * 设置指定参数名称对应的值，以Object来定义，对于同一名称参数，允许存在多条数据。
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name
	 * @param object
	 *            参数值为Object对象
	 * @param insert(采用正常模式(0)
	 *            追加模式(1) 、替换模式(2)，如果采用正常模式，已经存在同名参数时，系统会抛出意外)
	 * @param insert
	 * @throws BaseAppException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setValueByName(String name, Object object, int insert)
			throws BaseAppException {
		Object obj = valueMap.get(name);
		ArrayList alObj;
		if (object == null) {
			if (name != null) {
				nullValueKeyList.add(name);
			}
			return;
		}
		if (object != null) {
			if (object instanceof java.sql.ResultSet && this.VISIT_FLAG == 1) {
				// �����ʷ�ʽ当访问方式为JSP访问方式时，把ResultSet转换成ArrayList
				try {
					ArrayList<Map> arr = new ArrayList<Map>();
					ResultSet rs = (ResultSet) object;
					ResultSetMetaData rsmd = rs.getMetaData();
					int cols = rsmd.getColumnCount();
					String[] arr1 = new String[cols];
					for (int i = 1; i <= cols; i++) {
						arr1[i - 1] = rsmd.getColumnName(i);
					}
					Map<String, String> fields;
					while (rs.next()) {
						fields = new HashMap<String, String>();
						for (int i = 0; i < cols; i++) {
							if (rs.getString(arr1[i]) != null)
								fields.put(arr1[i], rs.getString(arr1[i]));
						}
						arr.add(fields);
					}
					if (arr.size() > 0)
						object = arr;
					else
						object = null;
				} catch (SQLException sqle) {
					ExceptionHandler.publish("S-SYS-00006");
				}
			}
		}
		// name = name.toUpperCase().trim();

		if (object != null) {
			if (object instanceof java.lang.String) {
				object = ((String) object).trim();
			} else if (object instanceof java.util.Date) {
				object = new java.sql.Date(((java.util.Date) object).getTime());
			} else if (object instanceof DynamicDict) {
				isBOMode = true;
				if (obj == null) {
					obj = new ArrayList();
				}
			}
		}

		// 已经存在�����

		if (obj != null) {
			switch (insert) {
			case 0: // 正常模式,抛出意外�����

				ExceptionHandler.publish("S-SYS-00007", null, name);
			case 1: // �ж��Ƿ�追加模式׷��ģʽ
				// 判断是否为ArrayList,是则直接添加，否则创建新的ArrayList,再将值添加进去

				if (obj.getClass().getName().equals("java.util.ArrayList")) {
					alObj = (ArrayList) obj;
					alObj.add(object);
					valueMap.remove(name);
					valueMap.put(name, alObj);
				} else {
					alObj = new ArrayList();
					alObj.add(obj); // 添加原来的值

					alObj.add(object); // 添加当前的值

					valueMap.remove(name);
					valueMap.put(name, alObj);
				}
				break;
			case 2: // 替换模式
				valueMap.remove(name);
				valueMap.put(name, object); // 替换成传入的参数对象，不区分原来的对象类型�Ķ�������

				break;
			default:
				ExceptionHandler.publish("S-SYS-00008");
			}
		} else {
			valueMap.put(name, object);
		}
	}

	/**
	 * 设置指定参数名称对应的值，以Object来定义，可以是多种数据。
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name
	 * @param object
	 * @throws FrameException
	 * @throws BaseAppException
	 */
	public void setValueByName(String name, Object object)
			throws BaseAppException {
		setValueByName(name, object, 2); // 采用正常模式调用
	}

	public void setValueByName(String name, String str, boolean isTrim)
			throws BaseAppException {
		if (!ValidateUtil.validateNotEmpty(str)) {
			return;
		}
		if (isTrim) {
			str = str.trim();
		}
		setValueByName(name, str, 2); // 采用替换模式调用
	}

	/**
	 * 设置指定参数名称对应的值，以Object来定义，可以是多种数据。�ж��Ƿ�
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 追加模式
	 * 
	 * @param name
	 * @param object
	 * @throws FrameException
	 * @throws BaseAppException
	 */
	public void addValueByName(String name, Object object)
			throws BaseAppException {
		setValueByName(name, object, 1); // 采用正常模式调用
	}

	/**
	 * 根据名称获取参数(多个）的值，返回值为指定下标的对象，在取值时已经知道返回的是什? 样的数据类型,如果没有找到根据aThrow来决定是否抛出意外
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(
	 *            参数名称)
	 * @param seq(变量下标)
	 * @param isThrow(如果没有找到是否抛意外)
	 * @param name
	 * @param sSeq
	 * @param isThrow
	 * @return Object
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name, int seq, boolean isThrow,
			String strDefault) throws BaseAppException {
		Object obj;
		ArrayList alObj;
		// name = name.toUpperCase().trim();
		obj = valueMap.get(name);
		if (obj == null) {
			if (isThrow)
				ExceptionHandler.publish("S-SYS-00009", null, name);
			else
				obj = strDefault;
		}
		// 判断Object类型，如果是ArrayList，那么根据Seq来获取,
		// Visit_flag=1用于web访问的调用，web会把数据打成包的对象便于app层来使用
		else if (seq >= 0 && this.VISIT_FLAG != 1) // 在下标必须不小于0的情况下才有效，否则直接返回整个Object
		{
			if (obj.getClass().getName().equals("java.util.ArrayList")) {
				alObj = (ArrayList) obj;
				if ((alObj.size() > 0) && (seq < alObj.size())) {
					obj = alObj.get(seq);
				} else
					obj = null;
				if (obj == null && isThrow)
					ExceptionHandler.publish("S-SYS-00009", null, name);
			}
		}
		return obj;
	}

	/**
	 * 根据名称获取参数的值，返回值为对象，在取值时已经知道返回的是什么样的数据类型
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 如果对象不存在则返回默认字符串
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name
	 * @param isThrow
	 * @param defalutValue
	 * @return
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name, String strDefault)
			throws BaseAppException {
		return getValue(name, 0, false, strDefault);
	}

	/**
	 * 根据名称获取参数的值，返回值为对象，在取值时已经知道返回的是什么样的数据类型
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(参数名称)
	 * @param isThrow(是否允许抛出意外)
	 * @param name
	 * @param isThrow
	 * @return Object
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name, boolean isThrow)
			throws BaseAppException {
		if (name.indexOf(".") > 0) {
			String[] strs = name.split("(\\.)");
			DynamicDict obj = (DynamicDict) this.getBOByName(strs[0], isThrow);
			for (int i = 1; i < strs.length - 1; i++) {
				obj = (DynamicDict) obj.getValueByName(strs[i]);
			}
			return obj.getValueByName(strs[strs.length - 1], 0, isThrow, null);
		} else {
			return getValueByName(name, 0, isThrow, null);
		}
	}

	/**
	 * 根据名称获取参数的值，返回值为对象，在取值时已经知道返回的是什么样的数据类型
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name
	 * @return java.lang.Object
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name) throws BaseAppException {
		if (name.indexOf(".") > 0) {
			String[] strs = name.split("(\\.)");
			DynamicDict obj = (DynamicDict) this.getValueByName(strs[0]);
			for (int i = 1; i < strs.length - 1; i++) {
				obj = (DynamicDict) obj.getValueByName(strs[i]);
			}
			return obj.getValueByName(strs[strs.length - 1], 0, true, null);
		} else {
			return getValueByName(name, 0, true, null);
		}
	}

	/**
	 * 根据名称获取参数的值，返回值为对象，在取值时已经知道返回的是什么样的数据类型
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(参数名称)
	 * @param seq(序号)
	 * @param name
	 * @param seq
	 * @return Object
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name, int seq) throws BaseAppException {
		return getValueByName(name, seq, true, null);
	}

	/**
	 * 根据名称获取参数的值，返回值为对象，在取值时已经知道返回的是什么样的数据类型
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(参数名称)
	 * @param seq(序号)
	 * @param name
	 * @param seq
	 * @return Object
	 * @throws BaseAppException
	 */
	public Object getValueByName(String name, int seq, String defaultValue)
			throws BaseAppException {
		return getValueByName(name, seq, false, defaultValue);
	}

	public DynamicDict getBOByName(String aName) throws BaseAppException {
		return getBO(aName, 0, true);
	}

	public DynamicDict getBOByName(String aName, int aSeq)
			throws BaseAppException {
		return getBO(aName, aSeq, true);
	}

	public DynamicDict getBOByName(String aName, boolean isThrow)
			throws BaseAppException {
		return getBO(aName, 0, isThrow);
	}

	private DynamicDict getBO(String name, int seq, boolean isThrow)
			throws BaseAppException {

		if (name.indexOf(".") > 0) {
			String[] strs = name.split("(\\.)");
			DynamicDict obj = (DynamicDict) this.getBOByName(strs[0], isThrow);
			if (obj == null) {
				if (isThrow)
					ExceptionHandler.publish("S-SYS-00009", null, name);
				else
					return null;
			}
			for (int i = 1; i < strs.length - 1; i++) {
				obj = (DynamicDict) obj.getValueByName(strs[i]);
			}
			return obj.getBOByName(strs[strs.length - 1], seq, isThrow);
		} else {
			return getBOByName(name, seq, isThrow);
		}
	}

	public DynamicDict getBOByName(String name, int seq, boolean isThrow)
			throws BaseAppException {
		DynamicDict dict = null;
		Object obj = null;

		// name = name.toUpperCase().trim();
		obj = valueMap.get(name);
		if (obj == null) {
			if (isThrow)
				ExceptionHandler.publish("S-SYS-00009", null, name);
			else
				return null;
		} else {
			if (obj.getClass().getName().indexOf("ArrayList") != -1) {
				ArrayList al = (ArrayList) obj;
				if (seq >= 0 && seq < al.size()) {
					dict = (DynamicDict) al.get(seq);
				} else {
					throw new BaseAppException("S-SYS-00010");
				}
			} else if (obj.getClass().getName().indexOf("DynamicDict") >= 0) {
				// 如果是BO直接返回 by add ye.jianmin
				return (DynamicDict) obj;
			} else {
				ExceptionHandler.publish("S-SYS-00011", null, name);
			}

		}
		return dict;
	}

	/**
	 * 获取指定名称参数的个数
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param aName(参数名称)
	 * @param aName
	 * @return int(参数个数)
	 */
	public int getCountByName(String name) {
		if (name.indexOf(".") > 0) {
			String[] strs = name.split("(\\.)");
			DynamicDict obj = new DynamicDict();
			try {
				obj = (DynamicDict) this.getBOByName(strs[0], false);

				for (int i = 1; i < strs.length - 1; i++) {
					if (obj == null) {
						return 0;
					}
					obj = (DynamicDict) obj.getValueByName(strs[i]);
				}
			} catch (BaseAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (obj == null) {
				return 0;
			}
			return obj.getCountByName(strs[strs.length - 1], true);
		} else {
			return getCountByName(name, true);
		}

	}

	/**
	 * 获取指定名称参数的个数
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(参数名称)
	 * @param isSkipArray(是否忽略数组，因为多条同名参数是存储在一个数组中，因此对于采用ArrayLi
	 *            st数组类型的参数需要特殊处理)
	 * @param name
	 * @param isSkipArray
	 * @return int(参数的个数)
	 */
	public int getCountByName(String name, boolean isSkipArray) {
		Object obj;
		int iObj;
		ArrayList alObj;
		// obj = valueMap.get(name.toUpperCase().trim());
		obj = valueMap.get(name);
		if (obj == null)
			iObj = 0;
		else {
			if (isSkipArray
					&& (obj.getClass().getName().equals("java.util.ArrayList"))) {
				alObj = (ArrayList) obj;
				iObj = alObj.size();
			} else
				iObj = 1;
		}
		return iObj;
	}

	/**
	 * 20060609 添加 可支持嵌套定义的结果 resultNested("dddf${abc${1}}ddd")
	 * 以反向搜索分界符号来完成整个算法的实现。
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param format
	 *            String
	 * @return String
	 * @throws BaseAppException
	 */
	public String format(String format) throws BaseAppException {
		String result = "";
		// / 格式串为空，设置为源串

		if (format == null || format.length() == 0) {
			result = "";
			return result;
		}

		// 格式串不含格式符号，直接设置为格式串
		if (format.indexOf("${") < 0 || format.indexOf("}") < 0) {
			result = format;
			return result;
		}

		final int INIT = 0;
		final int LEFT = 1; // ${ 标志
		final int RIGHT = 2; // } 标志
		int index = 0;
		int rightIndex = 0;
		int flag = INIT;
		String tmp = format;
		result = "";
		String variable = null;
		String tran = null;

		for (;;) {
			// 标志不为RIGHT 则搜索 "}"
			if (flag != RIGHT && (index = tmp.indexOf("}")) >= 0) {
				flag = RIGHT;
			}
			// 标志不为LEFT 则搜索 "${"
			else if (flag != LEFT
					&& (index = tmp.substring(0, index).lastIndexOf("${")) >= 0) {
				flag = LEFT;
			}
			// 否则则退出搜索，可能有不匹配情况。

			else {
				break;
			}

			switch (flag) {
			case RIGHT: // "}"
				rightIndex = index;
				break;
			case LEFT: // 当前为"${" 则开始把"${var}" 之间的变量进行替换

				variable = tmp.substring(index + 2, rightIndex);
				tran = (String) this.getValueByName(variable);

				// 变量替换
				if (tran == null) {
					tran = "";
				}
				tmp = tmp.substring(0, index) + tran
						+ tmp.substring(rightIndex + 1);
				break;
			}
		}

		result = tmp;
		return result;
	}


	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isBOMode() {
		return isBOMode;
	}

	public void setBOMode(boolean isBOMode) {
		this.isBOMode = isBOMode;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	/** 删除值对象中的键值 */
	public void remove(String key) {
		// this.valueMap.remove(key.toUpperCase().trim());
		this.valueMap.remove(key);
	}

	/** **********简化名称API ********** */

	/**
	 * 获取指定名称参数的个数
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name(参数名称)
	 * @param name
	 * @return int(参数个数)
	 */
	public int getCount(String name) {
		return getCountByName(name);
	}

	public void set(String name, Object obj) throws BaseAppException {
		this.setValueByName(name, obj);
	}

	public void set(String name, DynamicDict obj) throws BaseAppException {
		remove(name);
		this.setValueByName(name, obj, 2); // 采用正常模式调用
	}

	public void set(String name, int obj) throws BaseAppException {
		this.add(name, obj);
	}

	public void set(String name, long obj) throws BaseAppException {
		Long value = new Long(obj);
		this.setValueByName(name, value);
	}

	public void set(String name, short obj) throws BaseAppException {
		Long value = new Long(obj);
		this.setValueByName(name, value);
	}

	@SuppressWarnings("unchecked")
	public void set(String name, ArrayList array) throws BaseAppException {
		valueMap.remove(name);
		valueMap.put(name, array);
	}

	public void add(String name, Object obj) throws BaseAppException {
		this.addValueByName(name, obj);
	}

	public void add(String name, int obj) throws BaseAppException {
		Long value = new Long(obj);
		this.setValueByName(name, value);
	}

	public void add(String name, long obj) throws BaseAppException {
		Long value = new Long(obj);
		this.setValueByName(name, value);
	}

	public void add(String name, short obj) throws BaseAppException {
		Long value = new Long(obj);
		this.setValueByName(name, value);
	}

	/**
	 * 给内部使用
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param name
	 * @return java.lang.Object
	 * @throws BaseAppException
	 */
	private Object getValue(String name, int seq, boolean isThrow,
			String defaultValue) throws BaseAppException {
		if (name.indexOf(".") > 0) {
			String[] strs = name.split("(\\.)");
			DynamicDict obj = (DynamicDict) this.getValueByName(strs[0],
					isThrow);
			if (obj == null) {
				return defaultValue;
			}
			for (int i = 1; i < strs.length - 1; i++) {
				obj = (DynamicDict) obj.getValueByName(strs[i], isThrow);
				if (obj == null) {
					return defaultValue;
				}
			}
			return obj.getValueByName(strs[strs.length - 1], seq, isThrow,
					defaultValue);
		} else {
			return getValueByName(name, seq, isThrow, defaultValue);
		}
	}

	public Long getLong(String name) throws BaseAppException {

		Object obj = this.getValue(name, 0, false, null);
		if (obj == null) {
			return null;
		} else if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof Double) {
			return Math.round((Double) obj);
		} else {
			if (obj.toString().equals("")) {
				return null;
			}
			return new Long(obj.toString());
		}

	}

	public Long getLong(String name, boolean isThrow) throws BaseAppException {
		Object obj = this.getValue(name, 0, isThrow, null);
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof Long) {
				return (Long) obj;
			} else if (obj instanceof Double) {
				return Math.round((Double) obj);
			} else {
				if (obj.toString().equals("")) {
					return null;
				}
				return new Long(obj.toString());
			}
		}
	}

	public Long getLong(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, true, null);
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof Long) {
				return (Long) obj;
			} else if (obj instanceof Double) {
				return Math.round((Double) obj);
			} else {
				if (obj.toString().equals("")) {
					return null;
				}
				return new Long(obj.toString());
			}
		}
	}

	public String getString(String name) throws BaseAppException {
		return getString(name, false);
	}

	public String getString(String name, boolean isThrow)
			throws BaseAppException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object obj = this.getValue(name, 0, isThrow, null);

		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof java.sql.Date
				|| obj instanceof java.sql.Timestamp) {
			return sdf.format(obj);
		} else {
			return obj.toString();
		}

	}

	public String getString(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, false, null);
		if (obj instanceof String) {
			return (String) obj;
		} else {
			if (obj == null) {
				return null;
			}
			return obj.toString();
		}
	}
	
	public Float getFloat(String name) throws BaseAppException {
		return getFloat(name, false);
	}

	public Float getFloat(String name, boolean isThrow)
			throws BaseAppException {
		
		Object obj = this.getValue(name, 0, isThrow, null);

		if (obj == null) {
			return null;
		} else if (obj instanceof Float) {
			return (Float) obj;
		} else if (obj instanceof String && !"".equals((String)obj)){
			return Float.valueOf((String)obj);
		}

		return null;
	}

	public Float getFloat(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, false, null);
		
		if (obj == null) {
			return null;
		} else if (obj instanceof Float) {
			return (Float) obj;
		} else if (obj instanceof String && !"".equals((String)obj)){
			return Float.valueOf((String)obj);
		}

		return null;
	}
	
	public Double getDouble(String name) throws BaseAppException {
		return getDouble(name, false);
	}

	public Double getDouble(String name, boolean isThrow)
			throws BaseAppException {
		
		Object obj = this.getValue(name, 0, isThrow, null);

		if (obj == null) {
			return null;
		} else if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof String && !"".equals((String)obj)){
			return Double.valueOf((String)obj);
		}

		return null;
	}

	public Double getDouble(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, false, null);
		
		if (obj == null) {
			return null;
		} else if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof String && !"".equals((String)obj)){
			return Double.valueOf((String)obj);
		}

		return null;
	}

	public DynamicDict getBO(String name) throws BaseAppException {
		return getBO(name, 0, false);
	}

	public DynamicDict getBO(String name, int seq) throws BaseAppException {
		return getBO(name, seq, true);
	}

	public DynamicDict getBO(String aName, boolean isThrow)
			throws BaseAppException {
		return getBO(aName, 0, isThrow);
	}

	@SuppressWarnings("unchecked")
	public List getList(String name) throws BaseAppException {
		Object obj = valueMap.get(name);
		List retList = null;
		if (obj != null) {
			if (obj instanceof List) {
				retList = (List) obj;
			} else {
				retList = new ArrayList();
				retList.add(obj);
			}
		} else {
			retList = new ArrayList();
		}
		return retList;
	}

	public java.sql.Date getDate(String name) throws BaseAppException {
		Object obj = this.getValue(name, 0, false, null);
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof String) {
				if (!obj.toString().equals("")) {
					return DateUtil.string2SQLDate(obj.toString());
				} else {
					return null;
				}
			} else if (obj instanceof Timestamp) {
				return new java.sql.Date(((Timestamp) obj).getTime());
			} else if (obj instanceof java.util.Date) {
				return new java.sql.Date(((java.util.Date) obj).getTime());
			} else {
				return (java.sql.Date) obj;
			}
		}
	}

	public java.sql.Date getDate(String name, boolean isThrow)
			throws BaseAppException {
		Object obj = this.getValue(name, 0, isThrow, null);
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof String) {
				if (!obj.toString().equals("")) {
					return DateUtil.string2SQLDate(obj.toString());
				} else {
					return null;
				}
			} else if (obj instanceof Timestamp) {
				return new java.sql.Date(((Timestamp) obj).getTime());
			} else {
				return (java.sql.Date) obj;
			}
		}
	}

	public java.sql.Date getDate(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, false, null);
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof String) {
				if (!obj.toString().equals("")) {
					return DateUtil.string2SQLDate(obj.toString());
				} else {
					return null;
				}
			} else if (obj instanceof Timestamp) {
				return new java.sql.Date(((Timestamp) obj).getTime());
			} else {
				return (java.sql.Date) obj;
			}
		}
	}

	public Object get(String name, int seq) throws BaseAppException {
		return this.getValue(name, seq, false, null);
	}

	public Object get(String name) throws BaseAppException {
		return this.getValue(name, 0, false, null);
	}

	private Object getOriginal(String name) throws BaseAppException {
		return this.getValue(name, -1, false, null);
	}

	public Object get(String name, String defaultValue) throws BaseAppException {
		return this.getValue(name, 0, false, defaultValue);
	}

	public Object get(String name, boolean isThrow) throws BaseAppException {
		return this.getValue(name, 0, isThrow, null);
	}

	public Boolean getBoolean(String name) throws BaseAppException {
		Object obj = this.getValue(name, 0, false, null);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			if (obj == null) {
				return null;
			}
			return new Boolean(obj.toString());
		}
	}

	public Boolean getBoolean(String name, boolean isThrow)
			throws BaseAppException {
		Object obj = this.getValue(name, 0, isThrow, null);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			if (obj == null) {
				return null;
			}
			return new Boolean(obj.toString());
		}
	}

	public Boolean getBoolean(String name, int seq) throws BaseAppException {
		Object obj = this.getValue(name, seq, false, null);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			if (obj == null) {
				return null;
			}
			return new Boolean(obj.toString());
		}
	}

	public static void main(String[] args) throws BaseAppException {

		DynamicDict custBo = new DynamicDict();
		custBo.add("tempBo", new Integer(1));
		custBo.add("tempBo", new Integer(2));
		custBo.setValueByName("tempBo", null);
		System.out.println(custBo.getList("tempBo").toString());
	}

	public void putAll(DynamicDict dict) throws BaseAppException {
		for (Iterator iter = dict.valueMap.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			set(key, dict.getOriginal(key));
		}
	}
}
