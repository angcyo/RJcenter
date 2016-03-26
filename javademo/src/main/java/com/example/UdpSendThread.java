package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by robi on 2016-03-21 14:53.
 */
public class UdpSendThread extends Thread {

    private static Object lock = new Object();
    private static UdpSendThread sendThread;

    private volatile boolean isExit = false;
    private Vector<int[]> sendDataInt;
    private Vector<byte[]> sendDataByte;
    private DatagramSocket socket;

    private int port = 8089;
    private String ip = "192.168.124.78";

    private UdpSendThread() {
        sendDataInt = new Vector<>();
        sendDataByte = new Vector<>();
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static UdpSendThread getInstance() {
        if (sendThread == null) {
            synchronized (lock) {
                if (sendThread == null) {
                    sendThread = new UdpSendThread();
                    sendThread.start();
                }
            }
        }

        return sendThread;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static byte[] intsToBytes(int[] values) {
        byte[] src = new byte[4 * values.length];
        for (int i = 0; i < values.length; i++) {
            byte[] bytes = intToBytes(values[i]);

            for (int j = 0; j < bytes.length; j++) {
                src[i * 4 + j] = bytes[j];
            }
//            System.arraycopy(bytes, 0, src, i * 4, bytes.length);
        }

        return src;
    }


    public void send(int[] data) {
        try {
            byte[] bytes = intsToBytes(data);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            packet.setPort(port);
            packet.setAddress(InetAddress.getByName(ip));
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        sendDataByte.add(data);
    }

    public void exit() {
        isExit = true;
    }

    @Override
    public void run() {
        while (!isExit) {
//            if (!sendDataInt.isEmpty()) {
//                int[] data = sendDataInt.remove(0);
//
//            }
            if (!sendDataByte.isEmpty()) {
                try {
                    byte[] data = sendDataByte.remove(0);
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    packet.setPort(port);
                    packet.setAddress(InetAddress.getByName(ip));
                    socket.send(packet);
                    System.out.println(getId() + " 发送数据长度:" + data.length + "字节");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
