package com.dp.acl.hdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Map;

public class MultiAuthResponseEncoder extends MessageToByteEncoder<MultiAuthResponse>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MultiAuthResponse msg,
			ByteBuf out) throws Exception {
		if(!valid(msg))
			throw new RuntimeException("authorization responses is not valid");
		
		int dataLen = getDataLength(msg);
		Map<AuthRequest, AuthResponse> responseMap = msg.getResponses();
		
		out.writeByte((byte)'R');  //magic number
		out.writeInt(dataLen);
		out.writeInt(responseMap.size());
		
		for(Map.Entry<AuthRequest, AuthResponse> entry: responseMap.entrySet()){
			encodeAuthResponse(out, entry.getKey(), entry.getValue());
		}
	}
	
	private void encodeAuthResponse(ByteBuf out, AuthRequest req, AuthResponse resp){
		out.writeInt(req.getAccessMode());
		out.writeInt(req.getUserLength());
		out.writeBytes(req.getUser().getBytes());
		out.writeInt(req.getTableNameLength());
		out.writeBytes(req.getTableName().getBytes());
		out.writeInt(resp.getRealUserLength());
		out.writeBytes(resp.getRealUser().getBytes());
		out.writeInt(resp.getTableHomePathLength());
		out.writeBytes(resp.getTableHomePath().getBytes());
		out.writeInt(resp.getEncryptedInfoLength());
		out.writeBytes(resp.getEncryptedInfo().getBytes());
	}
	
	private int getDataLength(MultiAuthResponse msg){
		int dataLen = 4;  //response number
		for(Map.Entry<AuthRequest, AuthResponse> entry: msg.getResponses().entrySet()){
			AuthRequest req = entry.getKey();
			dataLen += req.getAccessModeLength();
			dataLen += req.getUserLength();
			dataLen += req.getTableNameLength();
			AuthResponse resp = entry.getValue();
			dataLen += resp.getRealUserLength();
			dataLen += resp.getTableHomePathLength();
			dataLen += resp.getEncryptedInfoLength();
		}
		return dataLen;
	}
	
	private boolean valid(MultiAuthResponse msg){
		boolean valid = true;
		for(Map.Entry<AuthRequest, AuthResponse> entry: msg.getResponses().entrySet()){
			AuthRequest req = entry.getKey();
			AuthResponse resp = entry.getValue();
			if(!req.valid() || !resp.valid()){
				valid = false;
				break;
			}
		}
		return valid;
	}

}
