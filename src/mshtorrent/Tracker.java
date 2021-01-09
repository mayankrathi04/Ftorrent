/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import org.apache.commons.io.IOUtils;
/**
 *
 * @author root
 */
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.security.cert.Certificate;
import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

public class Tracker {

    public boolean timeout;
    public short countRetransmission;
    public int timeoutPeriod;
    public static final int DEFAULT_TIMEOUT = 7000;//in milli seconds
    public static final short MAX_RETRAMISSIONS = 1;
    public static final int ACTION_CONNECT = 0;
    public static final int ACTION_ANNOUNCE = 1;
    public static final int ACTION_SCRAPE = 2;
    public static final int ACTION_ERROR = 3;
    public static final int ACTION_TRANSACTION_ID_ERROR = 256;
    public static final long NO_CONNECTION_ID = 4497486125440L;
    String url;
    String requestUrl;
    TrackerResponseParameter responseParameter;
    TrackerRequestParameter requestParameter;
    int transactionId;
    long connectionId;
    boolean connectionStatus;

    Tracker(String u) {
        this.url = u;
        this.requestUrl = "";
        int random = (new Random()).nextInt();
        if (random < 0) {
            random = -random;
        }
        this.transactionId = random;
        this.connectionId = Tracker.NO_CONNECTION_ID;
        this.connectionStatus = false;
        this.responseParameter = new TrackerResponseParameter();
        this.countRetransmission = -1;
        this.timeout = false;
        this.timeoutPeriod = Tracker.DEFAULT_TIMEOUT;
    }

    void createRequest(byte[] info, long up, long down, long lef, int peers, Event eve, String peerId) {
        this.requestParameter = new TrackerRequestParameter(info, up, down, lef, peers, eve, peerId);
        requestUrl = "" + this.url + "?info_hash=" + new CheckUtilities().byteArrayToURLString(requestParameter.info_hash) + "&peer_id=" + requestParameter.peerId + "&port=" + requestParameter.port + "&uploaded=" + requestParameter.uploaded + "&downloaded=" + requestParameter.downloaded + "&left=" + requestParameter.left + "&event=" + requestParameter.event + "&compact=" + requestParameter.compact;
    }

