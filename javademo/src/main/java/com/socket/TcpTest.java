package com.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by robi on 2016-05-12 09:59.
 */
public class TcpTest {
    public static PrintStream p = System.out;

    public static void main(String... args) {
        tcpTest();
    }

    public static void tcpTest() {
        new Tcp().start();
    }


    static class Tcp extends Thread {

        Socket mSocket;
        String ip = "192.168.0.0";
        int port = 8090;

        public Tcp() {
        }

        private void create() {
            mSocket = new Socket();
            try {
                mSocket.setKeepAlive(true);
                mSocket.setSoTimeout(3000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        private void close() {
            if (mSocket != null) {
                try {
                    mSocket.close();
                    mSocket = null;
                } catch (IOException e1) {
                    p.println("连接失败 e1:" + e1.getMessage());
                }
            }
        }

        public void connect() {
            create();
            try {
                p.println("连接中");
                mSocket.connect(new InetSocketAddress(ip, port), 5000);
                p.println("连接成功");
            } catch (IOException e) {
                p.println("连接失败:" + e.getMessage());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                close();
                connect();
            }
        }

        @Override
        public void run() {
            connect();
        }
    }
}

