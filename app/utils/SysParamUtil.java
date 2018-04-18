/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package utils;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import models.RootParams;
import play.Logger;
import play.cache.Cache;

public class SysParamUtil {
	
	//单例模式
    //定义一个SysParamUtil类型的变量（不初始化，注意这里没有使用final关键字）
    private static SysParamUtil instance;
    //定义一个静态的方法（调用时再初始化SysParamUtil，但是多线程访问时，可能造成重复初始化问题）
    public static SysParamUtil getInstance() {
    	if (instance == null){
    		instance = new SysParamUtil();
    	}
    	return instance;
	}
    
	private static Map<String, Object> systemParamMap = new ConcurrentHashMap<String, Object>();

	public static Map<String, Object> getValueMap() {
		return systemParamMap;
	}
	
	@SuppressWarnings("unchecked")
	private static RootParams queryGlobalParamByMask(String mask) {
		if (Cache.get(mask) != null) {
			return (RootParams) Cache.get(mask);
		}
		RootParams dto = RootParams.findByMask(mask);
		if (dto != null) {
			Cache.set(mask, dto);
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	public static String getGlobalParamByMask(String mask) {
		RootParams param = queryGlobalParamByMask(mask);
		if (param != null) {
			return param.currentValue;
		}
		return null;
	}

	public static String getWebRoot() {
		RootParams dto = queryGlobalParamByMask("WEB_ROOT");
		if (dto != null) {
			return dto.currentValue;
		}
		return null;
	}

	/***
	 * 返回当前系统使用什么数据库
	 * @return oracle | postgresql
	 */
	public static String getDataBaseProductName() {
		String dataBaseName = "";
		if (Cache.get("DatabaseProductName") != null) {
			dataBaseName = (String) Cache.get("DatabaseProductName");
		}
		try {
			dataBaseName = play.db.DB.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
			if(StringUtil.isNotEmpty(dataBaseName)){
				Cache.set("DatabaseProductName", dataBaseName);
			}
		} catch (SQLException e) {
			Logger.error("------------ERROR------------"+e.getMessage());
			e.printStackTrace();
		}
		return dataBaseName;
	}

//	public static String getParamValueByOpenIdAndMask(String openId,
//			String mask, String defValue) {
//		try {
//			Vno vno = VnoHelper.getVnoByOpenId(openId);
//			
//			//return getParamValueByMask(vno.vnoId, mask);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return defValue;
//	}



	public static void refreshParamMap(String mask) {
		if (mask == null) {
			systemParamMap.clear();
			return;
		}
		if (systemParamMap.get(mask) != null) {
			systemParamMap.remove(mask);
		}
	}
}
