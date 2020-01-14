package com.viroyal.socket.netty;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.viroyal.socket.util.RandomUtil;
import com.viroyal.socket.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private boolean isRegistered = false;
	private boolean isActive = false;
	private Client mClient;

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

		ctx.writeAndFlush("aaa");
		isActive = true;

		// Scanner scanner = new Scanner(System.in);
		// while(true){
		// System.out.println("������");
		// ctx.channel().writeAndFlush(scanner.next());
		// }

		
		//��ʱ������Ϣ��������
		String imei=RandomUtil.RandomCode(10);
		 final EventLoop eventLoop = ctx.channel().eventLoop();
		 eventLoop.scheduleAtFixedRate(new Runnable() {
		 @Override
		 public void run() {
				ByteBuf buf = ctx.alloc().buffer();
				Charset charset = Charset.forName("UTF-8");
				buf.writeCharSequence("6F01000700000F3836373732353033303039353537380d0a0d0a", charset);
				ctx.channel().writeAndFlush(buf);
		 }
		 }, 1L,5L, TimeUnit.SECONDS);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		System.out.println("client channelInactive");
		isActive = false;

		System.err.println("������...");
		// ʹ�ù����ж������� ������������ã�close�� ctx�������Ѿ���ʧ���޷���ɶ�ʱ����
		// final EventLoop eventLoop = ctx.channel().eventLoop();
		// eventLoop.schedule(new Runnable() {
		// @Override
		// public void run() {
		// mClient.shutdown();
		// System.err.println("��mClient==null��...");
		// mClient = new Client(9100);
		// mClient.start();
		// }
		// }, 0L, TimeUnit.SECONDS);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mClient.shutdown();
				if (mClient == null) {
					System.err.println("��mClient==null��...");
					mClient = new Client(ClientCenter.port);
				}

				mClient.start();
			}
		}).start();

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
				System.out.println("����û�յ���������������");
				// ����ѡ����������
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				System.out.println("����δ���������������");
				// ����������
				ByteBuf buf = ctx.alloc().buffer();
				Charset charset = Charset.forName("UTF-8");
				buf.writeCharSequence("3131", charset);
				ctx.writeAndFlush(buf);
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
		System.err.println("�쳣��...");
		// �쳣��������
		// final EventLoop eventLoop = ctx.channel().eventLoop();
		// eventLoop.schedule(new Runnable() {
		// @Override
		// public void run() {
		// mClient.start();
		// }
		// }, 1L, TimeUnit.SECONDS);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mClient.shutdown();
				if (mClient == null) {
					System.err.println("��mClient==null��...");
					mClient = new Client(ClientCenter.port);
				}

				mClient.start();
			}
		}).start();
	}

}
