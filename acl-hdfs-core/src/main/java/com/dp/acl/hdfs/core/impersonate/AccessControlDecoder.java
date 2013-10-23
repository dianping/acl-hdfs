package com.dp.acl.hdfs.core.impersonate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenSecretManager;

public class AccessControlDecoder {
    Cipher decryptor;
    MessageDigest md;
    byte[] decrypted;
    AccessControlInfo info;
    String user;
    String path;
    
    public AccessControlDecoder(DelegationTokenSecretManager tokenSecretManager, AccessControlInfo _info) throws IOException {
        info = _info;
        
        DelegationTokenIdentifier tokenIdentifier = tokenSecretManager.createIdentifier();
        tokenIdentifier.readFields(new DataInputStream(new ByteArrayInputStream(info.getTokenIdentifier())));
        byte[] key = tokenSecretManager.retrievePassword(tokenIdentifier);
        
        DESKeySpec dks;
        try {
            dks = new DESKeySpec(key);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey skey = skf.generateSecret(dks);
            decryptor = Cipher.getInstance("DES");
            decryptor.init(Cipher.DECRYPT_MODE, skey);
            md = MessageDigest.getInstance("md5");
        } catch (Exception e) {
            throw new IOException(e);
        }
        
    }
    
    public String getString(ByteBuffer buf) {
        return new String(buf.array());
    }
    
    public boolean isValid() throws IOException {
        byte[] buf;
        try {
            buf = decryptor.doFinal(info.getData());
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        } catch (BadPaddingException e) {
            throw new IOException(e);
        }
        byte[] hash = Arrays.copyOfRange(buf, 0, 16);
        decrypted = Arrays.copyOfRange(buf, 16, buf.length);
        byte[] md5 = md.digest(decrypted);
        
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(decrypted));
        user = stream.readUTF();
        path = stream.readUTF();
        
        return Arrays.equals(md5, hash);
    }
    
    public byte[] getData() {
        return decrypted;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPath() {
        return path;
    }
}
