package com.viroyal.socket.netty;

import java.util.Scanner;

public class ClientCenter {

	public static final int port = 9100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Scanner scanner = new Scanner(System.in);
//
//		System.out.println("请输入需要启动的客户端数量:");
//		int a = scanner.nextInt();
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Client mClient = new Client(port);
					mClient.start();
				}
			}).start();

		}

	}

}
