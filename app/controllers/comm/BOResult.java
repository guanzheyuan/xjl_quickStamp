package controllers.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * ResultSet
 * 
 * @author leeyb
 *
 */
public class BOResult {
	public static final String DEF_NAME = "controllers.comm.BOResultSet";
	public static final String DEF_KEY = "controllers.comm.BOResultSet_DEF_KEY_";

	private Class<?> _CallClass;
	private boolean _IsRoot = false;
	private boolean _IsSuccess = true;
	private String _ReturnCode = "0";
	private String _ReturnInfo = "成功";
	private Throwable _ReturnThrowable = null;

	private Map<String, Object> _dataMap = new HashMap<String, Object>();//

	public BOResult() {
	}

	public BOResult(Class<?> thisClass) {
		_CallClass = thisClass;
	}

	public Class<?> getCallClass() {
		return _CallClass;
	}

	public boolean isSuccess() {
		return _IsSuccess;
	}

	public String getReturnInfo() {
		return _ReturnInfo;
	}

	public String getReturnCode() {
		return _ReturnCode;
	}

	public void setReturnInfo(String info) {
		_ReturnCode = "0";
		_ReturnInfo = info;
	}

	public Throwable getException() {
		return _ReturnThrowable;
	}

	public void setError(String code, String info) {
		_IsSuccess = false;
		_ReturnCode = code;
		_ReturnInfo = info;
	}

	public void setError(String code, String info, Throwable e) {
		_IsSuccess = false;
		_ReturnCode = code;
		_ReturnInfo = info;
		_ReturnThrowable = e;
	}

	public void addValue(String key, Object value) {
		_dataMap.put(key, value);
	}

	public void addValue(Object value) {
		_dataMap.put(DEF_KEY, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, Class<T> type) {
		return (T) _dataMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> type) {
		return (T) _dataMap.get(DEF_KEY);
	}

	public Object getValue(String key) {
		return _dataMap.get(key);
	}

	public boolean isRoot() {
		return _IsRoot;
	}

	public void setRoot(boolean _IsRoot) {
		this._IsRoot = _IsRoot;
	}

}
