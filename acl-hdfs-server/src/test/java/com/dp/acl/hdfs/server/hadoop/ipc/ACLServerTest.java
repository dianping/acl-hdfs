package com.dp.acl.hdfs.server.hadoop.ipc;

import java.net.InetSocketAddress;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ACLServerTest {
	
	private static final class ACLTestServer extends ACLServer{

		public ACLTestServer(String bindAddress, int port,
				int serverHandlerCount, Configuration conf) throws Exception {
			super(bindAddress, port, serverHandlerCount, conf);
		}
		
		@Override
		public MultiAuthResponse authorize(MultiAuthRequest request) throws Exception {
			System.out.println("Received Request: " + request.toString());
			MultiAuthResponse response = new MultiAuthResponse();
			Set<AuthRequest> reqs = request.getRequests();
			for(AuthRequest req : reqs){
				AuthResponse resp = new AuthResponse("hadoop", "/tmp", "ABCD".getBytes(), "ABCD".getBytes());
				response.addResponse(req, resp);
			}
			return response;
		}
	}
	
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		String addressString = "localhost:7777";
		InetSocketAddress address = NetUtils.createSocketAddr(addressString, 7777);
	    int handlerCount = 5;
	    ACLServer server = new ACLServer(address.getHostName(), address.getPort(),
	    		handlerCount, conf);
	    server.join();
	}

}
