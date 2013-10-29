package com.dp.acl.hdfs.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.junit.Before;
import org.junit.Test;

import com.dp.acl.hdfs.core.ACLDecryptor;

public class AccessControlEncodingServiceTest {
	
	private TestAccessControlEncodingService service;
	
	@Before
	public void setup() throws Exception{
		Configuration conf = new Configuration();
		UserGroupInformation.setConfiguration(conf);
		UserGroupInformation.loginUserFromKeytab("acl_test", "/Users/zhangyi/Downloads/acl_test.keytab");
		service = new TestAccessControlEncodingService(conf, 3000 , 1000);
	}
	
	@Test
	public void test_encrypt_normal_case() throws Exception{
		Token<DelegationTokenIdentifier> token = service.getToken();
		byte[] key = token.getPassword();
		String path = "/tmp";
		String username = "hadoop";
		ACLEncryptionInfo info = service.encrypte(path, username);
		ACLDecryptor decryptor = new ACLDecryptor(key);
		String[] result = decryptor.decrypt(info.getData());
		assertEquals(username, result[0]);
		assertEquals(path, result[1]);
	}
	
	@Test
	public void test_encoder_updater() throws Exception{
		Token<DelegationTokenIdentifier> token1 = service.getToken();
		System.out.println(token1.toString());
		Thread.sleep(5000);
		Token<DelegationTokenIdentifier> token2 = service.getToken();
		System.out.println(token2.toString());
		assertNotNull(token2);
		assertNotEquals(token1, token2);
	}
	
	private static final class TestAccessControlEncodingService extends ACLEncryptionService{

		public TestAccessControlEncodingService(Configuration conf)
				throws Exception {
			super(conf);
		}
		
		public TestAccessControlEncodingService(Configuration conf, long updateInterval, long sleepInterval)
				throws Exception {
			super(conf, updateInterval, sleepInterval);
		}
		
		public Token<DelegationTokenIdentifier> getToken(){
			return this.token;
		}
	}

}
