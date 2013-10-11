package com.dp.acl.hdfs.core;

import java.util.HashMap;
import java.util.Map;

public class MultiAuthResponse {
	
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
}
