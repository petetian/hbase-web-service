package com.petefields.hbaseweb.controller;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.petefields.hbaseweb.ConfigurationComponent;


@RestController
@RequestMapping(value = "/hbase")
public class HbaseCRUDController {
	private static final Logger logger = LoggerFactory.getLogger(HbaseCRUDController.class);

	@Autowired
	ConfigurationComponent configurationComponent;

	/**
	 * 
	 * @param tableName
	 * @param columnFamily
	 * @param rowKey
	 * @param column
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/checkentry/{tableName}/{rowKey}/{columnFamily}/{column}", method = RequestMethod.GET)
	public EntryStatus checkEntry(@PathVariable("tableName") String tableName,
			@PathVariable("columnFamily") String columnFamily, @PathVariable("rowKey") String rowKey,
			@PathVariable("column") String column) {
		logger.info("HBASE Check entry");

		boolean isExist = false;
		EntryStatus entryStatus = new EntryStatus();
		entryStatus.setTableName(tableName);
		entryStatus.setRowKey(rowKey);
		entryStatus.setColumnFamily(columnFamily);
		entryStatus.setColumn(column);

		Table table = null;
		Connection connection = null;
		try {
			connection = ConnectionFactory.createConnection(configurationComponent.getConfiguration());

			logger.debug("Get table: {}", tableName);
			table = connection.getTable(TableName.valueOf(tableName));

			logger.debug("Set rowKey: {}", rowKey);
			Get get = new Get(Bytes.toBytes(rowKey));
			Result result = table.get(get);

			logger.debug("Check entry result: {}", result.toString());
			logger.debug("Check column family: {}; column: {}", columnFamily, column);
			isExist = result.containsColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
		} catch (IOException e) {
			logger.error("Error in checkEntry: {}", e.getMessage());
		} finally {
			closeTableConnection(connection, table);

			entryStatus.setExist(isExist);
			return entryStatus;
		}
	}

	/**
	 * 
	 * @param tableName
	 * @param columnFamily
	 * @param rowKey
	 * @param column
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getentry/{tableName}/{rowKey}/{columnFamily}/{column}", method = RequestMethod.GET)
	public String getEntry(@PathVariable("tableName") String tableName,
			@PathVariable("columnFamily") String columnFamily, @PathVariable("rowKey") String rowKey,
			@PathVariable("column") String column) throws IOException {
		logger.info("HBASE GET entry");

		Table table = null;
		byte[] value = null;

		Connection connection = null;
		try {
			connection = ConnectionFactory.createConnection(configurationComponent.getConfiguration());

			logger.debug("Get table: {}", tableName);
			table = connection.getTable(TableName.valueOf(tableName));

			logger.debug("Set rowKey: {}", rowKey);
			Get get = new Get(Bytes.toBytes(rowKey));
			Result result = table.get(get);

			logger.debug("Check entry result: {}", result.toString());
			logger.debug("Check column family: {}; column: {}", columnFamily, column);
			value = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
		} catch (IOException e) {
			logger.error("Error in getEntry: {}", e.getMessage());
		} finally {
			closeTableConnection(connection, table);

			if (value != null) {
				logger.debug("search result: {}", Bytes.toString(value));
				return Bytes.toString(value);
			} else
				return null;
		}
	}
	
	/**
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param entries
	 * @return
	 */

	@RequestMapping(value = "/putentries/{tableName}/{rowKey}", method = RequestMethod.POST)
	public boolean putEntries(@PathVariable("tableName") String tableName, @PathVariable("rowKey") String rowKey,
			@RequestBody List<HBaseEntry> entries) {
		logger.info("HBASE PUT entry: {}", entries.toString());

		Connection connection = null;
		Table table = null;
		try {
			connection = ConnectionFactory.createConnection(configurationComponent.getConfiguration());
			table = connection.getTable(TableName.valueOf(tableName));
			
			Put put = new Put(Bytes.toBytes(rowKey));

			for (HBaseEntry entry : entries) {
//				boolean isExist = table.getTableDescriptor().hasFamily(Bytes.toBytes(entry.getColumnFamily()));
				
				put.addColumn(Bytes.toBytes(entry.getColumnFamily()), Bytes.toBytes(entry.getColumn()),
						Bytes.toBytes(entry.getValue()));
				table.put(put);
			}

			table.close();
			return true;

		} catch (IOException e) {
			logger.error("Error in putEntries: {}", e.getMessage());
			return false;
		} finally {
			closeTableConnection(connection, table);
		}
	}

	/**
	 * 
	 * @param tableName
	 * @param columnFamily
	 * @return
	 */
	@RequestMapping(value = "/checkCFstatus/{tableName}/{columnFamily}", method = RequestMethod.GET)
	public EntryStatus checkStatus(@PathVariable("tableName") String tableName,
			@PathVariable("columnFamily") String columnFamily) {

		boolean isExist = false;

		Table table = null;
		Connection connection = null;

		EntryStatus entryStatus = new EntryStatus();
		entryStatus.setTableName(tableName);
		entryStatus.setColumnFamily(columnFamily);

		try {
			connection = ConnectionFactory.createConnection(configurationComponent.getConfiguration());
			table = connection.getTable(TableName.valueOf(tableName));
			isExist = table.getTableDescriptor().hasFamily(Bytes.toBytes(columnFamily));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeTableConnection(connection, table);
		}
		entryStatus.setExist(isExist);

		return entryStatus;

	}

	/**
	 * 
	 * @param connection
	 * @param table
	 */
	private void closeTableConnection(Connection connection, Table table) {
		if (table != null) {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
