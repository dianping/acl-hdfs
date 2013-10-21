package com.dp.acl.hdfs.server.netty;

import java.util.Set;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.server.netty.ACLAuthServer;
import com.dp.acl.hdfs.server.netty.IACLAuthService;

public class ACLAuthServerTest {
	
	public static void main(String[] args) throws Exception{
		ACLAuthServer server = new ACLAuthServer(7777, new ACLAuthTestService());
		server.run();
	}
	
	private static class ACLAuthTestService implements IACLAuthService{

		public MultiAuthResponse process(MultiAuthRequest request) {
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

}


