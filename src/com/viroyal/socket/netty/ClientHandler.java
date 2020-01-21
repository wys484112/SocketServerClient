package com.viroyal.socket.netty;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.viroyal.socket.util.RandomUtil;
import com.viroyal.socket.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private boolean isRegistered = false;
	private boolean isActive = false;
	private Client mClient;
	String imeiHexStr=TextUtils.byte2HexStr(RandomUtil.RandomCode(15).getBytes());

	public ClientHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientHandler(Client mClient) {
		super();
		this.mClient = mClient;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("channelRead msg==" + TextUtils.byte2Str((byte[]) msg));
		String  msgHex=TextUtils.byte2Str((byte[]) msg);
		if(msgHex.substring(6, 8).equals("11")) {  //灯开关
			
			ByteBuf buf = ctx.alloc().buffer();
			Charset charset = Charset.forName("UTF-8");
			buf.writeCharSequence("6F010121000012"+imeiHexStr+"0101000D0A0D0A", charset);
			ctx.channel().writeAndFlush(buf).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// TODO Auto-generated method stub
					if(future.isSuccess()) {
						System.out.println("sendMsg 发送成功   channel:"+ctx.channel().localAddress() +"  textHexStr:"+"6F0101210000123836373732353033303039353537380101000D0A0D0A");        					
					}else {
						System.out.println("sendMsg 发送失败   channel:"+ctx.channel().localAddress() +"  textHexStr:"+"6F0101210000123836373732353033303039353537380101000D0A0D0A");        
						
					}

				}
			});	
		}

	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
		System.out.println("client channelRegistered");
		isRegistered = true;

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
		System.out.println("client channelUnregistered");
		isRegistered = false;

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		// super.channelActive(ctx);
		System.out.println("client channelActive");
		System.out.println("client xv");

//		ctx.writeAndFlush("aaa");
		isActive = true;

		// Scanner scanner = new Scanner(System.in);
		// while(true){
		// System.out.println("请输入");
		// ctx.channel().writeAndFlush(scanner.next());
		// }

		
		//定时发送信息给服务器
		System.out.println("client imeiHexStr=="+imeiHexStr);

//		ByteBuf buf = ctx.alloc().buffer();
//		Charset charset = Charset.forName("UTF-8");
////		buf.writeCharSequence("6F01000101002F"+"383637373235303330303935353738"+"006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A", charset);
//		buf.writeCharSequence("6F01000101002F"+imeiHexStr+"006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A", charset);		
//		ctx.channel().writeAndFlush(buf);
		
		 final EventLoop eventLoop = ctx.channel().eventLoop();
		 eventLoop.scheduleAtFixedRate(new Runnable() {
		 @Override
		 public void run() {
				System.out.println("client channel"+ctx.channel().localAddress());

				ByteBuf buf = ctx.alloc().buffer();
				Charset charset = Charset.forName("UTF-8");
//				buf.writeCharSequence("6F01000101002F"+"383637373235303330303935353738"+"006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A", charset);
				buf.writeCharSequence("6F01000101002F"+imeiHexStr+"006403E80003E80003E8503203E83804040500010064010304B004B004B004B00D0A0D0A", charset);
				
				ctx.channel().writeAndFlush(buf);
		 }
		 }, 1L,120L, TimeUnit.SECONDS);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		System.out.println("client channelInactive");
		isActive = false;

		System.err.println("掉线了...");
		// 使用过程中断线重连 这个方法不可用，close后 ctx上下文已经消失，无法完成定时任务
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				mClient.shutdown();
//				if (mClient == null) {
//					System.err.println("掉mClient==null了...");
//					mClient = new Client(ClientCenter.port);
//				}
//
//				mClient.start();
//			}
//		}).start();

		ctx.close();

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
		System.out.println("client userEventTriggered");
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				System.out.println("长期没收到服务器推送数据");
				// 可以选择重新连接
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				System.out.println("长期未向服务器发送数据");
				// 发送心跳包
//				ByteBuf buf = ctx.alloc().buffer();
//				Charset charset = Charset.forName("UTF-8");
//				buf.writeCharSequence("3131", charset);
//				ctx.writeAndFlush(buf);
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				System.out.println("ALL");
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		System.out.println("client exceptionCaught");
		System.err.println("异常了...");
		// 异常断线重连
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				mClient.shutdown();
//				if (mClient == null) {
//					System.err.println("掉mClient==null了...");
//					mClient = new Client(ClientCenter.port);
//				}
//
//				mClient.start();
//			}
//		}).start();
	}

}
