package com.viroyal.socket.netty;

import java.nio.charset.Charset;
import java.util.Scanner;

import com.viroyal.socket.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
        System.out.println("channelRead msg=="+TextUtils.byte2HexStr((byte[]) msg));                
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
        System.out.println("client channelRegistered");

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
        System.out.println("client channelUnregistered");

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
//		super.channelActive(ctx);
        System.out.println("client channelActive");
        System.out.println("client xv");

        ctx.writeAndFlush("aaa");

//        Scanner scanner = new Scanner(System.in);
//        while(true){
//            System.out.println("«Î ‰»Î");
//            ctx.channel().writeAndFlush(scanner.next());
//        }
        

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
        System.out.println("client channelInactive");

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
        System.out.println("client userEventTriggered");

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
        System.out.println("client exceptionCaught");

	}



}
