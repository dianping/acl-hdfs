package com.dp.acl.hdfs.server.netty;

import java.util.ArrayList;
import java.util.List;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.server.processor.EncryptionProcessor;
import com.dp.acl.hdfs.server.processor.GetTableHomePathProcessor;
import com.dp.acl.hdfs.server.processor.IProcessor;
import com.dp.acl.hdfs.server.processor.ValidationProcessor;

public class DefaultACLAuthService implements IACLAuthService{
	
	//TODO could move it to a separate class and make it configurable in external files
	private static final List<IProcessor> processors = new ArrayList<IProcessor>();
	
	static{
		processors.add(new ValidationProcessor());
		processors.add(new GetTableHomePathProcessor());
		processors.add(new EncryptionProcessor());
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
