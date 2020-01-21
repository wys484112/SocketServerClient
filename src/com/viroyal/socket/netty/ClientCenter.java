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
//		System.out.println("��������Ҫ�����Ŀͻ�������:");
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
	        * ����һ���̵߳�ʵ��
	        * �����̴߳�С5
	        * ����̴߳�С10
	        * �߳�û�д��������ʱ�����ʱ��
	        * ����һ�������������У����еĳ���Ϊ5��
	        * */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(30,30,8000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        for(int i=0;i<30;i++){
            //���ַ������ý�������뵽����������submit(Runnable) �� excute(Runnable) ����
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
			});  //�߳�ִ����Ϸ���һ��future����
        }
	}

}
