package utils.jpa;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import exception.BaseAppException;
import exception.ExceptionHandler;

public final class ParamObject {

	public final static int TYPE_UNKNOWN = 0;

	public final static int TYPE_INTEGER = 1;

	public final static int TYPE_LONG = 2;

	public final static int TYPE_BIG_DECIMAL = 3;

	public final static int TYPE_STRING = 4;

	public final static int TYPE_DATE = 5;

	public final static int TYPE_TIMES_STAMP = 6;

	public final static int TYPE_BLOB = 7;

	public final static int TYPE_BYTE = 8;

	public final static int TYPE_FLOAT = 9;

	public final static int TYPE_DOUBLE = 10;
	
	public final static int TYPE_CLOB = 11;

	public final static int TYPE_INTEGER_ARRAY = 100 + 1;

	public final static int TYPE_LONG_ARRAY = 100 + 2;

	public final static int TYPE_BIG_DECIMAL_ARRAY = 100 + 3;

	public final static int TYPE_STRING_ARRAY = 100 + 4;

	public final static int TYPE_DATE_ARRAY = 100 + 5;

	public final static int TYPE_TIMES_STAMP_ARRAY = 100 + 6;

	public final static int TYPE_BLOB_ARRAY = 100 + 7;

	public final static int TYPE_BYTE_ARRAY = 100 + 8;

	public final static int TYPE_FLOAT_ARRAY = 100 + 9;

	public final static int TYPE_DOUBLE_ARRAY = 100 + 10;

	private int type;

	String name;

	private Object value;

	public static ParamObject[] newParamObjectList(int paraCount, int arrayLen) {
		ParamObject[] paraList = new ParamObject[paraCount];
		for (int i = 0; i < paraCount; i++) {
			paraList[i] = new ParamObject(arrayLen);
		}
		return paraList;
	}

	public int hashCode() {
		int hashCode = 0;
		if (name != null) {
			hashCode = name.hashCode();
		}
		if (value != null) {
			hashCode = 31 * hashCode + value.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object obj) {
		ParamObject that = (ParamObject) obj;
		if (this == that) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!compareObject(this.name, that.name)) {
			return false;
		}
		if (!compareObject(this.value, that.value)) {
			return false;
		}
		return true;
	}

	private static boolean compareObject(Object a, Object b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		return a.equals(b);
	}

	public boolean IsNull() {
		return value == null;
	}

	public boolean IsArray() {
		return type >= 100;
	}

	public String toString() {
		if (value == null) {
			return "null";
		}

		switch (type) {
		case TYPE_INTEGER:
		case TYPE_LONG:
		case TYPE_BIG_DECIMAL:
			return value.toString();
		case TYPE_STRING:
			return new StringBuilder("'").append(value).append("'").toString();
		case TYPE_DATE: {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return new StringBuilder("to_date('")
					.append(sdf.format((Date) value))
					.append("','yyyy-mm-dd hh24:mi:ss')").toString();
		}
		case TYPE_TIMES_STAMP: {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return new StringBuilder("to_date('").append(sdf.format(value))
					.append("','yyyy-mm-dd hh24:mi:ss')").toString();
		}
		case TYPE_LONG_ARRAY:
		case TYPE_BIG_DECIMAL_ARRAY: {
			if (((Object[]) value).length > 0 && ((Object[]) value)[0] != null) {
				return ((Object[]) value)[0].toString();
			} else {
				return null;
			}
		}
		case TYPE_STRING_ARRAY: {
			if (((Object[]) value).length > 0) {
				return new StringBuilder("'").append(((Object[]) value)[0])
						.append("'").toString();
			} else {
				return null;
			}
		}
		case TYPE_DATE_ARRAY: {
			if (((Object[]) value).length > 0 && ((Object[]) value)[0] != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				return new StringBuilder("to_date('")
						.append(sdf.format((Date) ((Object[]) value)[0]))
						.append("','yyyy-mm-dd hh24:mi:ss')").toString();
			} else {
				return null;
			}
		}
		case TYPE_TIMES_STAMP_ARRAY: {
			if (((Object[]) value).length > 0 && ((Object[]) value)[0] != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				return new StringBuilder("to_date('")
						.append(sdf.format(((Object[]) value)[0]))
						.append("','yyyy-mm-dd hh24:mi:ss')").toString();
			} else {
				return null;
			}
		}

		default:
			return new StringBuilder("'").append(value).append("'").toString();
		}
	}

