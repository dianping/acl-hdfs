package com.dp.acl.hdfs.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Writable;

public class AuthResponse implements Serializable, Writable{
	
	private static final long serialVersionUID = 5420917516967896978L;
	
	private String realUser;
	private String tableHomePath;
	private byte[] encryptedInfo;
	private byte[] tokenIndentifier;
	
	public AuthResponse(){}
	
	public AuthResponse(String realUser, String tableHomePath,
			byte[] encryptedInfo, byte[] tokenIndentifier) {
		super();
		this.realUser = realUser;
		this.tableHomePath = tableHomePath;
		this.encryptedInfo = encryptedInfo;
		this.tokenIndentifier = tokenIndentifier;
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

	public byte[] getEncryptedInfo() {
		return encryptedInfo;
	}

	public void setEncryptedInfo(byte[] encryptedInfo) {
		this.encryptedInfo = encryptedInfo;
	}
		
	public byte[] getTokenIndentifier() {
		return tokenIndentifier;
	}

	public void setTokenIndentifier(byte[] tokenIndentifier) {
		this.tokenIndentifier = tokenIndentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(encryptedInfo);
		result = prime * result
				+ ((realUser == null) ? 0 : realUser.hashCode());
		result = prime * result
				+ ((tableHomePath == null) ? 0 : tableHomePath.hashCode());
		result = prime * result + Arrays.hashCode(tokenIndentifier);
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
		if (!Arrays.equals(encryptedInfo, other.encryptedInfo))
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
		if (!Arrays.equals(tokenIndentifier, other.tokenIndentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuthResponse [realUser=" + realUser + ", tableHomePath="
				+ tableHomePath + ", encryptedInfo="
				+ Arrays.toString(encryptedInfo) + ", tokenIndentifier="
				+ Arrays.toString(tokenIndentifier) + "]";
	}

	public boolean valid(){
		if(StringUtils.isEmpty(realUser) || 
		   StringUtils.isEmpty(tableHomePath) || 
		   encryptedInfo == null || encryptedInfo.length <= 0 ||
		   tokenIndentifier == null || tokenIndentifier.length <= 0)
			return false;
		return true;
	}

	public void readFields(DataInput in) throws IOException {
		realUser = in.readUTF();
		tableHomePath = in.readUTF();
		int encryptedInfoLen = in.readInt();
		encryptedInfo = new byte[encryptedInfoLen];
		in.readFully(encryptedInfo);
		int tokenIdentifierLen = in.readInt();
		tokenIndentifier = new byte[tokenIdentifierLen];
		in.readFully(tokenIndentifier);
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(realUser);
		out.writeUTF(tableHomePath);
		out.writeInt(encryptedInfo.length);
		out.write(encryptedInfo);
		out.writeInt(tokenIndentifier.length);
		out.write(tokenIndentifier);
		
	}
}
