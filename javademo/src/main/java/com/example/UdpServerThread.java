package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by angcyoon 2016-03-18 01:01.
 */
public class UdpServerThread extends Thread{

    private static Object lock = new Object();

    private boolean isExit = false;

    public static String serverIp = "192.168.1.12";
    public static int serverPort = 8989;
    private static final int DATA_LEN = 4096;
    byte[] data;

    DatagramSocket socket;

    private UdpServerThread() throws SocketException {
        socket = new DatagramSocket(serverPort);
    }

    public static void receive() throws SocketException {
        new UdpServerThread().start();
    }

    @Override
    public void run() {
        while (!isExit) {
            data = new byte[DATA_LEN];
            DatagramPacket packet = new DatagramPacket(data, DATA_LEN);
            try {

                System.out.println("等待中...");
                socket.receive(packet);
                String s = new String(data);
                System.out.println("收到数据包:" + s + " 大小:" + packet.getLength() + " 字节" + " 长度:" + s.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String... args) {
        try {
            receive();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
