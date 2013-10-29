package com.dp.acl.hdfs.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.KerberosName;
import org.apache.hadoop.security.token.Token;

import com.dp.acl.hdfs.core.AccessControlEncoder;
import com.dp.acl.hdfs.core.hadoop.ipc.ACLDFSConfigKeys;


public class AccessControlEncodingService {

	private static final long UPDATE_INTERVAL = 24 * 60 * 60 * 1000;
	private static final long SLEEP_INTERVAL = 60 * 60 * 1000;

	protected Token<DelegationTokenIdentifier> token;
	private ClientProtocol dfsClient;
	private long lastUpdatedTime;
	private Configuration conf;
	private AccessControlEncoder encoder;

	public AccessControlEncodingService(Configuration conf) throws Exception{
		this(conf, UPDATE_INTERVAL, SLEEP_INTERVAL);
	}
	
	public AccessControlEncodingService(Configuration conf, long updateInterval, long sleepInterval) throws Exception{
		this.dfsClient = DFSClient.createNamenode(conf);
		this.conf = conf;
		updateAccessControlEncoder();
		EncoderUpdater updater = new EncoderUpdater(this, updateInterval, sleepInterval);
		updater.start();
	}

	private static final class EncoderUpdater extends Thread{

		private static Log logger = LogFactory.getLog(EncoderUpdater.class);

		private AccessControlEncodingService service;
		private long updateInterval = -1;
		private long sleepInterval = -1;

		EncoderUpdater(AccessControlEncodingService service, long updateInterval, long sleepInterval){
			this.updateInterval = updateInterval;
			this.service = service;
			this.sleepInterval = sleepInterval;
		}

		@Override
		public void run(){
			while(true){
				try{
					if(System.currentTimeMillis() - service.lastUpdatedTime >= updateInterval){
						service.updateAccessControlEncoder();
					}else{
						Thread.sleep(sleepInterval);
					}
				} catch(Exception e){
					logger.error("Exception ocurrs in updating access control encoder:" + e);
					continue;
				}
			}
		}
	}

	private synchronized void updateAccessControlEncoder() throws Exception{
		Token<DelegationTokenIdentifier> lastToken = this.token;
		KerberosName aclServerKrbName = new KerberosName(conf.get(ACLDFSConfigKeys.ACL_SERVER_USER_NAME_KEY, ""));
	    String delegTokenRenewer = aclServerKrbName.getShortName();
		this.token = dfsClient.getDelegationToken(new Text(delegTokenRenewer));
		this.lastUpdatedTime = System.currentTimeMillis();
		if(lastToken != null){
			dfsClient.cancelDelegationToken(lastToken);
		}
		encoder = new AccessControlEncoder(token.getPassword());
	}
	
	public synchronized AccessControlInfo encrypte(String path, String username) throws Exception{
		if(encoder != null){
			return new AccessControlInfo(encoder.encode(path, username), token.getIdentifier());
		}else{
			throw new RuntimeException("encoder is empty!");
		}
	}
	
}
