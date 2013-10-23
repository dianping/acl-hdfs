package com.dp.acl.hdfs.core.impersonate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class AccessControlEncoder {
    Cipher encryptor;
    MessageDigest md;
    byte[] tokenIdent;
    
    public AccessControlEncoder(byte[] _tokenIdent, byte[] key) {
        tokenIdent = _tokenIdent;
        DESKeySpec dks;
        try {
            dks = new DESKeySpec(key);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey skey = skf.generateSecret(dks);
            encryptor = Cipher.getInstance("DES");
            encryptor.init(Cipher.ENCRYPT_MODE, skey);
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public AccessControlInfo encode(String path, String username) throws IllegalBlockSizeException, BadPaddingException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        out.writeUTF(username);
        out.writeUTF(path);
        byte[] array = bos.toByteArray();
        out.close();
        
        byte[] hash = md.digest(array);
        byte[] buf = new byte[hash.length + array.length];
        System.arraycopy(hash, 0, buf, 0, hash.length);
        System.arraycopy(array, 0, buf, hash.length, array.length);
        return new AccessControlInfo(encryptor.doFinal(buf), tokenIdent);
    }
}