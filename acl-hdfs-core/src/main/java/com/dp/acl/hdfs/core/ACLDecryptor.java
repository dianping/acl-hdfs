package com.dp.acl.hdfs.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class ACLDecryptor {

	Cipher decryptor;
	MessageDigest md;

	public ACLDecryptor(byte[] key) throws Exception{
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey skey = skf.generateSecret(dks);
		decryptor = Cipher.getInstance("DES");
		decryptor.init(Cipher.DECRYPT_MODE, skey);
		md = MessageDigest.getInstance("MD5");
	}

	/*
	 * return String[]:
	 * String[0] = user
	 * String[1] = path
	 */
	public String[] decrypt(byte[] encryptedString) throws Exception{
		byte[] buf = decryptor.doFinal(encryptedString);
		byte[] hash = Arrays.copyOfRange(buf, 0, 16);
		byte[] decrypted = Arrays.copyOfRange(buf, 16, buf.length);
		byte[] md5 = md.digest(decrypted);
		if(!Arrays.equals(md5, hash)){
			throw new RuntimeException("md5 is not matched!");
		}

		String[] result = new String[2];
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(decrypted));
		result[0] = stream.readUTF();
		result[1] = stream.readUTF();

		return result;
	}

}
