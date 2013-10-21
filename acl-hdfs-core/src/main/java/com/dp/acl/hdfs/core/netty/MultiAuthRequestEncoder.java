package com.dp.acl.hdfs.core.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.MultiAuthRequest;

public class MultiAuthRequestEncoder extends MessageToByteEncoder<MultiAuthRequest>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MultiAuthRequest msg,
			ByteBuf out) throws Exception {
		if(!valid(msg))
			throw new RuntimeException("authorization requests is not valid");
		
		List<byte[]> requestByteList = new ArrayList<byte[]>();
		for(AuthRequest req : msg.getRequests()){
			if(req != null){
				requestByteList.add(SerDeserUtils.serialize(req));
			}
		}
		
		int dataLen = getDataLength(requestByteList);
		Set<AuthRequest> requests = msg.getRequests();
		
		out.writeByte((byte)'Q');  //magic number
		out.writeInt(dataLen);
		out.writeInt(requests.size());
		for(byte[] requestByte: requestByteList){
			out.writeInt(requestByte.length);
			out.writeBytes(requestByte);
		}
	}

	private int getDataLength(List<byte[]> requestByteList){
		int dataLen = 4; //request number
		for(byte[] requestByte: requestByteList){
			dataLen += 4;  //request len
			dataLen += requestByte.length;
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
