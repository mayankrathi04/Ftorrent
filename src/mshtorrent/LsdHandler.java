/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author root
 */
public class LsdHandler extends Thread {

    Thread send;
    Thread rec;
    byte[] request;
    volatile boolean isExitLsd;

    LsdHandler(byte[] infoHash) {
        String rp1 = "BT-SEARCH * HTTP/1.1\r\nHost: 239.192.152.143\r\nPort: 6771\r\nInfohash: ";
        String rp2 = "\r\ncookie: 61535b49\r\n" + "\r\n" + "\r\n";
        request = new byte[rp1.length() + rp2.length() + 20];
        CheckUtilities cu = new CheckUtilities();
        cu.copyBytes(rp1.getBytes(), request, 0, 0, rp1.length());
        cu.copyBytes(infoHash, request, 0, rp1.length(), 20);
        cu.copyBytes(rp2.getBytes(), request, 0, (rp1.length() + 20), rp2.length());
        this.isExitLsd = false;
    }

    void sendLsd(String ip, int port) {
        try {
            DatagramSocket ds = new DatagramSocket(45000);
            byte[] data = this.request;
            InetAddress in = InetAddress.getByAddress((new CheckUtilities()).convertIpFromStringToByteArray(ip));
            DatagramPacket dp = new DatagramPacket(data, data.length, in, port);
            ds.send(dp);
            ds.close();
        } catch (SocketException e) {
            System.err.println("Socket Exception");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host Exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception");
            e.printStackTrace();
        }

    }

    void recieveLsd() throws SocketException {
        DatagramSocket ds = new DatagramSocket(6771);
        while (true) {
            try {
                byte[] recieve = new byte[1400];
                DatagramPacket p = new DatagramPacket(recieve, recieve.length);
                ds.setSoTimeout(3000);
                ds.receive(p);
                ds.close();
                System.out.println(new String(recieve, "UTF-8"));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host Exception");
                e.printStackTrace();
            } catch (IOException e) {
                // System.err.println("IO Exception");
                // e.printStackTrace();
            }
            if (this.isExitLsd) {
                ds.close();
                return;
            }
        }
    }

    public void sendFunc() {
        while (true) {
            this.sendLsd("239.192.152.143", 6771);
            try {
                TimeUnit.MINUTES.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            Thread x = Thread.currentThread();
            if ((x.getName()).equals("reclsd")) {
                this.recieveLsd();
            } else if ((x.getName()).equals("sendlsd")) {
                this.sendFunc();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void exec() {
        send = new Thread(this, "sendlsd");
        rec = new Thread(this, "reclsd");
        send.start();
        rec.start();
    }

    public void exit() {
        this.isExitLsd = true;
    }
}
