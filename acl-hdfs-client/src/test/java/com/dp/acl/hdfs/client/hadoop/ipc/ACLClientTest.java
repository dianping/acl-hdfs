package com.dp.acl.hdfs.client.hadoop.ipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ACLClientTest {
	
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		//security
		UserGroupInformation.setConfiguration(conf);
		UserGroupInformation.loginUserFromKeytab("acl_test", "/Users/zhangyi/Downloads/acl_test.keytab");
		ACLClient client = new ACLClient("localhost", 7777, conf);
		
		MultiAuthRequest mRequest = new MultiAuthRequest();
		mRequest.addRequest(new AuthRequest("yix.zhang", "hippolog", AuthRequest.ACCESS_MODE_READ));
		mRequest.addRequest(new AuthRequest("erik.fang", "bo.traffic_base", AuthRequest.ACCESS_MODE_WRITE));
		MultiAuthResponse resp = client.authorize(mRequest);
		System.out.println(resp);
		
		mRequest = new MultiAuthRequest();
		mRequest.addRequest(new AuthRequest("yix.zhang", "hippolog01", AuthRequest.ACCESS_MODE_READ));
		mRequest.addRequest(new AuthRequest("erik.fang", "bo.traffic_base01", AuthRequest.ACCESS_MODE_WRITE));
		resp = client.authorize(mRequest);
		System.out.println(resp);
	}

}
