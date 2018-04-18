package exception;

import java.sql.SQLException;

/**
 * 
 * @author chen.weizheng
 *
 */
public class ExceptionUtil {
	
	public static final int SQLException_ORA = 1;
	public static final int SQLException_TT = 2;
	public static final int SQLException_ABD = 3;
	public static final int SQLException_QDB = 4;
	public static final int SQLException_ZSMART = 10;
	public static final int SQLException_UNKNOWN = 1000;
	
	public static Throwable findMostUsefulInner(Throwable t){
		if(t == null){
			return null;
		}
		Throwable target = t;
		while(target.getCause() != null){
			target = target.getCause();
			if(target instanceof Error){
				return target;
			}
			if(target instanceof SQLException){
				int type = ExceptionUtil.getSQLExceptionVernderType(target);
				switch(type){
				case SQLException_ORA:
				case SQLException_TT:
				case SQLException_ZSMART:
					return target;
				}
			}
		}		
		return target;	
	}
	
	public static Throwable getMostInnerException(Throwable t){
		if(t == null){
			return null;
		}
		
		Throwable target = t;
		while(target.getCause() != null){
			target = target.getCause();
		}		
		return target;	
	}

	public static BaseAppException getFirstBaseAppException(Throwable t){
		Throwable cause = t;
		while(cause != null){
			if(cause instanceof BaseAppException){
				return (BaseAppException)cause;
			} 
			cause = cause.getCause();
		}
		return null;
	}
	
	public static Object exCheck(Object arg){
		if(arg instanceof Throwable){
			arg = ExceptionUtil.exToString((Throwable)arg);
		}
		return arg;
	}
	
	public static String exToString(Throwable t) {
		StringBuffer sb = new StringBuffer();
		sb.append(t.toString());
		for (int i = 0; i < t.getStackTrace().length; i++) {
			sb.append("\r\n\tat ");
			sb.append(t.getStackTrace()[i]);
		}
		Throwable cause = t.getCause();
		while (cause != null) {
			sb.append("\r\nCause by: ");
			if(cause instanceof BaseAppException){
				sb.append(((BaseAppException)cause).getCode()).append("   ");
			}
			sb.append(cause.toString());
			for (int i = 0; i < cause.getStackTrace().length; i++) {
				sb.append("\r\n\tat ");
				sb.append(cause.getStackTrace()[i]);
			}
			cause = cause.getCause();
		}
		return sb.toString();
	}
	
	public static int getSQLExceptionVernderType(Throwable t){
		StackTraceElement[] steList = t.getStackTrace();
		if(steList == null || steList.length<=0){
			return SQLException_UNKNOWN;
		}
		String name = steList[0].getClassName();
		if(name == null){
			return SQLException_UNKNOWN;
		}
		name = name.toLowerCase();
		if(name.startsWith("oracle")){
			return SQLException_ORA;
		}
		if(name.startsWith("com.timesten")){
			return SQLException_TT;
		}
		if(name.startsWith("altibase")){
			return SQLException_ABD;
		}
		if(name.startsWith("com.ztesoft.uboss.core.jdbc.qdbdriver")){
			return SQLException_QDB;
		}
		if(name.startsWith("com.ztesoft")){
			return SQLException_ZSMART;
		}
		return SQLException_UNKNOWN;
	}
}
