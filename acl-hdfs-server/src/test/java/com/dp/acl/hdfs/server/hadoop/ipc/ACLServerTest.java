package com.dp.acl.hdfs.server.hadoop.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.SecurityUtil;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.core.hadoop.ipc.ACLDFSConfigKeys;

public class ACLServerTest {
	
	private static final class ACLTestServer extends ACLServer{

		public ACLTestServer(String bindAddress, int port,
				int serverHandlerCount, Configuration conf) throws IOException,
				InterruptedException {
			super(bindAddress, port, serverHandlerCount, conf);
		}
		
		@Override
		public MultiAuthResponse authorize(MultiAuthRequest request) throws Exception {
			System.out.println("Received Request: " + request.toString());
			MultiAuthResponse response = new MultiAuthResponse();
			Set<AuthRequest> reqs = request.getRequests();
			for(AuthRequest req : reqs){
				AuthResponse resp = new AuthResponse("hadoop", "/tmp", "ABCD");
				response.addResponse(req, resp);
			}
			return response;
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException{
		Configuration conf = new Configuration();
		String addressString = "localhost:7777";
		InetSocketAddress address = NetUtils.createSocketAddr(addressString, 7777);
	    int handlerCount = 5;
	    ACLServer server = new ACLServer(address.getHostName(), address.getPort(),
	    		handlerCount, conf);
	    server.join();
	}

}
