package com.dp.acl.hdfs.server.processor;

import java.util.Map.Entry;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;
import com.dp.acl.hdfs.server.service.ACLEncryptionService;
import com.dp.acl.hdfs.server.service.ACLEncryptionInfo;

public class EncryptionProcessor implements IProcessor{
	
	private ACLEncryptionService service;
	
	public EncryptionProcessor(ACLEncryptionService service){
		this.service = service;
	}

	public boolean process(MultiAuthRequest request, MultiAuthResponse response) throws Exception{
		for(Entry<AuthRequest, AuthResponse> entry : response.getResponses().entrySet()){
			AuthResponse resp = entry.getValue();
			ACLEncryptionInfo info = service.encrypte(
					resp.getTableHomePath(), 
					resp.getRealUser());
			resp.setEncryptedInfo(info.getData());
			resp.setTokenIndentifier(info.getTokenIdentifier());
		}
		return true;
	}

}