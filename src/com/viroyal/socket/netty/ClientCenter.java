package com.viroyal.socket.netty;

import java.util.Scanner;

public class ClientCenter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner scanner = new Scanner(System.in);
		 while(true){
		 System.out.println("请输入需要启动的客户端数量:");
		   int a=scanner.nextInt();
		   for(int i=0 ;i<a;i++) {
				Client mClient=new Client(9100);
				mClient.start();
		   }
		 
		 }
	}

}
