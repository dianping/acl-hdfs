package com.dp.acl.hdfs.core;

import org.apache.commons.lang.StringUtils;

public class AuthResponse {
	
	private String realUser;
	private String tableHomePath;
	private String encryptedInfo;
	
	public AuthResponse(){}
	
	public AuthResponse(String realUser, String tableHomePath,
			String encryptedInfo) {
		super();
		this.realUser = realUser;
		this.tableHomePath = tableHomePath;
		this.encryptedInfo = encryptedInfo;
	}
	public String getRealUser() {
		return realUser;
	}
	public void setRealUser(String realUser) {
		this.realUser = realUser;
	}
	public String getTableHomePath() {
		return tableHomePath;
	}
	public void setTableHomePath(String tableHomePath) {
		this.tableHomePath = tableHomePath;
	}
	public String getEncryptedInfo() {
		return encryptedInfo;
	}
	public void setEncryptedInfo(String encryptedInfo) {
		this.encryptedInfo = encryptedInfo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((encryptedInfo == null) ? 0 : encryptedInfo.hashCode());
		result = prime * result
				+ ((realUser == null) ? 0 : realUser.hashCode());
		result = prime * result
				+ ((tableHomePath == null) ? 0 : tableHomePath.hashCode());
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
		AuthResponse other = (AuthResponse) obj;
		if (encryptedInfo == null) {
			if (other.encryptedInfo != null)
				return false;
		} else if (!encryptedInfo.equals(other.encryptedInfo))
			return false;
		if (realUser == null) {
			if (other.realUser != null)
				return false;
		} else if (!realUser.equals(other.realUser))
			return false;
		if (tableHomePath == null) {
			if (other.tableHomePath != null)
				return false;
		} else if (!tableHomePath.equals(other.tableHomePath))
			return false;
		return true;
	}
	
	public boolean valid(){
		if(StringUtils.isEmpty(realUser) || 
		   StringUtils.isEmpty(tableHomePath) || 
		   StringUtils.isEmpty(encryptedInfo))
			return false;
		return true;
	}
	
	public int getRealUserLength(){
		return realUser.getBytes().length;
	}
	
	public int getTableHomePathLength(){
		return tableHomePath.getBytes().length;
	}
	
	public int getEncryptedInfoLength(){
		return encryptedInfo.getBytes().length;
	}
}
