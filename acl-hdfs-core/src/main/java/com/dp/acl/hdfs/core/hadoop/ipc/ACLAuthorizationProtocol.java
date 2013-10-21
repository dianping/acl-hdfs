package com.dp.acl.hdfs.core.hadoop.ipc;

import org.apache.hadoop.ipc.VersionedProtocol;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public interface ACLAuthorizationProtocol extends VersionedProtocol{

	public static final long versionID = 1L;

	public MultiAuthResponse authorize(MultiAuthRequest multiRequest) throws Exception;
}
