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
package utils.jpa;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ParamMap {
	private Map<String, ParamObject> paraMap = new HashMap<String, ParamObject>();

	public static ParamMap wrap(ParamObject[] paraList) {
		if (paraList == null) {
			return null;
		}

		ParamMap pm = new ParamMap();
		for (int i = 0; i < paraList.length; i++) {
			pm.paraMap.put(paraList[i].name, paraList[i]);
		}

		return pm;
	}

	public ParamMap() {

	}

	public int hashCode() {
		int hashCode = 0;
		for (Iterator it = paraMap.values().iterator(); it.hasNext();) {
			ParamObject para = (ParamObject) it.next();
			hashCode = 31 * hashCode + para.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object obj) {
		ParamMap that = (ParamMap) obj;
		if (this == that) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.paraMap.size() != that.paraMap.size()) {
			return false;
		}
		for (Iterator it1 = paraMap.values().iterator(), it2 = that.paraMap
				.values().iterator(); it1.hasNext() && it2.hasNext();) {
			ParamObject para1 = (ParamObject) it1.next();
			ParamObject para2 = (ParamObject) it2.next();
			if (!compareObject(para1, para2)) {
				return false;
			}
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
	
	public void set(String name, Object value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, int type, Object value) {
		paraMap.put(name, new ParamObject(name, type, value));
	}

	public ParamObject get(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para;
	}

	public Object getValue(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getValue();
	}

	public void set(String name, Integer value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, Integer[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public Integer getAsInteger(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getInteger();
	}

	public void set(String name, Long value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, Long[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}
	
	public void set(String name, Float value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, Float[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}
	
	public void set(String name, Double value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, Double[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public Long getAsLong(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getLong();
	}

	public void set(String name, BigDecimal[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	/**
	 * 向集合中添加参数键值对
	 * 
	 * @param name
	 * @param value
	 */
	public void set(String name, BigDecimal value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	/**
	 * 根据名称返回集合中的参数值实例
	 * 
	 * @param name
	 * @return
	 */
	public BigDecimal getAsBigDecimal(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getBigDecimal();
	}

	public void set(String name, String[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, String value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public String getAsString(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getString();
	}

	public void set(String name, java.util.Date value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, byte[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}
	
	public void setDate(String name, Date value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, java.sql.Date value) {
		if (value == null) {
			paraMap.put(name, new ParamObject(name, (Timestamp) null));
		} else {
			paraMap.put(name, new ParamObject(name, new Timestamp(value
					.getTime())));
		}
	}

	public void set(String name, java.sql.Date[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, java.util.Date[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public Date getAsDate(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getDate();
	}

	public void set(String name, Timestamp value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public void set(String name, Timestamp[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	public Timestamp getAsTimestamp(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getTimestamp();
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public void set(String name, Blob value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public void set(String name, Blob[] value) {
		paraMap.put(name, new ParamObject(name, value));
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public Blob getAsBlob(String name) {
		ParamObject para = (ParamObject) paraMap.get(name);
		return para.getBlob();
	}
}
