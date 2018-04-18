package controllers.comm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseBO {

	/**
	 * 实体bean转VNO
	 * 
	 * @param o
	 * @param cls
	 * @return
	 */
	public static <T> T fromEJB(Object ob, Class<?> retObj) {
		if (ob == null || retObj == null) {
			return null;
		}

		if (ob.getClass().getName().indexOf("ArrayList") > 0) {
			try {
				List obs = (ArrayList) ob;
				List ret = new ArrayList();
				for (Object data : obs) {
					Object to = retObj.newInstance();
					ret.add(to);
					copy(data, to);
				}
				return (T) ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Class cls = ob.getClass().getSuperclass();
				if (cls.getName().indexOf("Model") > 0) {
					Object to = retObj.newInstance();
					copy(ob, to);
					return (T) to;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	private static void copy(Object from, Object to) {
		try {
			Field[] fields1 = to.getClass().getDeclaredFields();
			Field[] fields2 = from.getClass().getDeclaredFields();
			for (int i = 0; i < fields1.length; i++) {
				fields1[i].setAccessible(true);
				String fName1 = fields1[i].getName();
				for (int j = 0; j < fields2.length; j++) {
					fields2[j].setAccessible(true);
					String fName2 = fields2[j].getName();
					if (fName1.equals(fName2)) {
						if (fields2[j].get(from) != null) {
							fields1[i].set(to, fields2[j].get(from));
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * VO转实体bean
	 * 
	 * @param o
	 * @param cls
	 * @return
	 */
	public static <T> T fromVOList(List<Object> obs, Class<?> retObj) {
		return null;
	}

	/**
	 * VO转实体bean
	 * 
	 * @param o
	 * @param retObj
	 * @return
	 */
	public static <T> T fromVO(Object o, Class<?> retObj) {
		return null;
	}
}
