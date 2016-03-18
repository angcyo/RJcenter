package com.angcyo.sample.SocketDemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by angcyoon 2016-03-18 00:20.
 */
public class UdpSendThread extends Thread {

    private static UdpSendThread sendThread;
    private static Object lock = new Object();

    private Vector<byte[]> sendDatas;

    private boolean isExit = false;

    public static String serverIp = "192.168.1.12";
    public static int serverPort = 8989;
    private static final int DATA_LEN = 4096;

    DatagramSocket socket;

    private UdpSendThread() throws SocketException {
        sendDatas = new Vector<>();
        socket = new DatagramSocket();
    }

    private static UdpSendThread getInstance() throws SocketException {
        synchronized (lock) {
            if (sendThread == null) {
                sendThread = new UdpSendThread();
                sendThread.start();
            }
            return sendThread;
        }
    }

    private void addData(byte[] data) {
        sendDatas.add(data);
    }

    public static void sendData(byte[] data) throws SocketException {
        synchronized (lock) {
            getInstance().addData(data);
        }
    }

    @Override
    public void run() {
        while (!isExit) {
            if (!sendDatas.isEmpty()) {
                byte[] bytes = sendDatas.remove(0);
                try {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    packet.setAddress(InetAddress.getByName(serverIp));
                    packet.setPort(serverPort);

                    socket.send(packet);
                    String s = new String(bytes);
                    System.out.println("发送数据包:" + s + " 大小:" + packet.getLength() + " 字节" + " 长度:" + s.length());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
