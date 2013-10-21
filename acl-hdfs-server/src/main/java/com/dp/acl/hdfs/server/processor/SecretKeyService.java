package com.dp.acl.hdfs.server.processor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.security.token.block.ExportedBlockKeys;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.hdfs.server.protocol.NamenodeProtocol;
import org.apache.hadoop.io.retry.RetryPolicies;
import org.apache.hadoop.io.retry.RetryPolicy;
import org.apache.hadoop.io.retry.RetryProxy;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.UserGroupInformation;


public class SecretKeyService {

	private static final long UPDATER_SLEEP_INTERVALS = 30*1000;

	private ExportedBlockKeys keys;
	private NamenodeProtocol namenode;
	private long lastUpdatedTime;

	public SecretKeyService(Configuration conf) throws IOException{
		this.namenode = createNamenode(conf);
		updateSecretKeys();
		SecretKeyUpdater updater = new SecretKeyUpdater(this, UPDATER_SLEEP_INTERVALS);
		updater.start();
	}

	private static final class SecretKeyUpdater extends Thread{

		private static Log logger = LogFactory.getLog(SecretKeyUpdater.class);

		private SecretKeyService service;
		private long keyUpdateIntervals = -1;
		private long sleepIntervals;

		SecretKeyUpdater(SecretKeyService service, long sleepIntervals){
			this.sleepIntervals = sleepIntervals;
			this.service = service;
			ExportedBlockKeys keys = service.keys;
			if(keys != null)
				keyUpdateIntervals = keys.getKeyUpdateInterval();
		}

		@Override
		public void run(){
			while(true){
				try{
					if(System.currentTimeMillis() - service.lastUpdatedTime >= keyUpdateIntervals){
						service.updateSecretKeys();
					}else{
						Thread.sleep(sleepIntervals);
					}
				} catch(Exception e){
					logger.error("Exception ocurrs in updating secret keys:" + e);
					continue;
				}
			}
		}
	}

	private synchronized void updateSecretKeys() throws IOException{
		this.keys = namenode.getBlockKeys();
		this.lastUpdatedTime = System.currentTimeMillis();
	}

	private static NamenodeProtocol createNamenode(Configuration conf)
			throws IOException {
		InetSocketAddress nameNodeAddr = NameNode.getServiceAddress(conf, true);
		RetryPolicy timeoutPolicy = RetryPolicies.exponentialBackoffRetry(
				5, 200, TimeUnit.MILLISECONDS);
		Map<Class<? extends Exception>,RetryPolicy> exceptionToPolicyMap =
				new HashMap<Class<? extends Exception>, RetryPolicy>();
		RetryPolicy methodPolicy = RetryPolicies.retryByException(
				timeoutPolicy, exceptionToPolicyMap);
		Map<String,RetryPolicy> methodNameToPolicyMap =
				new HashMap<String, RetryPolicy>();
		methodNameToPolicyMap.put("getBlocks", methodPolicy);
		methodNameToPolicyMap.put("getAccessKeys", methodPolicy);

		UserGroupInformation ugi = UserGroupInformation.getCurrentUser();

		return (NamenodeProtocol) RetryProxy.create(
				NamenodeProtocol.class,
				RPC.getProxy(NamenodeProtocol.class,
						NamenodeProtocol.versionID,
						nameNodeAddr,
						ugi,
						conf,
						NetUtils.getDefaultSocketFactory(conf)),
						methodNameToPolicyMap);
	}
	
	public String encrypte(String orignal){
		//TODO 
		return null;
	}

}
