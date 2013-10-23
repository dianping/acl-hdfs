package com.dp.acl.hdfs.core.hadoop.ipc;

import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.security.KerberosInfo;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

@KerberosInfo(
	    serverPrincipal = "dfs.acl.server.kerberos.principal")
public interface ACLAuthorizationProtocol extends VersionedProtocol{

	public static final long versionID = 1L;

	public MultiAuthResponse authorize(MultiAuthRequest multiRequest) throws Exception;
}