	public Integer[] getIntegerArray() throws BaseAppException {

		if (type != TYPE_INTEGER) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Integer[]) value;
	}

	public Float[] getFloatArray() throws BaseAppException {

		if (type != TYPE_FLOAT_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Float[]) value;
	}

	public Double[] getDoubleArray() throws BaseAppException {

		if (type != TYPE_DOUBLE_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Double[]) value;
	}

	public Integer getInteger() {
		if (value == null) {
			return null;
		}

		if (type == TYPE_INTEGER) {
			return (Integer) value;
		} else if (value instanceof Number) {
			return new Integer(((Number) value).intValue());
		} else {
			return Integer.valueOf(value.toString());
		}
	}

	public Float getFloat() {
		if (value == null) {
			return null;
		}

		if (type == TYPE_FLOAT) {
			return (Float) value;
		} else if (value instanceof String) {
			return Float.valueOf(value.toString());
		} else {
			return (Float) value;
		}
	}

	public Double getDouble() {
		if (value == null) {
			return null;
		}

		if (type == TYPE_DOUBLE) {
			return (Double) value;
		} else if (value instanceof String) {
			return Double.valueOf(value.toString());
		} else {
			return (Double) value;
		}
	}

	public Long[] getLongArray() throws BaseAppException {

		if (type != TYPE_LONG_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Long[]) value;
	}

	public Long getLong() {
		if (value == null) {
			return null;
		}

		if (type == TYPE_LONG) {
			return (Long) value;
		} else if (value instanceof Number) {
			return new Long(((Number) value).longValue());
		} else {
			return Long.valueOf(value.toString());
		}
	}

	public BigDecimal[] getBigDecimalArray() throws BaseAppException {

		if (type != TYPE_BIG_DECIMAL_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (BigDecimal[]) value;
	}

	public BigDecimal getBigDecimal() {
		if (type == TYPE_BIG_DECIMAL) {
			return (BigDecimal) value;
		} else {
			if (value == null) {
				return null;
			}

			return new BigDecimal(value.toString());
		}
	}

	public String[] getStringArray() throws BaseAppException {

		if (type != TYPE_STRING_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (String[]) value;
	}

	public String getString() {
		if (value == null) {
			return null;
		}

		return value.toString();
	}

	public Date[] getDateArray() throws BaseAppException {

		if (type != TYPE_DATE_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Date[]) value;
	}

	public Date getDate() {
		if (type == TYPE_DATE || value instanceof Date) {
			return (Date) value;
		} else {
			if (value == null) {
				return null;
			}

			return string2SQLDate(value.toString(), "yyyy-MM-dd");
		}
	}

	public Timestamp[] getTimestampArray() throws BaseAppException {

		if (type != TYPE_TIMES_STAMP_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Timestamp[]) value;
	}

	public Timestamp getTimestamp() {
		if (type == TYPE_TIMES_STAMP) {
			return (Timestamp) value;
		} else {
			if (value == null) {
				return null;
			}

			return Timestamp.valueOf(value.toString());
		}
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public Blob[] getBlobArray() throws BaseAppException {

		if (type != TYPE_BLOB_ARRAY) {
			ExceptionHandler.publish("S-DAT-00049");
		}
		return (Blob[]) value;
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public Blob getBlob() {
		if (type == TYPE_BLOB) {
			return (Blob) value;
		} else {
			return null;
		}
	}
	
	public Clob getClob() {
		if (type == TYPE_CLOB) {
			return (Clob) value;
		} else {
			return null;
		}
	}

	public byte[] getByteArray() {

		if (value == null) {
			return null;
		}

		return (byte[]) value;
	}

	public Object[] getValueArray() throws BaseAppException {
		return (Object[]) value;
	}

	public Object getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	private void init(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public ParamObject(int arrayLen) {
		value = new Object[arrayLen];
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public ParamObject(String name, Blob value) {
		this.type = TYPE_BLOB;
		init(name, value);
	}
	
	public ParamObject(String name, Clob value) {
		this.type = TYPE_CLOB;
		init(name, value);
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public ParamObject(String name, Blob[] value) {
		this.type = TYPE_BLOB_ARRAY;
		init(name, value);
	}

	public ParamObject(String name, Timestamp value) {
		this.type = TYPE_TIMES_STAMP;
		init(name, value);
	}

	public ParamObject(String name, Timestamp[] value) {
		this.type = TYPE_TIMES_STAMP_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, Timestamp value, int i) {
		this.type = TYPE_TIMES_STAMP_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	/**
	 * 根据名称、值实例生成包装类
	 * 
	 * @param name
	 * @param value
	 */
	public ParamObject(String name, java.util.Date value) {
		this.type = TYPE_DATE;
		if (value != null) {
			value = new java.sql.Date(value.getTime());
		} else {
			value = null;
		}
		init(name, value);
	}

	public ParamObject(String name, java.sql.Date value) {
		this.type = TYPE_DATE;
		init(name, value);
	}

	public ParamObject(String name, java.sql.Date[] value) {
		this.type = TYPE_DATE_ARRAY;
		init(name, value);
	}

	public ParamObject(String name, java.util.Date[] value) {
		this.type = TYPE_DATE_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, Date value, int i) {
		this.type = TYPE_DATE_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public ParamObject(String name, String value) {
		this.type = TYPE_STRING;
		init(name, value);
	}

	public ParamObject(String name, String[] value) {
		this.type = TYPE_STRING_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, String value, int i) {
		this.type = TYPE_STRING_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public ParamObject(String name, BigDecimal value) {
		this.type = TYPE_BIG_DECIMAL;
		init(name, value);
	}

	public ParamObject(String name, BigDecimal[] value) {
		this.type = TYPE_BIG_DECIMAL_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, BigDecimal value, int i) {
		this.type = TYPE_BIG_DECIMAL_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public ParamObject(String name, Long value) {
		this.type = TYPE_LONG;
		init(name, value);
	}

	public ParamObject(String name, Long[] value) {
		this.type = TYPE_LONG_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, Long value, int i) {
		this.type = TYPE_LONG_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public ParamObject(String name, Integer value) {
		this.type = TYPE_INTEGER;
		init(name, value);
	}

	public ParamObject(String name, Integer[] value) {
		this.type = TYPE_INTEGER_ARRAY;
		init(name, value);
	}

	public ParamObject(String name, Float value) {
		this.type = TYPE_FLOAT;
		init(name, value);
	}

	public ParamObject(String name, Float[] value) {
		this.type = TYPE_FLOAT_ARRAY;
		init(name, value);
	}

	public ParamObject(String name, Double value) {
		this.type = TYPE_DOUBLE;
		init(name, value);
	}

	public ParamObject(String name, Double[] value) {
		this.type = TYPE_DOUBLE_ARRAY;
		init(name, value);
	}

	public void setBatchElement(String name, Integer value, int i) {
		this.type = TYPE_INTEGER_ARRAY;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public ParamObject(String name, int type, Object value) {
		this.type = type;
		init(name, value);
	}

	public ParamObject(String name, byte[] value) {
		this.type = TYPE_BYTE_ARRAY;
		init(name, value);
	}

	public ParamObject(String name, Object value) {
		this.type = TYPE_UNKNOWN;
		init(name, value);
	}

	public void setBatchElement(String name, int type, Object value, int i) {
		this.type = type;
		this.name = name;
		((Object[]) this.value)[i] = value;
	}

	public static Date string2SQLDate(String date, String dateFormat) {
		try {
			long time;
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(false);
			time = sdf.parse(date).getTime();
			return new java.sql.Date(time);
		} catch (ParseException e) {
			throw new IllegalArgumentException("the date string " + date
					+ " is not matching format: " + dateFormat);
		}

	}
}
