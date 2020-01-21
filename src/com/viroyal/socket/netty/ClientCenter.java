package com.viroyal.socket.netty;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientCenter {

	public static final int port = 9100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Scanner scanner = new Scanner(System.in);
//
//		System.out.println("请输入需要启动的客户端数量:");
//		int a = scanner.nextInt();
//		for (int j = 0; j < 3; j++) {
//			Client mClient = new Client(port);
//			mClient.start();
//            try {
//            Thread.sleep(5 * 1000);
//        } catch (InterruptedException e) {
//            return;
//        }
//			
//		}
	       /*
	        * 创建一个线程的实例
	        * 核心线程大小5
	        * 最大线程大小10
	        * 线程没有处理任务的时候存活的时间
	        * 创建一个数组阻塞队列（队列的长度为5）
	        * */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(30,30,8000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        for(int i=0;i<30;i++){
            //两种方法调用将任务加入到阻塞队列中submit(Runnable) 和 excute(Runnable) 方法
             executor.submit(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int j = 0; j < 6; j++) {
						Client mClient = new Client(port);
						mClient.start();
			            try {
			            Thread.sleep(5 * 1000);
			        } catch (InterruptedException e) {
			            return;
			        }
						
					}
				}
			});  //线程执行完毕返回一个future对象
        }
	}

}
