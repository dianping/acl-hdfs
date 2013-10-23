package com.dp.acl.hdfs.core.impersonate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.Writable;

public class AccessControlInfo implements Writable {
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

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(data.length);
        out.write(data);
        out.writeInt(tokenIdentifier.length);
        out.write(tokenIdentifier);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        int datalength = in.readInt();
        data = new byte[datalength];
        in.readFully(data);
        int tokenlength = in.readInt();
        tokenIdentifier = new byte[tokenlength];
        in.readFully(tokenIdentifier);
    }    
}
