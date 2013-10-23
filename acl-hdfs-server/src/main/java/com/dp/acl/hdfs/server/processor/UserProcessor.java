package com.dp.acl.hdfs.server.processor;

import org.apache.hadoop.security.UserGroupInformation;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class UserProcessor implements IProcessor{

	@Override
	public boolean process(MultiAuthRequest request, MultiAuthResponse response) throws Exception {
		for(AuthRequest req : request.getRequests()){
			req.setUser(UserGroupInformation.getCurrentUser().getShortUserName());
		}
		return true;


	}

}
