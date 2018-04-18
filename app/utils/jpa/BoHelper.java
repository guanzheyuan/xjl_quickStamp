package utils.jpa;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import utils.DynamicDict;
import utils.StringUtil;
import exception.BaseAppException;
import exception.ExceptionHandler;

public class BoHelper {
	/**
	 * 缓存BO和DTO互转时使用的变量名
	 */
	private static Map<String, String> buffer = new Hashtable<String, String>();

	/**
	 * BO属性是否使用大写的表名字段名规则
	 */
	private static final boolean suppertSlashName = true;

	/**
	 * 将DynamicDict对象转换成DTO对象
	 * 
	 * @param dict
	 *            DynamicDict对象
	 * @param dtoClass
	 *            Dto Class对象
	 * @return dtoObj 转换后的DTO对象
	 * @throws BaseAppException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T boToDto(DynamicDict dict, Class<T> dtoClass)
			throws BaseAppException {
		if (dict == null || dtoClass == null) {
			return null;
		}

		Object dtoObj = null;

		// 用默认的构造方法构造DTO对象,所以被转成BO的DTO必须有默认构造方法
		try {
			dtoObj = dtoClass.newInstance();
			setDtoFieldsValue(dict, dtoClass, dtoObj);
		} catch (Exception e) {
			ExceptionHandler.publish("BoToDto Has Error:", e);
		}

		return (T) dtoObj;
	}

	/**
	 * 根据BO里面的值设置DTO的属性值
	 * 
	 * @param dict
	 * @param dtoClass
	 * @param dtoObj
	 * @param fields
	 * @throws BaseAppException
	 */
	@SuppressWarnings("unchecked")
	private static void setDtoFieldsValue(DynamicDict dict, Class<?> dtoClass,
			Object dtoObj) throws Exception {
		PropertyDescriptor[] des = Introspector.getBeanInfo(dtoClass)
				.getPropertyDescriptors();

		for (PropertyDescriptor property : des) {
			String fieldName = property.getName();

			if ("class".equals(fieldName)) {
				continue;
			}

			String fieldBoName = suppertSlashName ? firstLetterUpperToSlash(fieldName)
					: fieldName;

			// 在BO中没有找到DTO定义的属性
			if (!isExist(dict, fieldBoName)) {
				continue;
			}

			Class<?> fieldType = property.getPropertyType();
			// 获取属性相应的set方法
			Method fieldSetMethod = property.getWriteMethod();

			if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
				if (dict.getLong(fieldBoName) == null) {
					continue;
				}

				fieldSetMethod.invoke(dtoObj, dict.getLong(fieldBoName));
			} else if (fieldType.equals(int.class)
					|| fieldType.equals(Integer.class)) {
				if (dict.get(fieldBoName) == null) {
					continue;
				}

				fieldSetMethod.invoke(dtoObj, dict.getLong(fieldBoName)
						.intValue());
			} else if (fieldType.equals(String.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getString(fieldBoName));
			} else if (fieldType.equals(java.util.Date.class)
					|| fieldType.equals(java.sql.Date.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getDate(fieldBoName));
			} else if (fieldType.equals(boolean.class)
					|| fieldType.equals(Boolean.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getBoolean(fieldBoName));
			} else if (fieldType.equals(List.class)
					|| fieldType.equals(ArrayList.class)) {
				// 先要获取List属性所相应的泛型
				Type fc = property.getReadMethod().getGenericReturnType();
				if (fc == null) {
					continue;
				}
				if (fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> elementType = (Class<?>) pt
							.getActualTypeArguments()[0];
					if (elementType.equals(DynamicDict.class)) {
						fieldSetMethod
								.invoke(dtoObj, dict.getList(fieldBoName));
					} else {
						fieldSetMethod.invoke(dtoObj,
								boToListDto(dict, fieldBoName, elementType));
					}
				}
			} else if (fieldType.equals(Set.class)
					|| fieldType.equals(HashSet.class)) {
				// 先要获取List属性所相应的泛型
				Type fc = property.getReadMethod().getGenericReturnType();
				if (fc == null) {
					continue;
				}
				if (fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> elementType = (Class<?>) pt
							.getActualTypeArguments()[0];
					Set<Object> set;
					if (elementType.equals(DynamicDict.class)) {
						set = new HashSet<Object>(dict.getList(fieldBoName));
						fieldSetMethod.invoke(dtoObj, set);
					} else {
						set = new HashSet<Object>(boToListDto(dict,
								fieldBoName, elementType));
						fieldSetMethod.invoke(dtoObj, set);
					}
				}
			} else if (fieldType.isArray()) {
				Class<?> elementType = fieldType.getComponentType();
				if (elementType.equals(DynamicDict.class)) {
					fieldSetMethod.invoke(dtoObj, dict.getList(fieldBoName));
				} else {
					fieldSetMethod.invoke(dtoObj,
							boToArrayDto(dict, fieldBoName, elementType));
				}
			} else if (fieldType.equals(DynamicDict.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getBO(fieldBoName));
			} else if (fieldType.equals(Double.class)
					|| fieldType.equals(double.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getDouble(fieldBoName));
			} else if (fieldType.equals(Float.class)
					|| fieldType.equals(float.class)) {
				fieldSetMethod.invoke(dtoObj, dict.getFloat(fieldBoName));
			} else {
				fieldSetMethod.invoke(dtoObj,
						boToDto(dict.getBO(fieldBoName), fieldType));
			}
		}
	}

	// private static Blob createBlob(String blobStr) throws BaseAppException
	// {
	// BLOB blob = null;
	// OutputStream outStream = null;
	//
	// try
	// {
	// blob = LobHelper.createOracleBlob();
	// outStream = blob.setBinaryStream(0L);
	// byte[] strs = blobStr.getBytes(XMLDom4jUtils.DEFAULT_ENCODING);
	// outStream.write(strs);
	// outStream.flush();
	// }
	// catch (Exception e)
	// {
	// ExceptionHandler.publish("BoToDto createBlob Has Error:", e);
	// }
	// finally
	// {
	// IOUtils.closeQuietly(outStream);
	// if (blob != null)
	// {
	// try
	// {
	// blob.freeTemporary();
	// }
	// catch (SQLException e)
	// {
	// ExceptionHandler.publish("BoToDto createBlob Has Error:", e);
	// }
	// }
	// }
	//
	// return blob;
	// }

	// /**
	// * 将DynamicDict对象里面的某个List转换成List DTO
	// *
	// * @param dictList BO List对象
	// * @param dtoArrayClass DTO泛型类对象
	// * @return dtoList DTO List对象
	// * @throws BaseAppException
	// */
	// @SuppressWarnings("unchecked")
	// public static List<Object> boToListDto(DynamicDict dict, String
	// dictArryName, Class<?> dtoClass)
	// throws BaseAppException
	// {
	// if (dict == null)
	// {
	// return null;
	// }
	//
	// List<Object> dictArray = dict.getList(dictArryName);
	// List<Object> dtoArray = new ArrayList<Object>(dictArray.size());
	//
	// for (Object obj : dictArray)
	// {
	// Class<?> objClass = obj.getClass();
	//
	// if (objClass.equals(DynamicDict.class))
	// {
	// dtoArray.add(boToDto((DynamicDict) obj, dtoClass));
	// }
	// else if (objClass.equals(dtoClass))
	// {
	// dtoArray.add(obj);
	// }
	// }
	//
	// return dtoArray;
	// }

	/**
	 * 将DynamicDict对象里面的某个List转换成List DTO
	 * 
	 * @param dictList
	 *            BO List对象
	 * @param dtoArrayClass
	 *            DTO泛型类对象
	 * @return dtoList DTO List对象
	 * @throws BaseAppException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> boToListDto(DynamicDict dict,
			String dictArryName, Class<?> dtoClass) throws BaseAppException {
		if (dict == null) {
			return null;
		}

		List<Object> dictArray = dict.getList(dictArryName);
		List<T> dtoArray = new ArrayList<T>(dictArray.size());

		for (Object obj : dictArray) {
			Class<?> objClass = obj.getClass();

			if (objClass.equals(DynamicDict.class)) {
				dtoArray.add((T) boToDto((DynamicDict) obj, dtoClass));
			} else if (objClass.equals(dtoClass)) {
				dtoArray.add((T) obj);
			}
		}

		return dtoArray;
	}

	/**
	 * 将DynamicDict对象里面的某个List转换成Set<DTO>
	 * 
	 * @param dictList
	 *            BO List对象
	 * @param dtoArrayClass
	 *            DTO泛型类对象
	 * @return Set DTO HashSet对象
	 * @throws BaseAppException
	 */
	@SuppressWarnings("unchecked")
	public static Set<Object> boToSetDto(DynamicDict dict, String dictArryName,
			Class<?> dtoClass) throws BaseAppException {
		if (dict == null) {
			return null;
		}

		List<Object> dictArray = dict.getList(dictArryName);
		Set<Object> set = new HashSet<Object>(dictArray.size());

		for (Object obj : dictArray) {
			Class<?> objClass = obj.getClass();
			if (objClass.equals(DynamicDict.class)) {
				set.add(boToDto((DynamicDict) obj, dtoClass));
			} else if (objClass.equals(dtoClass)) {
				set.add(obj);
			}
		}
		return set;
	}

	/**
	 * 将DynamicDict对象里面的某个List转换成Array DTO
	 * 
	 * @param dict
	 *            DynamicDict对象
	 * @param dictArryName
	 *            DynamicDict里面的List所对应Name
	 * @param dtoClass
	 *            DTO类对象
	 * @return dtoArray DTO数组
	 * @throws BaseAppException
	 */
	@SuppressWarnings("unchecked")
	public static Object boToArrayDto(DynamicDict dict, String dictArryName,
			Class<?> dtoClass) throws BaseAppException {
		if (dict == null) {
			return null;
		}

		List<Object> dictArray = dict.getList(dictArryName);
		Object dtoArray = Array.newInstance(dtoClass, dictArray.size());

		int index = 0;
		for (Object obj : dictArray) {
			Class<?> objClass = obj.getClass();

			if (objClass.equals(DynamicDict.class)) {
				Array.set(dtoArray, index, boToDto((DynamicDict) obj, dtoClass));
			} else if (objClass.equals(dtoClass)) {
				Array.set(dtoArray, index, dtoClass);
			}

			index++;
		}

		return dtoArray;
	}

	/**
	 * 将DTO对象转化成DynamicDict对象
	 * 
	 * @param dtoObj
	 *            DTO对象
	 * @param ownDict
	 *            BO自引用对象 如果传入则在BO中直接设置DTO属性,如果不传入则内部构造一个返回
	 * @return dict BO对象
	 * @throws BaseAppException
	 */
	public static DynamicDict dtoToBO(Object dtoObj, DynamicDict ownDict)
			throws BaseAppException {
		if (dtoObj == null) {
			return null;
		}

		DynamicDict dict = null;
		if (ownDict == null) {
			dict = new DynamicDict();
		} else {
			dict = ownDict;
		}

		try {
			setBOFieldValue(dtoObj, dict);
		} catch (Exception e) {
			ExceptionHandler.publish("dtoToBO Has Error:", e);
		}

		return dict;
	}

	private static void setBOFieldValue(Object dtoObj, DynamicDict dict)
			throws Exception {
		PropertyDescriptor[] des = Introspector.getBeanInfo(dtoObj.getClass())
				.getPropertyDescriptors();

		for (PropertyDescriptor property : des) {
			Object resobj = property.getReadMethod().invoke(dtoObj);

			String fieldName = property.getName();

			if ("class".equals(fieldName)) {
				continue;
			}

			String fieldBoName = suppertSlashName ? firstLetterUpperToSlash(fieldName)
					: fieldName;

			if (resobj == null) {
				dict.valueMap.put(fieldBoName, null);
				continue;
			}

			Class<?> fieldType = property.getPropertyType();

			if (isSimpleType(fieldType)) {
				dict.set(fieldBoName, resobj);
			} else if (fieldType.equals(List.class)
					|| fieldType.equals(ArrayList.class)) {
				// 先要获取List属性所相应的泛型
				Type fc = property.getReadMethod().getGenericReturnType();
				List<?> listDto = (List<?>) resobj;
				if (fc != null && fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> elementType = (Class<?>) pt
							.getActualTypeArguments()[0];

					listDtoToBO(fieldBoName, listDto, elementType, dict);
				} else {

					listDtoToBO(fieldBoName, listDto, null, dict);
				}
			} else if (fieldType.isArray()) {
				Class<?> elementType = fieldType.getComponentType();

				Object[] arrayDto = (Object[]) resobj;

				for (Object dto : arrayDto) {
					if (elementType.equals(DynamicDict.class)
							|| isSimpleType(elementType)) {
						dict.add(fieldBoName, dto);
					} else {
						dict.add(fieldBoName, dtoToBO(dto, null));
					}
				}
			} else if (fieldType.equals(Set.class)
					|| fieldType.equals(HashSet.class)) {
				// 先要获取List属性所相应的泛型
				Type fc = property.getReadMethod().getGenericReturnType();
				if (fc == null) {
					continue;
				}
				if (fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> elementType = (Class<?>) pt
							.getActualTypeArguments()[0];

					Set<?> setDto = (Set<?>) resobj;

					for (Object dto : setDto) {
						if (isSimpleType(elementType)) {
							dict.add(fieldBoName, dto);
						} else {
							dict.add(fieldBoName, dtoToBO(dto, null));
						}
					}
				}
			} else if (fieldType.equals(DynamicDict.class)) {
				dict.set(fieldBoName, resobj);
			} else if (fieldType.equals(Blob.class)) {
				dict.set(fieldBoName, blobToStr((Blob) resobj));
			} else {
				dict.set(fieldBoName, dtoToBO(resobj, null));
			}
		}
	}

	private static String blobToStr(Blob blob) throws BaseAppException {
		InputStream inputStream = null;

		try {
			inputStream = blob.getBinaryStream();
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);

			return new String(data);
		} catch (Exception e) {
			ExceptionHandler.publish("dtoToBO blobToStr Has Error:", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		return "";
	}

	/**
	 * 将List DTO转换成DynamicDict对象
	 * 
	 * @param boListName
	 *            转换后DynamicDict对象里面DTO List的名称
	 * @param dtoList
	 *            需要转换的DTO LIST
	 * @param elementTypeClass
	 *            需要转换的DTO LIST的泛型类对象
	 * @param ownDict
	 *            BO自引用对象 如果传入则在BO中直接设置DTO属性,如果不传入则内部构造一个返回
	 * @return dict DynamicDict对象
	 * @throws BaseAppException
	 */
	public static DynamicDict listDtoToBO(String boListName, List<?> dtoList,
			Class<?> elementTypeClass, DynamicDict ownDict)
			throws BaseAppException {
		if (StringUtil.isEmpty(boListName) || dtoList == null) {
			return null;
		}

		DynamicDict dict;
		if (ownDict == null) {
			dict = new DynamicDict();
		} else {
			dict = ownDict;
		}

		List<Object> list = new ArrayList<Object>();

		for (Object dtoObj : dtoList) {
			if (elementTypeClass == null) {

				elementTypeClass = dtoObj.getClass();
			}

			if (isSimpleType(elementTypeClass)) {
				list.add(dtoObj);
			} else {
				list.add(dtoToBO(dtoObj, null));
			}
		}

		dict.set(boListName, list);

		return dict;
	}

	public static DynamicDict listDtoToBO(String boListName, List<?> dtoList,
			DynamicDict ownDict) throws BaseAppException {
		if (StringUtil.isEmpty(boListName) || dtoList == null) {
			return null;
		}

		DynamicDict dict;
		if (ownDict == null) {
			dict = new DynamicDict();
		} else {
			dict = ownDict;
		}

		List<Object> list = new ArrayList<Object>();

		for (Object dtoObj : dtoList) {
			if (isSimpleType(dtoObj.getClass())) {
				list.add(dtoObj);
			} else if (DynamicDict.class.equals(dtoObj.getClass())) {
				list.add(dtoObj);
			} else {
				list.add(dtoToBO(dtoObj, null));
			}
		}

		dict.set(boListName, list);

		return dict;
	}

	/**
	 * 将数组DTO转换成DynamicDict对象
	 * 
	 * @param boListName
	 *            转换后DynamicDict对象里面DTO List的名称
	 * @param dtoArray
	 *            需要转换DTO数组对象
	 * @param ownDict
	 *            BO自引用对象 如果传入则在BO中直接设置DTO属性,如果不传入则内部构造一个返回
	 * @return dict DynamicDict对象
	 * @throws BaseAppException
	 */
	public static DynamicDict arrayDtoToBO(String boListName,
			Object[] dtoArray, DynamicDict ownDict) throws BaseAppException {
		if (StringUtil.isEmpty(boListName) || dtoArray == null) {
			return null;
		}

		DynamicDict dict = null;
		if (ownDict == null) {
			dict = new DynamicDict();
		} else {
			dict = ownDict;
		}

		for (Object dtoObj : dtoArray) {
			Class<?> elementTypeClass = dtoArray.getClass().getComponentType();

			if (isSimpleType(elementTypeClass)) {
				dict.add(boListName, dtoObj);
			} else {
				dict.add(boListName, dtoToBO(dtoObj, null));
			}
		}

		return dict;
	}

	/**
	 * @description 将BO对象转为SQL用的参数<br>
	 * @param dict
	 *            dict BO对象
	 * @return <br>
	 */
	public static ParamMap boToParamMap(DynamicDict dict) {
		ParamMap ParamMap = new ParamMap();
		HashMap<String, Object> map = dict.valueMap;
		Iterator<Map.Entry<String, Object>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			ParamMap.set(entry.getKey(), ParamType.get(entry.getValue()),
					entry.getValue());
		}
		return ParamMap;
	}

	/**
	 * @description 将BO对象转为SQL用的参数<br>
	 * @param dict
	 *            BO对象
	 * @param params
	 *            在SQL需要用的键值名称 -- 如果BO是前台通过XML传进来的话,类型都是STRING,此时慎用
	 * @return <br>
	 */
	public static ParamMap boToParamMap(DynamicDict dict, String... params) {
		ParamMap ParamMap = new ParamMap();
		HashMap<String, Object> map = dict.valueMap;
		Object o = null;
		for (String param : params) {
			o = map.get(param);
			if (o != null) {
				ParamMap.set(param, ParamType.get(o), o);
			}
		}
		return ParamMap;
	}

	public static boolean isSimpleType(Class<?> fieldType) {
		if (fieldType.isPrimitive()) {
			return true;
		} else if (fieldType.equals(Long.class)
				|| fieldType.equals(String.class)
				|| fieldType.equals(Boolean.class)
				|| fieldType.equals(java.sql.Date.class)
				|| fieldType.equals(java.util.Date.class)
				|| fieldType.equals(byte[].class)
				|| fieldType.equals(Integer.class)
				|| fieldType.equals(int.class)
				|| fieldType.equals(Double.class)
				|| fieldType.equals(Float.class)) {
			return true;
		}

		return false;
	}

	/**
	 * 型如XxxYyyZxx的子串改为XXX_YYY_ZZZ的子串
	 * 
	 * @param input
	 * @return
	 */
	public static String firstLetterUpperToSlash(String input) {
		if (input == null) {
			return null;
		}

		// 先在缓冲区中找
		String ret = buffer.get(input);
		if (ret != null) {
			return ret;
		}
		// 转换
		String inputNoDto = input.replaceAll("ID", "Id").replaceAll("Dto", "");

		StringBuffer output = new StringBuffer();
		int beg = 0;
		int len = inputNoDto.length();
		for (int i = 0; i < len; i++) {
			if (Character.isUpperCase(inputNoDto.charAt(i))) {
				if (beg < i) {
					output.append(inputNoDto.substring(beg, i).toUpperCase());
					output.append("_");
					beg = i;
				} else {
					beg = i;
				}
			}
		}
		output.append(inputNoDto.substring(beg).toUpperCase());
		ret = output.toString();
		// 加入缓冲
		if (buffer.size() < 10000)// 防止缓冲爆掉
		{
			buffer.put(input, ret);
		}
		return ret;
	}

	/**
	 * 检查属性在dict中是否存在
	 * 
	 * @param dict
	 * @param fieldName
	 * @return
	 * @throws BaseAppException
	 */
	private static boolean isExist(DynamicDict dict, String fieldName)
			throws BaseAppException {
		if (dict == null) {
			return false;
		}
		Object obj;

		obj = dict.valueMap.get(fieldName);

		if (obj == null) {
			return false;
		}

		return true;
	}

}