package com.areaofit.app.utils;

public class Column {

	private String field;
	private String type;
	private String nullStr;
	private String key;
	private String defaultStr;
	private String extra;
	private String comment;
	private boolean isNewColumn;
	private boolean isNewProp;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNullStr() {
		return nullStr;
	}

	public void setNullStr(String nullStr) {
		this.nullStr = nullStr;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDefaultStr() {
		return defaultStr;
	}

	public void setDefaultStr(String defaultStr) {
		this.defaultStr = defaultStr;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isNewColumn() {
		return isNewColumn;
	}

	public void setNewColumn(boolean isNewColumn) {
		this.isNewColumn = isNewColumn;
	}

	public boolean isNewProp() {
		return isNewProp;
	}

	public void setNewProp(boolean isNewProp) {
		this.isNewProp = isNewProp;
	}
	
	@Override
	public String toString() {
		return "Column [field=" + field + ", type=" + type + ", nullStr=" + nullStr + ", key=" + key + ", defaultStr="
		+ defaultStr + ", extra=" + extra + ", comment=" + comment + ", isNewColumn=" + isNewColumn
		+ ", isNewProp=" + isNewProp + "]";
	}
}
