package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageDecoder;

abstract public class AbstractAuthDecoder extends ByteToMessageDecoder{
	
	protected String decodeString(ByteBuf in){
		int len = in.readInt();
		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		return new String(bytes);
	}

}
