package controllers.comm;

public class JSResult {
	private String ret = "0";
	private String desc = "success";
	private Object data = null;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JSResult [ret=" + ret + ", desc=" + desc + ", data=" + data
				+ "]";
	}
	
	

}
