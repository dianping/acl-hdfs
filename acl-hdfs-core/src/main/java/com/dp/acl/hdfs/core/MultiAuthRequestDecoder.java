package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.io.IOException;
import java.util.List;

public class MultiAuthRequestDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes() < 5)
			return;
		
		in.markReaderIndex();
		
		int magicNumber = in.readUnsignedByte();
		if(magicNumber != 'Q'){
			in.resetReaderIndex();
			throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
		}
		
		int dataLen = in.readInt();
		if(in.readableBytes() < dataLen){
			in.resetReaderIndex();
			return;
		}
		
		int requestNum = in.readInt();
		MultiAuthRequest msg = new MultiAuthRequest();
		for(int i = 0; i < requestNum; i++){
			msg.addRequest(decodeAuthRequest(in));
		}
		
		out.add(msg);
	}
	
	private AuthRequest decodeAuthRequest(ByteBuf in) throws ClassNotFoundException, IOException{
		int requestLen = in.readInt();
		byte[] requestInByte = new byte[requestLen];
		in.readBytes(requestInByte);
		
		return (AuthRequest)SerDeserUtils.deserialize(requestInByte);
	}
	

}
