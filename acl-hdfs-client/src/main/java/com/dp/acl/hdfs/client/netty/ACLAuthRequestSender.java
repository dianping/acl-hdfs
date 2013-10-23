package com.dp.acl.hdfs.client.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dp.acl.hdfs.core.MultiAuthRequest;

public class ACLAuthRequestSender extends Thread{

	private static final Log logger = LogFactory.getLog(ACLAuthRequestSender.class);

	private Channel ch;
	private BlockingQueue<MultiAuthRequest> requestQueue;
	private boolean open = true;

	public ACLAuthRequestSender(Channel ch, BlockingQueue<MultiAuthRequest> requestQueue){
		this.ch = ch;
		this.requestQueue = requestQueue;
	}

	public void run() {
		ChannelFuture lastWriteFuture = null;
		while(open){
			try{
				MultiAuthRequest request = requestQueue.poll();
				if(request != null){
					lastWriteFuture = ch.writeAndFlush(request);
				}
				else{
					//if requestQueue is empty, sleep 100ms
					Thread.sleep(100);
				}
			} catch(Exception e){
				logger.warn("Exception occurs in sending request", e);
				continue;
			}
		}

		if(lastWriteFuture != null)
			try {
				lastWriteFuture.sync();
			} catch (InterruptedException e) {
				logger.warn("error occurs in lastWriteFuture.sync()");
			}
	}

	public void close(){
		open = false;
	}

}
