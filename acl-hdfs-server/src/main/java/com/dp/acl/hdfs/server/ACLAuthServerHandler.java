package com.dp.acl.hdfs.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dp.acl.hdfs.core.MultiAuthRequest;
import com.dp.acl.hdfs.core.MultiAuthResponse;

public class ACLAuthServerHandler extends SimpleChannelInboundHandler<MultiAuthRequest>{
	
	private static final Log logger = LogFactory.getLog(ACLAuthServerHandler.class);
	
	private IACLAuthService service;
	
	public ACLAuthServerHandler(IACLAuthService service){
		this.service = service;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MultiAuthRequest msg)
			throws Exception {
		MultiAuthResponse resp = service.process(msg);
		if(resp != null)
			ctx.write(resp);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downstream", cause);
        ctx.close();
    }

}
