package com.porar.ebooks.stou;

import java.io.Serializable;
import java.util.Hashtable;

public class Config_Data implements Serializable {
	private static final long serialVersionUID = 4907059651876052573L;
	private Hashtable<String,String> data = new Hashtable<String,String>();
	
	public void setData(String key,String value){
		data.put(key, value);
	}

	public String getData(String key) {
		String value;
		if(data.containsKey(key)){
			value = data.get(key);
		}else{
			value = "";
		}
		return value;
	}
	
	public void clearKey(String key){
		data.remove(key);
	}
	
	public void clearData(){
		data.clear();
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
}
