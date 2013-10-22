package com.dp.acl.hdfs.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Writable;

public class MultiAuthRequest implements Writable{
	
	private Set<AuthRequest> requests = new HashSet<AuthRequest>();

	public Set<AuthRequest> getRequests() {
		return this.requests;
	}
	
	public void addRequest(AuthRequest request){
		this.requests.add(request);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requests == null) ? 0 : requests.hashCode());
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
		MultiAuthRequest other = (MultiAuthRequest) obj;
		if (requests == null) {
			if (other.requests != null)
				return false;
		} else if (!requests.equals(other.requests))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MultiAuthRequest [requests=" + requests + "]";
	}
	
	public boolean valid(){
		boolean valid = true;
		if(requests.isEmpty()){
			valid = false;
		}
		for(AuthRequest req : requests){
			if(!req.valid()){
				valid = false;
				break;
			}
		}
		return valid;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		int size = in.readInt();
		requests.clear();
		for(int i = 0; i < size; i++){
		     AuthRequest req = (AuthRequest) ACLUtils.newInstance(AuthRequest.class);
		     req.readFields(in);
		     requests.add(req);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(requests.size());
		for(AuthRequest req : requests){
			req.write(out);
		}
	}
}
