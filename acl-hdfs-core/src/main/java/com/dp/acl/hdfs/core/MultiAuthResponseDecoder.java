package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class MultiAuthResponseDecoder extends AbstractAuthDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes() < 5)
			return;
		
		in.markReaderIndex();
		
		int magicNumber = in.readUnsignedByte();
		if(magicNumber != 'R'){
			in.resetReaderIndex();
			throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
		}
		
		int dataLen = in.readInt();
		if(in.readableBytes() < dataLen){
			in.resetReaderIndex();
			return;
		}
		
		int responseNum = in.readInt();
		MultiAuthResponse msg = new MultiAuthResponse();
		for(int i = 0; i < responseNum; i++){
			msg.addResponse(decodeAuthRequest(in), decodeAuthResponse(in));
		}
		
		out.add(msg);
	}
	
	private AuthRequest decodeAuthRequest(ByteBuf in){
		AuthRequest request = new AuthRequest();
		request.setAccessMode(in.readInt());
		request.setUser(decodeString(in));
		request.setTableName(decodeString(in));
		
		return request;
	}
	
	private AuthResponse decodeAuthResponse(ByteBuf in){
		AuthResponse resp = new AuthResponse();
		resp.setRealUser(decodeString(in));
		resp.setTableHomePath(decodeString(in));
		resp.setEncryptedInfo(decodeString(in));
		
		return resp;
	}

}
