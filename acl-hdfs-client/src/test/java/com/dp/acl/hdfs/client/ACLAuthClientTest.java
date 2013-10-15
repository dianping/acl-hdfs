package com.dp.acl.hdfs.client;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ACLAuthClientTest {
	
	public static void main(String[] args) throws Exception{
		ACLAuthClient client = new ACLAuthClient("localhost", 7777);
		client.init();
		
		MultiAuthRequest mRequest = new MultiAuthRequest();
		mRequest.addRequest(new AuthRequest("yix.zhang", "hippolog", AuthRequest.ACCESS_MODE_READ));
		mRequest.addRequest(new AuthRequest("erik.fang", "bo.traffic_base", AuthRequest.ACCESS_MODE_WRITE));
		MultiAuthResponse resp = client.send(mRequest, 5);
		System.out.println(resp);
		
		mRequest = new MultiAuthRequest();
		mRequest.addRequest(new AuthRequest("yix.zhang", "hippolog01", AuthRequest.ACCESS_MODE_READ));
		mRequest.addRequest(new AuthRequest("erik.fang", "bo.traffic_base01", AuthRequest.ACCESS_MODE_WRITE));
		resp = client.send(mRequest, 5);
		System.out.println(resp);
		
		client.close();
	}

}
