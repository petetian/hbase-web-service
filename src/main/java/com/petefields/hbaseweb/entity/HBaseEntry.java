package com.petefields.hbaseweb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HBaseEntry {
	@JsonProperty(value = "columnFamily", required = true)
	private String columnFamily;
	
	@JsonProperty(value = "column", required = true)
	private String column;
	
	@JsonProperty(value = "value", required = true)
	private String value;

	/**
	 * 
	 * @return
	 */
	public String getColumnFamily() {
		return columnFamily;
	}

	/**
	 * 
	 * @param columnFamily
	 */
	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	/**
	 * 
	 * @return
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * 
	 * @param column
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
