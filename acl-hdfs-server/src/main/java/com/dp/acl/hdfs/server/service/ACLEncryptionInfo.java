package com.dp.acl.hdfs.server.service;


public class ACLEncryptionInfo{
    byte[] data;
    byte[] tokenIdentifier;
    
    public ACLEncryptionInfo() {
    }
    
    public ACLEncryptionInfo(byte[] _data, byte[] _tokenIdentifier) {
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
