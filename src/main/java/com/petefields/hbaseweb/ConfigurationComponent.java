package com.petefields.hbaseweb;

import javax.annotation.PostConstruct;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationComponent {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationComponent.class);
	
	@Value("${hbase.zookeeper.quorum}")
	private String quorum;
	
	@Value("${hbase.zookeeper.property.clientPort}")
	private String clientPort;
	
	@Value("${hbase.master}")
	private String hbaseMaster;
	
	@Value("${zookeeper.znode.parent}")
	private String znodeParent;
	
	private Configuration configuration = HBaseConfiguration.create();
	
	/**
	 * 
	 */
	@PostConstruct
	public void postConstruct() {
		logger.info("hbase.zookeeper.quorum: {}", quorum);
		logger.info("hbase.zookeeper.property.clientPort: {}", clientPort);
		logger.info("hbase.master: {}", hbaseMaster);
		logger.info("zookeeper.znode.parent: {}", znodeParent);
		
		this.setConfiguration(this.getHBaseConfig());
	}

	/**
	 * 
	 * @return
	 */
	public String getQuorum() {
		return quorum;
	}

	/**
	 * 
	 * @param quorum
	 */
	public void setQuorum(String quorum) {
		this.quorum = quorum;
	}

	/**
	 * 
	 * @return
	 */
	public String getClientPort() {
		return clientPort;
	}

	/**
	 * 
	 * @param clientPort
	 */
	public void setClientPort(String clientPort) {
		this.clientPort = clientPort;
	}

	/**
	 * 
	 * @return
	 */
	public String getHbaseMaster() {
		return hbaseMaster;
	}

	/**
	 * 
	 * @param hbaseMaster
	 */
	public void setHbaseMaster(String hbaseMaster) {
		this.hbaseMaster = hbaseMaster;
	}

	/**
	 * 
	 * @return
	 */
	public String getZnodeParent() {
		return znodeParent;
	}

	/**
	 * 
	 * @param znodeParent
	 */
	public void setZnodeParent(String znodeParent) {
		this.znodeParent = znodeParent;
	}
	
	/**
	 * 
	 * @return
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * 
	 * @param configuration
	 */
	private void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * 
	 * @return
	 */
	private Configuration getHBaseConfig() {
		Configuration conf = HBaseConfiguration.create();
		
		logger.info("Setup Hbase configuration");

		if (!this.getQuorum().isEmpty())
                    conf.set("hbase.zookeeper.quorum", this.getQuorum());
		if (!this.getClientPort().isEmpty())
		    conf.set("hbase.zookeeper.property.clientPort", this.getClientPort());
		if (!this.getHbaseMaster().isEmpty())
		    conf.set("hbase.master", this.getHbaseMaster());
		if (!this.getZnodeParent().isEmpty())
		    conf.set("zookeeper.znode.parent", this.getZnodeParent());

		return conf;
	}
}
