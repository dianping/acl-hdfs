package com.dp.acl.hdfs.client.hadoop.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.core.hadoop.ipc.ACLAuthorizationProtocol;

public class ACLClient {
	
	private ACLAuthorizationProtocol server;
	
	public ACLClient(String addr, int port, Configuration conf) throws IOException{
		InetSocketAddress address = new InetSocketAddress(addr, port);
		server = (ACLAuthorizationProtocol) RPC.getProxy(
				ACLAuthorizationProtocol.class, 
				ACLAuthorizationProtocol.versionID, 
				address, conf);
	}
	
	public MultiAuthResponse authorize(MultiAuthRequest requests) throws Exception{
		return server.authorize(requests);
	}
}
