package com.dp.acl.hdfs.core;

import java.util.HashSet;
import java.util.Set;

public class MultiAuthRequest {
	
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
}
