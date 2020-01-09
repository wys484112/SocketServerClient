package com.viroyal.socket.netty;

import java.net.InetSocketAddress;
import java.util.Scanner;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        EventLoopGroup group = new NioEventLoopGroup();
        ByteArrayEncoder mByteEncoder = new ByteArrayEncoder();
        int readIdleTime = 140;
        try {
            Bootstrap b = new Bootstrap();           
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(readIdleTime, 0, 0));

							// Decoders
//							ch.pipeline().addLast("frameDecoder",
//									new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
							ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
							// Encoder
//							ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
							ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
							ch.pipeline().addLast("decoder", new ClientHandler());
						}
					});
            //连接服务端
            ChannelFuture connect = b.connect(new InetSocketAddress("localhost", 9701)).sync();
            Channel channel = connect.channel();

            System.out.println("client start");
            //获取客户端屏幕的写入
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入");
                channel.write(scanner.next());
                channel.flush();
            }
            
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } finally {  
            group.shutdownGracefully();  
        }  
	}

}
