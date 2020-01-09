package com.viroyal.socket.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * Socket�����<br> 
 * ����˵���� 
 *  
 * @author �������޵�С�� 
 * @Date 2016��8��30�� 
 * @version 1.0 
 */  
public class Server {  
  
    /** 
     * ��� 
     *  
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
        // Ϊ�˼���������е��쳣��Ϣ��������  
        int port = 8899;  
        // ����һ��ServiceSocket�����ڶ˿�8899��  
        ServerSocket server = new ServerSocket(port);  
        System.out.println("�ȴ���ͻ��˽�������...");  
        while (true) {  
            // server���Խ�������Socket����������server��accept����������ʽ��  
            Socket socket = server.accept();  
            /** 
             * ���ǵķ���˴���ͻ��˵�����������ͬ�����еģ� ÿ�ν��յ����Կͻ��˵���������� 
             * ��Ҫ�ȸ���ǰ�Ŀͻ���ͨ����֮������ٴ�����һ���������� ���ڲ����Ƚ϶������»�����Ӱ���������ܣ� 
             * Ϊ�ˣ����ǿ��԰�����Ϊ���������첽������ͻ���ͨ�ŵķ�ʽ 
             */  
            // ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������  
            new Thread(new Task(socket)).start();  
  
        }  
        // server.close();  
    }  
  
    /** 
     * ����Socket������߳��� 
     */  
    static class Task implements Runnable {  
  
        private Socket socket;  
  
        /** 
         * ���캯�� 
         */  
        public Task(Socket socket) {  
            this.socket = socket;  
        }  
  
        @Override  
        public void run() {  
            try {  
                handlerSocket();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
  
        /** 
         * ���ͻ���Socket����ͨ�� 
         *  
         * @throws IOException 
         */  
        private void handlerSocket() throws Exception {  
            // ���ͻ��˽���������֮�����ǾͿ��Ի�ȡsocket��InputStream�������ж�ȡ�ͻ��˷���������Ϣ��  
            /** 
             * �ڴ�Socket��InputStream�н�������ʱ������������һ���Ķ���̫�����ˣ� 
             * ��ʱ�����Ǿͻỻ��ʹ��BufferedReader��һ�ζ�һ�� 
             *  
             * BufferedReader��readLine������һ�ζ�һ�еģ���������������ģ�ֱ����������һ������Ϊֹ����Ż��������ִ�У� 
             * ��ôreadLineʲôʱ��Ż����һ���أ�ֱ�����������˻��з������Ƕ�Ӧ���Ľ�����readLine�����Ż���Ϊ������һ�У� 
             * �Ż�������������ó����������ִ�С� 
             * ����������ʹ��BufferedReader��readLine��ȡ���ݵ�ʱ��һ��Ҫ�ǵ��ڶ�Ӧ�����������һ��Ҫд�뻻�з��� 
             * ������֮����Զ����Ϊ������readLine����ʶ�𣩣�д�뻻�з�֮��һ���ǵ����������������Ϲرյ�����¼ǵ�flushһ�£� 
             * �������ݲŻ������Ĵӻ���������д�롣 
             */  
            BufferedReader br = new BufferedReader(  
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));  
            StringBuilder sb = new StringBuilder();  
            String temp;  
            int index;  
            while ((temp = br.readLine()) != null) {  
                if ((index = temp.indexOf("eof")) != -1) { // ����eofʱ�ͽ�������  
                    sb.append(temp.substring(0, index));  
                    break;  
                }  
                sb.append(temp);  
            }  
            System.out.println("Form Cliect[port:" + socket.getPort()  
                    + "] ��Ϣ����:" + sb.toString());  
  
            // ��Ӧһ�¿ͻ���  
            Writer writer = new OutputStreamWriter(socket.getOutputStream(),  
                    "UTF-8");  
            writer.write(String.format("Hi,%d.�������壬�ݷ�ͳ���", socket.getPort()));  
            writer.flush();  
            writer.close();  
            System.out.println(  
                    "To Cliect[port:" + socket.getPort() + "] �ظ��ͻ��˵���Ϣ���ͳɹ�");  
  
            br.close();  
            socket.close();  
        }  
  
    }  
  
}  
