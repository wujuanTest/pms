package com.pms.dto;

import java.util.List;

public class AttrDictItem {
	private int id;
	private String name;
	private String code;
	private List<String> dictionary;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getDictionary() {
		return dictionary;
	}
	public void setDictionary(List<String> dictionary) {
		this.dictionary = dictionary;
	}
}
