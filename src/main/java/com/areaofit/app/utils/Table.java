package com.areaofit.app.utils;

import java.util.Map;

public class Table {
	
	private String name;
	private Map<String, Column> colunms;
	private boolean isNewTable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Column> getColunms() {
		return colunms;
	}

	public void setColunms(Map<String, Column> colunms) {
		this.colunms = colunms;
	}

	public boolean isNewTable() {
		return isNewTable;
	}

	public void setNewTable(boolean isNewTable) {
		this.isNewTable = isNewTable;
	}

}
