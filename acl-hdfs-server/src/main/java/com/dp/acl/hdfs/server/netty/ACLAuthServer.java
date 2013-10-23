package com.dp.acl.hdfs.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.dp.acl.hdfs.core.netty.MultiAuthRequestDecoder;
import com.dp.acl.hdfs.core.netty.MultiAuthResponseEncoder;

public class ACLAuthServer {
	
	private final int port;
	
	private IACLAuthService service; 
	
	public ACLAuthServer(int port, IACLAuthService service){
		this.port = port;
		this.service = service;
	}
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .childHandler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new MultiAuthRequestDecoder());
					pipeline.addLast(new MultiAuthResponseEncoder());
					
					pipeline.addLast(new ACLAuthServerHandler(service));
				}
				 
			 });
			
			b.bind(port).sync().channel().closeFuture().sync();
		} finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
