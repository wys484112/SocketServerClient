package com.viroyal.socket.test;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class Client {  
	static int portStart=50000;                    	
    /** 
     * ��� 
     * @param args 
     */  
    public static void main(String[] args) {  
        // ���������ͻ��ˣ�һ���̴߳���һ���ͻ���  
        for (int i = 0; i < 3; i++) {  
            new Thread(new Runnable() {  
                @Override  
                public void run() {  
                    try {  
                        TestClient client = TestClientFactory.createClient(8899);  
                        client.send(String.format("Hello,Server!I'm %d.����ĩ������Ρ�", client.client.getLocalPort()));  
                        client.receive();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            }).start();  
        }  
    }  
  
    /** 
     * �������Կͻ��˵Ĺ��� 
     */  
    static class TestClientFactory {  
  
        public static TestClient createClient(int port) throws Exception {  
            return new TestClient("localhost", port);  
        }  
  
    }  
  
    /** 
     * ���Կͻ��� 
     */  
    static class TestClient {  
  
        /** 
         * ���캯�� 
         * @param host Ҫ���ӵķ����IP��ַ 
         * @param port Ҫ���ӵķ���˶�Ӧ�ļ����˿� 
         * @throws Exception 
         */  
        public TestClient(String host, int port) throws Exception {  
            // �����˽�������  
            this.client = new Socket(host, port);  
            System.out.println("Cliect[port:" + client.getLocalPort() + "] �����˽�������...");  
        }  
  
        private Socket client;  
  
        private Writer writer;  
  
        /** 
         * ������Ϣ 
         * @param msg 
         * @throws Exception 
         */  
        public void send(String msg) throws Exception {  
            // �������Ӻ�Ϳ����������д������  
            if(writer == null) {  
                writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");  
            }  
            writer.write(msg);  
            writer.write("eof\n");  
            writer.flush();// д���Ҫ�ǵ�flush  
            System.out.println("Cliect[port:" + client.getLocalPort() + "] ��Ϣ���ͳɹ�");  
        }  
  
        /** 
         * ������Ϣ 
         * @throws Exception 
         */  
        public void receive() throws Exception {  
            // д���Ժ���ж�����  
            Reader reader = new InputStreamReader(client.getInputStream(), "UTF-8");  
            // ���ý������ݳ�ʱ��Ϊ10��  
            client.setSoTimeout(10*1000);  
            char[] chars = new char[64];  
            int len;  
            StringBuilder sb = new StringBuilder();  
            while ((len = reader.read(chars)) != -1) {  
                sb.append(new String(chars, 0, len));  
            }  
            System.out.println("Cliect[port:" + client.getLocalPort() + "] ��Ϣ�յ��ˣ�����:" + sb.toString());  
            reader.close();  
  
            // �ر�����  
            writer.close();  
            client.close();  
        }  
  
    }  
  
}  
