package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelEntityUtils {
	
	
	
    public ExcelEntityUtils(){}
	public String name;
	public List<Map<String,Object>> _map;
	public Double price;
	public String isPay;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<Map<String, Object>> get_map() {
		return _map;
	}
	public void set_map(List<Map<String, Object>> _map) {
		this._map = _map;
	}
	public String getIsPay() {
		return isPay;
	}
	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
}
