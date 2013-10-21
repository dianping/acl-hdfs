package com.dp.acl.hdfs.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dp.acl.hdfs.core.AuthRequest;
import com.dp.acl.hdfs.core.AuthResponse;
import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ACLAuthClientHandler extends SimpleChannelInboundHandler<MultiAuthResponse>{

	private static final Log logger = LogFactory.getLog(ACLAuthClientHandler.class);
	
	private final Map<MultiAuthRequest, MultiAuthResponse> resultMap;
	
	public ACLAuthClientHandler(Map<MultiAuthRequest, MultiAuthResponse> resultMap){
		this.resultMap = resultMap;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MultiAuthResponse msg)
			throws Exception {
		MultiAuthRequest request = new MultiAuthRequest();
		for(Entry<AuthRequest, AuthResponse> entry : msg.getResponses().entrySet()){
			request.addRequest(entry.getKey());
		}
		resultMap.put(request, msg);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception when recieving auth response", cause);
        ctx.close();
    }
}
