package com.dp.acl.hdfs.core.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.io.IOException;
import java.util.List;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class MultiAuthResponseDecoder extends ByteToMessageDecoder{

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
	
	private AuthRequest decodeAuthRequest(ByteBuf in) throws ClassNotFoundException, IOException{
		int requestLen = in.readInt();
		byte[] byteArray = new byte[requestLen];
		in.readBytes(byteArray);
		
		return (AuthRequest)SerDeserUtils.deserialize(byteArray);
	}
	
	private AuthResponse decodeAuthResponse(ByteBuf in) throws ClassNotFoundException, IOException{
		int requestLen = in.readInt();
		byte[] byteArray = new byte[requestLen];
		in.readBytes(byteArray);
		
		return (AuthResponse)SerDeserUtils.deserialize(byteArray);
	}

}
