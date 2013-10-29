package com.dp.acl.hdfs.server.netty;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.server.processor.AuthorizationProcessor;
import com.dp.acl.hdfs.server.processor.EncryptionProcessor;
import com.dp.acl.hdfs.server.processor.GetTableHomePathProcessor;
import com.dp.acl.hdfs.server.processor.IProcessor;
import com.dp.acl.hdfs.server.service.ACLEncryptionService;

public class DefaultACLAuthService implements IACLAuthService{
	
	//TODO could move it to a separate class and make it configurable in external files
	private final List<IProcessor> processors = new ArrayList<IProcessor>();
	
	private ACLEncryptionService encodingService;
	
	DefaultACLAuthService() throws Exception{
		Configuration conf = new Configuration();
		encodingService = new ACLEncryptionService(conf);
		processors.add(new AuthorizationProcessor());
		processors.add(new GetTableHomePathProcessor());
		processors.add(new EncryptionProcessor(encodingService));
	}

	public MultiAuthResponse process(MultiAuthRequest request) throws Exception {
		MultiAuthResponse response = new MultiAuthResponse();
		for(IProcessor processor: processors){
			if(!processor.process(request, response))
				break;
		}
		return response;
	}

}
