package com.data;

import java.util.HashMap;

public class RequestFileJson {
	public HashMap<String, Object> header;
	public HashMap<String, Object> data;
	public HashMap<String, Object> getHeader() {
		return header;
	}
	public void setHeader(HashMap<String, Object> header) {
		this.header = header;
	}
	public HashMap<String, Object> getData() {
		return data;
	}
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
}
