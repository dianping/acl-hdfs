package com.dp.acl.hdfs.server;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public interface IACLAuthService {
	
	public MultiAuthResponse process(MultiAuthRequest request);

}
