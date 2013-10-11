package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;

public class DecoderUtils {
	
	public String decodeString(ByteBuf in){
		int len = in.readInt();
		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		return new String(bytes);
	}

}
