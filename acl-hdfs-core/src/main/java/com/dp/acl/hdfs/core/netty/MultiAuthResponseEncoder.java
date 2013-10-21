package com.dp.acl.hdfs.core.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class MultiAuthResponseEncoder extends MessageToByteEncoder<MultiAuthResponse>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MultiAuthResponse msg,
			ByteBuf out) throws Exception {
		if(!valid(msg))
			throw new RuntimeException("authorization responses is not valid");
		
		List<byte[]> requestByteList = new ArrayList<byte[]>();
		List<byte[]> responseByteList = new ArrayList<byte[]>();
		for(Map.Entry<AuthRequest, AuthResponse> entry: msg.getResponses().entrySet()){
			if(entry != null && entry.getKey() != null && entry.getValue() != null){
				requestByteList.add(SerDeserUtils.serialize(entry.getKey()));
				responseByteList.add(SerDeserUtils.serialize(entry.getValue()));
			}
		}
		
		int dataLen = getDataLength(requestByteList, responseByteList);
		Map<AuthRequest, AuthResponse> responseMap = msg.getResponses();
		
		out.writeByte((byte)'R');  //magic number
		out.writeInt(dataLen);
		out.writeInt(responseMap.size());
		
		for(int i = 0; i <  requestByteList.size(); i++){
			byte[] requestByte = requestByteList.get(i);
			byte[] responseByte = responseByteList.get(i);
			out.writeInt(requestByte.length);
			out.writeBytes(requestByte);
			out.writeInt(responseByte.length);
			out.writeBytes(responseByte);
		}
	}
	
	private int getDataLength(List<byte[]> requestByteList, List<byte[]> responseByteList){
		int dataLen = 4;  //response number
		for(byte[] requestByte: requestByteList){
			dataLen += 4;  //request len
			dataLen += requestByte.length;
		}
		for(byte[] responseByte: responseByteList){
			dataLen += 4;  //request len
			dataLen += responseByte.length;
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
