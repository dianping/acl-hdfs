package com.dp.acl.hdfs.server.service;


public class AccessControlInfo{
    byte[] data;
    byte[] tokenIdentifier;
    
    public AccessControlInfo() {
    }
    
    public AccessControlInfo(byte[] _data, byte[] _tokenIdentifier) {
        data = _data;
        tokenIdentifier = _tokenIdentifier;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getTokenIdentifier() {
        return tokenIdentifier;
    }
}
