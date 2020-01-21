package com.viroyal.socket.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Port;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {
	public static int readIdleTime = 60;
	public static int writeIdleTime = 60;
	public static int allIdleTime = 60 * 10;

	private int Port;
	private Channel channel;
	private EventLoopGroup group;

	public Client(int port) {
		super();
		Port = port;
	}

	public void start() {
		shutdown();
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast("idleStateHandler",
									new IdleStateHandler(readIdleTime, writeIdleTime, allIdleTime));

							// Decoders
							ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
							// Encoder
							ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
							ch.pipeline().addLast("decoder", new ClientHandler(Client.this));
						}
					});
			// 连接服务端
			connect(b, Port);

			System.out.println("client start");

		} catch (InterruptedException e) {
			System.out.println("client InterruptedException");

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("client Exception");
			try {
				Thread.sleep(2000);
				start();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
		}
	}

	public void shutdown() {
		if (group != null) {
			try {
				group.shutdownGracefully().sync();
				System.out.println("shutdown");
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("shutdown Exception");
			}
		}
	}

	public void connect(Bootstrap bootstrap, int port) throws Exception {
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress("localhost", port)).sync();
		System.out.println("operationComplete connect");

		connect.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("operationComplete");

				// TODO Auto-generated method stub
				if (!future.isSuccess()) {
					System.out.println("operationComplete Exception1");

					final EventLoop loop = future.channel().eventLoop();
					loop.schedule(new Runnable() {
						@Override
						public void run() {
							System.err.println("服务端链接不上，开始重连操作...");
							try {
								connect(bootstrap, port);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, 1L, TimeUnit.SECONDS);
				} else {
					System.err.println("服务端链接成功...");

				}

			}
		});

		channel = connect.channel();
//		screenSengMessage();
	}

	public void sendMsg(String text) throws Exception {
		// Thread.sleep(2 * 1000);
		ByteBuf buf = channel.alloc().buffer();
		Charset charset = Charset.forName("UTF-8");
		buf.writeCharSequence(text, charset);
		channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("发送成功");

			}
		});
	}

	public void screenSengMessage() {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("请输入");
			try {
				sendMsg(scanner.next());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Client mClient = new Client(9100);
		mClient.start();
	}

}
