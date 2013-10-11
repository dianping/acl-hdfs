package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Set;

public class MultiAuthRequestEncoder extends MessageToByteEncoder<MultiAuthRequest>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MultiAuthRequest msg,
			ByteBuf out) throws Exception {
		if(!valid(msg))
			throw new RuntimeException("authorization requests is not valid");
		
		int dataLen = getDataLength(msg);
		Set<AuthRequest> requests = msg.getRequests();
		
		out.writeByte((byte)'Q');  //magic number
		out.writeInt(dataLen);
		out.writeInt(requests.size());
		for(AuthRequest req : requests){
			encodeAuthRequest(out, req);
		}
	}
	
	private void encodeAuthRequest(ByteBuf out, AuthRequest req){
		out.writeInt(req.getAccessMode());
		out.writeInt(req.getUserLength());
		out.writeBytes(req.getUser().getBytes());
		out.writeInt(req.getTableNameLength());
		out.writeBytes(req.getTableName().getBytes());
	}

	private int getDataLength(MultiAuthRequest msg){
		int dataLen = 4; //request number
		Set<AuthRequest> requests = msg.getRequests();
		for(AuthRequest req : requests){
			dataLen += req.getAccessModeLength();
			dataLen += req.getUserLength();
			dataLen += req.getTableNameLength();
		}
		return dataLen;
	}
	
	private boolean valid(MultiAuthRequest msg){
		boolean valid = true;
		for(AuthRequest req : msg.getRequests()){
			if(!req.valid()){
				valid = false;
				break;
			}
		}
		return valid;
	}
}
