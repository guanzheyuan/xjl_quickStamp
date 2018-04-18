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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;

/**
 * 匿名参数集合类
 * 
 * @author chen.weizheng
 * 
 */
public final class ParamArray {

	public static ParamArray wrap(ParamObject[] paraList) {
		if (paraList == null) {
			return null;
		}

		ParamArray pa = new ParamArray();
		for (int i = 0; i < paraList.length; i++) {
			pa.paraList.add(paraList[i]);
		}

		return pa;
	}

	private ArrayList<ParamObject> paraList = new ArrayList<ParamObject>();

	public ParamArray() {

	}

	public ParamArray(int paraCount, int arrayLen) {
		for (int i = 0; i < paraCount; i++) {
			paraList.add(new ParamObject(arrayLen));
		}
	}

	public void clear() {
		paraList.clear();
	}

	public int getCount() {
		return paraList.size();
	}

	public ArrayList<ParamObject> getParaList() {
		return paraList;
	}

	public void set(String name, Object value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, int type, Object value) {
		paraList.add(new ParamObject(name, type, value));
	}

	public ParamObject get(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para;
	}

	public Object getValue(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getValue();
	}
	
	public void set(String name, Float value) {
		paraList.add(new ParamObject(name, value));
	}
	
	public void set(String name, Float[] value) {
		paraList.add(new ParamObject(name, value));
	}
	
	public void set(String name, float value) {
		paraList.add(new ParamObject(name, value));
	}
	
	public void set(String name, Double value) {
		paraList.add(new ParamObject(name, value));
	}
	
	public void set(String name, Double[] value) {
		paraList.add(new ParamObject(name, value));
	}
	
	public void set(String name, double value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, Integer value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, Integer[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public Integer getAsInteger(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getInteger();
	}

	public void set(String name, Long value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, Long[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public Long getAsLong(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getLong();
	}

	public void set(String name, BigDecimal value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, BigDecimal[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public BigDecimal getAsBigDecimal(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getBigDecimal();
	}

	public void set(String name, String value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, String[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public String getAsString(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getString();
	}

	public void set(String name, java.util.Date value) {
		paraList.add(new ParamObject(name, value));
	}

	public void setDate(String name, Date value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, Date value) {
		if (value == null) {
			paraList.add(new ParamObject(name, (Timestamp) null));
		} else {
			paraList.add(new ParamObject(name, new Timestamp(value.getTime())));
		}
	}

	public void set(String name, Date[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public Date getAsDate(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getDate();
	}

	public void set(String name, Timestamp value) {
		paraList.add(new ParamObject(name, value));
	}

	public void set(String name, Timestamp[] value) {
		paraList.add(new ParamObject(name, value));
	}

	public Timestamp getAsTimestamp(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getTimestamp();
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public void set(String name, Blob value) {
		paraList.add(new ParamObject(name, value));
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public void set(String name, Blob[] value) {
		paraList.add(new ParamObject(name, value));
	}

	// add by chen.weizheng 20070830 支持Blob类型
	public Blob getAsBlob(int i) {
		ParamObject para = (ParamObject) paraList.get(i);
		return para.getBlob();
	}
}