    void requestTracker() {
        try {
            // System.out.println("Connecting to url - " + this.url);

            if ((this.url).contains("https://")) {
                URL track = new URL(this.requestUrl);
                System.out.println("https request");
                HttpsURLConnection con = (HttpsURLConnection) track.openConnection();
                this.print_https_cert(con);
                if (con != null) {
                    this.handleResponse(con);
                }
            } else if ((this.url).contains("http://")) {
                URL track = new URL(this.requestUrl);
                System.out.println("http request");
                HttpURLConnection con = (HttpURLConnection) track.openConnection();
                System.setProperty("http.agent", "");
                con.setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                if (con != null) {
                    this.handleResponse(con);
                }
            } else if ((this.url).contains("udp://")) {
                try {
                    this.handleUdpRequest();
                } catch (Exception e) {
                    System.out.println("In udp error ");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

            System.err.println("Error here:" + e.getMessage());
        }
    }

    void handleResponse(HttpsURLConnection track) {
        try {
            InputStream resp = track.getInputStream();
            byte[] response = IOUtils.toByteArray(resp);
            System.out.println();
            System.out.println(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    void handleResponse(HttpURLConnection track) {
        try {
            InputStream resp = track.getInputStream();
            byte[] response = IOUtils.toByteArray(resp);
            System.out.println();
            System.out.println(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getUdpPort() {
        String[] urlData = this.url.split(":");
        int port;
        port = Integer.parseInt(urlData[2].split("/")[0]);
        return (port);
    }

    public String getUdpAddress() {
        String[] urlData = this.url.split(":");
        return urlData[1].substring(2);
    }

    public void handleUdpRequest() {
        try {
            System.out.println("Connecting tracker..");
            do {
                connectUdpTracker();
                this.countRetransmission++;
                this.updateTimeoutPeriod();
            } while (timeout && this.countRetransmission != Tracker.MAX_RETRAMISSIONS);
            this.resetTimeout();
            try {
                if (this.connectionStatus) {
                    this.transactionId = this.getRandomInteger();
                    do {
                        System.out.println("Sending scrape request to tracker");
                        this.sendScrapeRequestUdpTracker();
                        this.countRetransmission++;
                        this.updateTimeoutPeriod();
                    } while (timeout && this.countRetransmission != Tracker.MAX_RETRAMISSIONS);
                    this.resetTimeout();
                    this.transactionId = this.getRandomInteger();
                    System.out.println("Sending announce request to tracker..");
                    do {
                        this.sendAnnounceRequestUdpTracker();
                        this.countRetransmission++;
                        this.updateTimeoutPeriod();
                    } while (timeout && this.countRetransmission != Tracker.MAX_RETRAMISSIONS);
                    this.resetTimeout();

                } else {
                    System.out.println("Tracker not working");
                }
            } catch (Exception e) {
                System.out.println("Error sending/recieving details");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error connecting tracker");
            e.printStackTrace();
        }
        timeout = false;
    }

    public int getRandomInteger() {
        int random = (new Random()).nextInt();
        if (random < 0) {
            random = -random;
        }
        return random;
    }

    public void sendScrapeRequestUdpTracker() throws Exception {
        byte[] pack = new byte[36];
        byte[] resp = new byte[20];
        CheckUtilities cu = new CheckUtilities();
        cu.putLong(pack, 0, this.connectionId);
        cu.putInt(pack, 8, Tracker.ACTION_SCRAPE);
        cu.putInt(pack, 12, this.transactionId);
        cu.copyBytes(this.requestParameter.info_hash, pack, 0, 16, 20);
        resp = this.sendUdpPacket(pack, 20, 10000);
        if (this.timeout) {
            return;
        }
        if (cu.readInt(resp, 0) == Tracker.ACTION_SCRAPE) {
            if (cu.readInt(resp, 4) == this.transactionId) {
                System.out.println("Scrape request completed");
                this.responseParameter.seeders = cu.readInt(resp, 8);
                this.responseParameter.complete = cu.readInt(resp, 12);
                this.responseParameter.leechers = cu.readInt(resp, 16);
            }
        }
    }

    public void sendAnnounceRequestUdpTracker() throws Exception {
        byte[] pack = new byte[98];
        byte[] resp = new byte[(20 + (6 * this.responseParameter.seeders))];
        CheckUtilities cu = new CheckUtilities();
        cu.putLong(pack, 0, this.connectionId);
        cu.putInt(pack, 8, Tracker.ACTION_ANNOUNCE);
        cu.putInt(pack, 12, this.transactionId);
        cu.copyBytes(this.requestParameter.info_hash, pack, 0, 16, 20);
        cu.putString(pack, 36, this.requestParameter.peerId, 20);
        cu.putLong(pack, 56, this.requestParameter.downloaded);
        cu.putLong(pack, 64, this.requestParameter.left);
        cu.putLong(pack, 72, this.requestParameter.uploaded);
        cu.putInt(pack, 80, 2);
        cu.copyBytes((new CheckUtilities()).convertIpFromStringToByteArray(this.requestParameter.ip), pack, 0, 84, 4);
        cu.putInt(pack, 88, this.requestParameter.key);
        cu.putInt(pack, 92, this.requestParameter.numwant);
        cu.copyBytes((new CheckUtilities()).convertPortFromIntToByteArray(this.requestParameter.port), pack, 0, 96, 2);
        resp = this.sendUdpPacket(pack, (20 + (6 * this.requestParameter.numwant)), 10000);
        if (this.timeout) {
            return;
        }
        //   System.out.println("2nd Reply from tracker-" + (new CheckUtilities().byteArrayToByteString(resp)));
        if (cu.readInt(resp, 0) == Tracker.ACTION_ANNOUNCE) {
            if (cu.readInt(resp, 4) == this.transactionId) {
                System.out.println("Announce complete");
                this.responseParameter.interval = cu.readInt(resp, 8);
                for (int i = 0; i < this.responseParameter.seeders; i++) {
                    byte[] ipByte = cu.getByte(resp, (20 + (i * 6)), ((20 + (i * 6)) + 3));
                    byte[] portByte = cu.getByte(resp, ((20 + (i * 6)) + 4), ((20 + (i * 6)) + 5));
                    if (!cu.checkNullArray(ipByte) && !cu.checkNullArray(portByte)) {
                        String ip = cu.convertIpFromByteArrayToString(ipByte);
                        int port = cu.convertPortFromByteArrayToInt(portByte);
                        System.out.println("IP:" + ip + "Port:" + port);
                        this.responseParameter.peersArray.add(new PeerInfo(ip, port));
                    } else {
                        break;
                    }
                }
                System.out.println("Announce setup complete");
                // this.responseParameter.printResponse();
            }
        } else {
            System.out.println("Respnse code " + cu.readInt(resp, 0));
        }
    }

    public void connectUdpTracker() throws Exception {
        byte[] request = new byte[16];

        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(0, Tracker.NO_CONNECTION_ID);
        bb.putInt(8, Tracker.ACTION_CONNECT);
        bb.putInt(12, this.transactionId);
        request = bb.array();

        byte[] buffer = new byte[16];
        buffer = this.sendUdpPacket(request, 16, this.timeoutPeriod);
        if (this.timeout) {
            return;
        }
        if (((new CheckUtilities()).readInt(buffer, 0)) == Tracker.ACTION_CONNECT) {
            if ((new CheckUtilities()).readInt(buffer, 4) == this.transactionId) {
                this.connectionId = (new CheckUtilities()).readLong(buffer, 8);
                this.connectionStatus = true;
                System.out.println("Connection Successful");
            }
        }
    }

    public byte[] sendUdpPacket(byte[] data, int len, int timeout) throws Exception {
        byte[] buffer = new byte[len];
        DatagramSocket ds = new DatagramSocket(6883);
        try {
            //ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(this.getUdpAddress());
            System.out.println("ip-" + ip + " port-" + this.getUdpPort());
            DatagramPacket dp = new DatagramPacket(data, data.length, ip, this.getUdpPort());
            ds.send(dp);
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            ds.setSoTimeout(timeout);
            ds.receive(reply);
            ds.close();
        } catch (SocketTimeoutException e) {
            // e.printStackTrace();
            System.out.println("Timeout..retransmitting");
            this.timeout = true;
            ds.close();
        } catch (UnknownHostException e) {
            System.out.println("problem in resolving");
            System.err.println(e.getMessage());
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
            ds.close();
        }
        return buffer;
    }

    public void print_https_cert(HttpsURLConnection con) {

        if (con != null) {

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for (Certificate cert : certs) {
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void resetTimeout() {
        this.timeout = false;
        this.countRetransmission = -1;
        this.timeoutPeriod = Tracker.DEFAULT_TIMEOUT;
    }

    public void updateTimeoutPeriod() {
        this.timeoutPeriod = this.timeoutPeriod * ((int) (Math.pow(2, this.countRetransmission)));
    }
}
