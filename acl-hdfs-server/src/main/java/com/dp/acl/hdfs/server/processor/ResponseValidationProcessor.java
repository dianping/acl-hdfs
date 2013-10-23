package com.dp.acl.hdfs.server.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ResponseValidationProcessor implements IProcessor{
	
	private static final Log logger = LogFactory.getLog(ResponseValidationProcessor.class);

	@Override
	public boolean process(MultiAuthRequest request, MultiAuthResponse response)
			throws Exception {
		if(!response.valid()){
			logger.error("multiresponse is invalid! print all responses: " + response.toString());
			throw new RuntimeException("There are some internel problems in authorzation, response is invalid");
		}
		return true;
	}

}
