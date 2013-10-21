package com.dp.acl.hdfs.server.hadoop.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.core.hadoop.ipc.ACLAuthorizationProtocol;
import com.dp.acl.hdfs.server.processor.EncryptionProcessor;
import com.dp.acl.hdfs.server.processor.GetTableHomePathProcessor;
import com.dp.acl.hdfs.server.processor.IProcessor;
import com.dp.acl.hdfs.server.processor.UserProcessor;
import com.dp.acl.hdfs.server.processor.ValidationProcessor;

public class ACLServer implements ACLAuthorizationProtocol{

	private static final String ACL_SERVER_RPC_ADDRESS_KEY = "dfs.acl.server.rpc-address";
	private static final String ACL_SERVER_KEYTAB_FILE_KEY = "dfs.acl.server.keytab.file";
	private static final String ACL_SERVER_USER_NAME_KEY = "dfs.acl.server.kerberos.principal";
	private static final String ACL_SERVER_HANDLER_COUNT_KEY = "dfs.acl.server.handler.count";
	private static final int ACL_SERVER_DEFAULT_PORT = 8080;
	
	private static final Log logger = LogFactory.getLog(ACLServer.class);

	//TODO could move it to a separate class and make it configurable in external files
	private static final List<IProcessor> processors = new ArrayList<IProcessor>();

	private Server rpcServer;

	static{
		processors.add(new UserProcessor());
		processors.add(new ValidationProcessor());
		processors.add(new GetTableHomePathProcessor());
		processors.add(new EncryptionProcessor());
	}

	@Override
	public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
		return ACLAuthorizationProtocol.versionID;
	}

	@Override
	public MultiAuthResponse authorize(MultiAuthRequest request) throws Exception {
		MultiAuthResponse response = new MultiAuthResponse();
		for(IProcessor processor: processors){
			if(!processor.process(request, response))
				break;
		}
		return response;
	}

	public ACLServer(String bindAddress, int port, int serverHandlerCount, Configuration conf) throws IOException, InterruptedException{
		//Security
		UserGroupInformation.setConfiguration(conf);
	    SecurityUtil.login(conf, ACL_SERVER_KEYTAB_FILE_KEY, 
	    		ACL_SERVER_USER_NAME_KEY, bindAddress);
		
		rpcServer = RPC.getServer(this, bindAddress, port, serverHandlerCount, false, conf);
		rpcServer.start();
	}

	public void join() {
		try {
			this.rpcServer.join();
		} catch (InterruptedException ie) {}
	}

	public static void main(String[] args) throws IOException, InterruptedException{
		Configuration conf = new Configuration();
		String addressString = conf.get(ACL_SERVER_RPC_ADDRESS_KEY);
		if(addressString == null){
			logger.error("Bind Address is not specified!");
			throw new RuntimeException("Bind Address is not specified!");	
		}
		InetSocketAddress address = NetUtils.createSocketAddr(addressString, ACL_SERVER_DEFAULT_PORT);
	    int handlerCount = conf.getInt(ACL_SERVER_HANDLER_COUNT_KEY, 5);
	    
	    ACLServer server = new ACLServer(address.getHostName(), address.getPort(),
	    		handlerCount, conf);
	    server.join();
	}
}
