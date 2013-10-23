package com.dp.acl.hdfs.server.processor;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public interface IProcessor {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return continue or not?
	 */
	public boolean process(MultiAuthRequest request, MultiAuthResponse response)
		throws Exception;

}
