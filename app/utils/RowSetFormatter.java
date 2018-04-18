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
 * 分页、排序、表头格式定义

 * @author chen.weizheng
 *
 */
public final class RowSetFormatter {

	/**是否分页显示*/
	private boolean pageFlag;

	/**显示第几页*/
	private int pageIndex;
	
	/**每页显示几条数据*/
	private int pageSize;
	
	/**返回记录总数*/
	private int rowCount;

	/**过滤显示字段列表*/
	private String[] showFields;

	/**排序字段列表*/
	private String[] orderFields;

	/**临时表（For Informix）*/
	private String tempTable;
	
	private String dialectName;

	public RowSetFormatter() {
		pageFlag = false ;
		pageIndex = -1;
		pageSize = 0;
		rowCount = 0 ;
		showFields = null;
		orderFields = null ;
		tempTable = null;
	}
	
	private int hashCode = 0;
	
	public boolean isEmptyFormat(){
		return pageFlag == false &&
				(showFields == null || showFields.length == 0) &&
				(orderFields == null || orderFields.length == 0) &&
				tempTable == null;

	}
	
	public static int getArrayHashCode(Object[] a){
		int h =0;
		if(a != null){
			for(int i=0;i<a.length;i++){
				h = 31*h + (a[i]==null?0:a[i].hashCode());
			}
		}		
		return h;
	}
	
	public int hashCode(){
		if(hashCode == 0 && !isEmptyFormat()){
			if(pageFlag) hashCode = 1237;
			hashCode += 31*hashCode + (showFields==null?0:getArrayHashCode(showFields));
			hashCode += 31*hashCode + (orderFields==null?0:getArrayHashCode(orderFields));
			hashCode += 31*hashCode + (tempTable==null?0:tempTable.hashCode());
		}
		
		return hashCode;
	}
	
	public static boolean compareArray(Object[] a,Object[] b){
		if(a == b){
			return true;
		}
		if(a == null || b == null){
			return false;
		}
		if(a.length != b.length ){
			return false;
		}
		
		for(int i=0;i<a.length;i++){
			if(a[i]==b[i]){
				continue;
			}
			if(a[i] == null || b[i] == null){
				return false;
			}
			if(!a[i].equals(b[i])){
				return false;
			}
		}		
		return true;
	}
	
	public static boolean compareObject(Object a,Object b){
		if(a == b){
			return true;
		}
		if(a == null || b == null){
			return false;
		}
		return a.equals(b);
	}
	
	public boolean equals(Object anRF){
		
		RowSetFormatter rf = (RowSetFormatter)anRF;
		
		if(this.pageFlag != rf.pageFlag){
			return false;
		}
		if(!compareArray(this.showFields,rf.showFields)){
			return false;
		}
		if(!compareArray(this.orderFields,rf.orderFields)){
			return false;
		}
		return compareObject(this.tempTable, rf.tempTable);

	}
	
	/*************************************** bean属性 ************************************************/

	public boolean getPageFlag(){
		return this.pageFlag;
	}

	/**
	 * 返回页码
	 * @return
	 */
	public int getPageIndex() {
		return this.pageIndex;
	}

	/**
	 * 设置页码
	 * @param pageIndex
	 */
	public void setPageIndex(int pageIndex) {
		if(pageIndex >= 0){
			this.pageIndex = pageIndex;
		}
	}

	/**
	 * 获取每页记录数

	 * @return
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 设置每页记录数

	 */
	public void setPageSize(int pageSize) {
		if(pageSize > 0){
			this.pageFlag = true;
			this.pageSize = pageSize;
		}
	}

	/**
	 * 返回最大记录数
	 * @return
	 */
	public int getRowCount() {
		return this.rowCount;
	}

	/**
	 * 设置最大记录数
	 * @param rowCount
	 */
	public void setRowCount(int rowCount) {
		if(rowCount > 0){
			this.pageFlag = true;
			this.rowCount = rowCount;
		}
	}

	public String getTempTable() {
		this.hashCode = 0;
		return tempTable;
	}

	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}

	/**
	 * 获取返回列

	 * @return
	 */
	public String[] getShowFields() {
		return showFields;
	}

	/**
	 * 设置返回列

	 * @param orderFields
	 */
	public void setShowFields(String[] showFields) {
		this.hashCode = 0;
		this.showFields = showFields;
	}

	/**
	 * 获取排序列

	 * @return
	 */
	public String[] getOrderFields() {
		return orderFields;
	}

	/**
	 * 设置排序列

	 * @param orderFields
	 */
	public void setOrderFields(String[] orderFields) {
		this.hashCode = 0;
		this.orderFields = orderFields;
	}

	public String getDialectName() {
		return dialectName;
	}

	public void setDialectName(String dialectName) {
		this.dialectName = dialectName;
	}
}
