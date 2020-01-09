package com.viroyal.socket.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
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
        final ClientHandler clientHandler = new ClientHandler();

        int readIdleTime = 140;
        try {
            Bootstrap b = new Bootstrap();           
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
//							ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(readIdleTime, 0, 0));

							// Decoders
//							ch.pipeline().addLast("frameDecoder",
//									new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
							ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
							// Encoder
//							ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
							ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
							ch.pipeline().addLast("decoder", clientHandler);
						}
					});
            //连接服务端
            ChannelFuture connect = b.connect(new InetSocketAddress("localhost", 9100)).sync();
            Channel channel = connect.channel();

            System.out.println("client start");
//            ByteBuf buf = channel.alloc().buffer();
//            buf.writeByte(0xAA);
//          channel.writeAndFlush(buf);

            //获取客户端屏幕的写入

          
			Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.println("请输入");

				ByteBuf buf = channel.alloc().buffer();
				Charset charset = Charset.forName("UTF-8");
				buf.writeCharSequence(scanner.next(), charset);

				channel.writeAndFlush(buf);
			}
            
            
            
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }
	}

}
