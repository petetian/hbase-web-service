package com.petefields.hbaseweb.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EntryStatus {
	private String tableName;
	private String columnFamily;
	private String column;
	private String rowKey;
	private boolean isExist;

	/**
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

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
	public String getRowKey() {
		return rowKey;
	}

	/**
	 * 
	 * @param rowKey
	 */
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isExist() {
		return isExist;
	}

	/**
	 * 
	 * @param isExist
	 */
	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

}
