package com.dp.acl.hdfs.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;

public class MultiAuthResponse implements Writable{
	
	private Map<AuthRequest, AuthResponse> responses = new HashMap<AuthRequest, AuthResponse>();

	public Map<AuthRequest, AuthResponse> getResponses() {
		return responses;
	}

	public void addResponse(AuthRequest request, AuthResponse response){
		this.responses.put(request, response);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((responses == null) ? 0 : responses.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiAuthResponse other = (MultiAuthResponse) obj;
		if (responses == null) {
			if (other.responses != null)
				return false;
		} else if (!responses.equals(other.responses))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MultiAuthResponse [responses=" + responses + "]";
	}

	public boolean valid(){
		boolean valid = true;
		if(responses.isEmpty()){
			valid = false;
		}
		for(Map.Entry<AuthRequest, AuthResponse> entry: responses.entrySet()){
			AuthRequest req = entry.getKey();
			AuthResponse resp = entry.getValue();
			if(!req.valid() || !resp.valid()){
				valid = false;
				break;
			}
		}
		return valid;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		int size = in.readInt();
		responses.clear();
		for(int i = 0; i < size; i++){
		     AuthRequest req = (AuthRequest) ACLUtils.newInstance(AuthRequest.class);
		     req.readFields(in);
		     AuthResponse res = (AuthResponse) ACLUtils.newInstance(AuthResponse.class);
		     res.readFields(in);
		     responses.put(req, res);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(responses.size());
		for(Entry<AuthRequest, AuthResponse> entry : responses.entrySet()){
			entry.getKey().write(out);
			entry.getValue().write(out);
		}
	}
}
