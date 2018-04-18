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

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: Ztesoft
 * </p>
 * 
 * @author lu.zhen
 * @version 1.0
 */
public class AssertUtil {
    public static <T> void isNotNull(T obj, String msg) {
	if (obj == null) {
	    throw new IllegalArgumentException(msg);
	}
    }

    public static <T> void isNotNull(T obj) {
	isNotNull(obj, "The value is null.");
    }

    public static void isNotEmpty(String str, String msg) {
	if ((str == null) || (str.length() == 0)) {
	    throw new IllegalArgumentException(msg);
	}
    }

    public static void isNotEmpty(String str) {
	isNotEmpty(str, "The value is null.");
    }

    public static void isFalse(boolean blVal, String msg) {
	if (blVal) {
	    throw new IllegalArgumentException(msg);
	}
    }

    public static void isFalse(boolean blVal) {
	isFalse(blVal, "The value is not false.");
    }

    public static void isTrue(boolean blVal, String msg) {
	if (!blVal) {
	    throw new IllegalArgumentException(msg);
	}
    }

    public static void isTrue(boolean blVal) {
	isTrue(blVal, "The value is not true.");
    }

    public static <T,V> void areEqual(T obj1, V obj2, String msg) {
	if (!obj1.equals(obj2)) {
	    throw new IllegalArgumentException(msg);
	}
    }

    public static <T,V>  void areEqual(T obj1, V obj2) {
    	areEqual(obj1, obj2, "The object is not equal.");
    }

    public  static <T> void isNotEmpty(T[] array, String message) {
		if (isEmpty(array)) {
		    throw new IllegalArgumentException(message);
		}
    }

    public static <T> boolean isEmpty(T[] array) {
	return (array == null || array.length == 0);
    }

    public static boolean isEmpty(byte[] array) {
	return (array == null || array.length == 0);
    }

    public static void isNotEmpty(byte[] array, String message) {
	if (isEmpty(array)) {
	    throw new IllegalArgumentException(message);
	}
    }

    public static void isNotEmpty(byte[] array) {
	isNotEmpty(
		array,
		"[Assertion failed] - this byte array must not be empty: it must contain at least 1 element");
    }

    public static <T> void notEmpty(T[] array) {
	isNotEmpty(
		array,
		"[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }
    
    public static void main(String[] argsw){
    	byte[] l = null;
    	AssertUtil.isNotEmpty(l);
    }

	public static void hasLength(String str, String string) {
		if(str.length()==0){
			throw new IllegalArgumentException(string);
		}
	}

}
