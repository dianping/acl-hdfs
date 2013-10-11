package com.dp.acl.hdfs.core;

import org.apache.commons.lang.StringUtils;

public class AuthRequest {
	
	public static final int NONE = -1;
	public static final int ACCESS_MODE_READ = 0;
	public static final int ACCESS_MODE_WRITE = 1;
	
	private String user;
	private String tableName;
	private int accessMode = NONE;

	public AuthRequest(){}
	
	public AuthRequest(String user, String tableName, String tableHomePath,
			int accessMode) {
		super();
		this.user = user;
		this.tableName = tableName;
		this.accessMode = accessMode;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getAccessMode() {
		return accessMode;
	}
	public void setAccessMode(int accessMode) {
		this.accessMode = accessMode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessMode;
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		AuthRequest other = (AuthRequest) obj;
		if (accessMode != other.accessMode)
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AuthRequest [user=" + user + ", tableName=" + tableName
				+ ", accessMode=" + accessMode + "]";
	}

	public boolean valid(){
		if(accessMode == NONE || StringUtils.isEmpty(user) || StringUtils.isEmpty(tableName))
			return false;
		return true;
	}
	
	public int getUserLength(){
		return user.getBytes().length;
	}
	
	public int getTableNameLength(){
		return tableName.getBytes().length;
	}
	
	public int getAccessModeLength(){
		return 4;
	}

}
