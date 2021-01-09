/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

/**
 *
 * @author root
 */
import static java.util.Arrays.asList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Dht extends Thread implements Runnable {

    private static char dictionaryStart = 'd';
    private static char dictionaryEnd = 'e';
    private static char listStart = 'l';
    private static char listEnd = 'e';
    private static char numberStart = 'i';
    private static char numberEnd = 'e';
    private static char byteArrayDivider = ':';
    private static int i = 0;
    short queryType;
    byte[] data;
    byte[] infohash;
    Thread t;
    DhtNode dhtNodes;
    List<DhtNode> moreNode;
    List<PeerInfo> peerList;
    DhtRequestHandler requesthandler;
    List<DhtNode> freshList;
    volatile boolean isExit;

    public static List<DhtNode> getBootstrapNode() {
        return bootstrapNode;
    }
    private static List<DhtNode> bootstrapNode = asList((new DhtNode("dht.transmissionbt.com", 6881)), (new DhtNode("dht.libtorrent.org", 25401)), (new DhtNode("router.utorrent.com", 6881)), (new DhtNode("router.bittorrent.com", 6881))/*, (new DhtNode("dht.aelitis.com", 6881))*/);
    private Lock lock;

    Dht(String ip, int port) {
        this.dhtNodes = new DhtNode(ip, port);
        this.lock = new ReentrantLock();
        this.requesthandler = new DhtRequestHandler();
        this.moreNode = new ArrayList<DhtNode>();
        this.queryType = (short) 0;
        this.freshList = new ArrayList<DhtNode>();
        this.isExit = false;
        this.peerList = new ArrayList<PeerInfo>();
    }

    Dht(byte[] id) {
        this.dhtNodes = new DhtNode(id);
        this.lock = new ReentrantLock();
        this.requesthandler = new DhtRequestHandler();
        this.moreNode = new ArrayList<DhtNode>();
        this.queryType = (short) 0;
        this.freshList = new ArrayList<DhtNode>();
        this.isExit = false;
        this.peerList = new ArrayList<PeerInfo>();

    }

    Dht(String ip, int port, byte[] id) {
        this.dhtNodes = new DhtNode(ip, port, id);
        this.lock = new ReentrantLock();
        this.requesthandler = new DhtRequestHandler();
        this.moreNode = new ArrayList<DhtNode>();
        this.queryType = (short) 0;
        this.freshList = new ArrayList<DhtNode>();
        this.isExit = false;
        this.peerList = new ArrayList<PeerInfo>();

    }

    void sendPingMessage(String nodeId) {
        byte[] transactionId = this.randomTransactionId();
        String request = "d1:ad2:id20:" + nodeId + "e1:q4:ping1:t2:" + transactionId + "1:y1:qe";
        byte[] requestBytes = request.getBytes();
        this.lock.lock();
        try {
            this.requesthandler.putRequest(transactionId, requestBytes, this.dhtNodes, (short) 1);
        } finally {
            this.lock.unlock();
        }
    }

    void sendAnnouncePeerMessage(byte[] nodeId, byte[] infoHash, byte[] token) {
        byte[] transactionId = this.randomTransactionId();
        byte[] rp1 = "d1:ad2:id20:".getBytes();
        byte[] rp3 = "9:info_hash20:".getBytes();
        byte[] rp5 = "4:porti6881e5:token8:".getBytes();
        byte[] rp7 = "+e1:q13:announce_peer1:t2:".getBytes();
        byte[] rp9 = "1:y1:qe".getBytes();
        byte[] requestBytes = new byte[rp1.length + 20 + rp3.length + 20 + rp5.length + token.length + rp7.length + 2 + rp9.length];
        CheckUtilities cu = new CheckUtilities();
        int ii = 0;
        cu.copyBytes(rp1, requestBytes, 0, ii, rp1.length);
        ii += rp1.length;
        cu.copyBytes(nodeId, requestBytes, 0, ii, 20);
        ii += 20;
        cu.copyBytes(rp3, requestBytes, 0, ii, rp3.length);
        ii += rp3.length;
        cu.copyBytes(infoHash, requestBytes, 0, ii, 20);
        ii += 20;
        cu.copyBytes(rp5, requestBytes, 0, ii, rp5.length);
        ii += rp5.length;
        cu.copyBytes(token, requestBytes, 0, ii, token.length);
        ii += token.length;
        cu.copyBytes(rp7, requestBytes, 0, ii, rp7.length);
        ii += rp7.length;
        cu.copyBytes(transactionId, requestBytes, 0, ii, 2);
        ii += 2;
        cu.copyBytes(rp9, requestBytes, 0, ii, rp9.length);
        this.lock.lock();
        try {
            this.requesthandler.putRequest(transactionId, requestBytes, this.dhtNodes, (short) 2);
        } finally {
            this.lock.unlock();
        }
    }

    void sendGetPeerMessage(String nodeId, byte[] infoHash) {
        this.infohash = infoHash;
        byte[] transactionId = this.randomTransactionId();
        byte[] requestp1 = ("d1:ad2:id20:" + nodeId + "9:info_hash20:").getBytes();
        byte[] requestp2 = infoHash;
        byte[] requestp3 = "e1:q9:get_peers1:t2:".getBytes();
        byte[] requestp4 = transactionId;
        byte[] requestp5 = "1:y1:qe".getBytes();
        byte[] requestBytes = new byte[requestp1.length + requestp2.length + requestp3.length + requestp4.length + requestp5.length];
        CheckUtilities cu = new CheckUtilities();
        int len = 0;
        cu.copyBytes(requestp1, requestBytes, 0, len, requestp1.length);
        len += requestp1.length;
        cu.copyBytes(requestp2, requestBytes, 0, len, requestp2.length);
        len += requestp2.length;
        cu.copyBytes(requestp3, requestBytes, 0, len, requestp3.length);
        len += requestp3.length;
        cu.copyBytes(requestp4, requestBytes, 0, len, requestp4.length);
        len += requestp4.length;
        cu.copyBytes(requestp5, requestBytes, 0, len, requestp5.length);

// System.out.println("xxxx");
        this.lock.lock();
        try {
            this.requesthandler.putRequest(transactionId, requestBytes, this.dhtNodes, (short) 3);
            // System.out.println("xxxx");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }
    }

    void sendFindNodeMessage(String nodeId, String queryNodeId) {
        byte[] transactionId = this.randomTransactionId();
        String request = "d1:ad2:id20:" + nodeId + "6:target20:" + queryNodeId + "e1:q9:find_node1:t2:" + transactionId + "1:y1:qe";
        byte[] requestBytes = request.getBytes();
        this.lock.lock();
        try {
            this.requesthandler.putRequest(transactionId, requestBytes, this.dhtNodes, (short) 4);
        } finally {
            this.lock.unlock();
        }
    }

    String decodeString() {
        String a = "";
        while (this.data[i] != (int) byteArrayDivider) {
            a = a + (char) this.data[i];
            i++;
        }
        String b = "";
        int llength = Integer.parseInt(a);
        i++;
        for (int j = i; j < i + llength; j++) {
            b = b + (char) this.data[j];
        }
        i = i + llength - 1;
        return b;
    }

    long decodeNumber() {
        String a = "";
        while ((char) this.data[i] != numberEnd) {
            a = a + (char) this.data[i];
            i++;
        }
        return Long.parseLong(a);
    }

    public byte[] randomTransactionId() {
        byte[] xx = new byte[2];
        byte[] xa = new byte[1];
        byte[] xb = new byte[1];
        (new Random()).nextBytes(xa);
        (new Random()).nextBytes(xb);
        xx[0] = xa[0];
        xx[1] = xb[0];
        return xx;

    }

    public void handleDhtResponse() throws SocketTimeoutException {
        while (!isExit) {
            this.requesthandler.respon.lock();
            try {
                if (!this.requesthandler.responded.isEmpty()) {
                    for (byte[] obb : this.requesthandler.responded.keySet()) {
                        //   System.out.println("Solving response -");
                        DhtRequestHandlerData ob = this.requesthandler.responded.get(obb);
                        if (ob.queryType == 1) {
                            handlePingResponse(ob.response);
                            this.requesthandler.responded.remove(obb);
                        } else if (ob.queryType == 2) {
                            handleAnnouncePeerResponse(ob.response);
                            this.requesthandler.responded.remove(obb);
                        } else if (ob.queryType == 3) {
                            //   System.out.println("Get peer response handling");
                            this.data = ob.response;
                            this.queryType = ob.queryType;
                            i = 0;
                            this.requesthandler.responded.remove(obb);
                            decode();
                        } else if (ob.queryType == 4) {
                            handleFindNodeResponse(ob.response);
                            this.requesthandler.responded.remove(obb);
                        } else {
                            System.out.println("Something unwanted");
                        }
                    }
                }
            } finally {
                this.requesthandler.respon.unlock();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleFindNodeResponse(byte[] resp) {
    }

    public void handlePingResponse(byte[] resp) {
    }

    public void handleAnnouncePeerResponse(byte[] resp) {
        System.out.println("Got announce-peer response");
        return;
    }

    public void handleGetPeerResponse() {
        CheckUtilities cu = new CheckUtilities();
        byte[] token = new byte[10];
        Dht.i++;
        if ((char) this.data[i] == 'd') {
            Dht.i++;
            while ((char) this.data[i] != 'e') {
                char a = (char) data[i];
                if (Character.isDigit(a)) {
                    String x = decodeString();
                    if (x.equals("id")) {
                        Dht.i += 23;
                        //    System.out.println("decoding id");
                    } else if (x.equals("token")) {
                        //  System.out.println("decoding token");
                        Dht.i++;
                        int l = this.getStringLength();
                        token = new byte[l];
                        cu.copyBytes(data, token, i, 0, l);
                    } else if (x.equals("nodes")) {
                        // System.out.println("decoding nodes");
                        i++;
                        int l = this.getStringLength();
                        // System.out.println("length of nodes :" + l);
                        for (int j = 0; j < l; j = j + 26) {
                            byte[] nodeId = new byte[20];
                            byte[] ip = new byte[4];
                            byte[] port = new byte[2];
                            cu.copyBytes(data, nodeId, i, 0, 20);
                            i += 20;
                            cu.copyBytes(data, ip, i, 0, 4);
                            i += 4;
                            cu.copyBytes(data, port, i, 0, 2);
                            i += 2;
                            DhtNode node = (new DhtNode(cu.convertIpFromByteArrayToString(ip), cu.convertPortFromByteArrayToInt(port), nodeId));
                            boolean ff = true;
                            for (DhtNode ob : this.freshList) {
                                if ((ob.ip).equals(node.ip)) {
                                    // System.out.println("Found duplicate dht node");
                                    ff = false;
                                    break;
                                }
                            }
                            if (ff) {
                                //System.out.println("adding new dht node");
                                this.sendAnnouncePeerMessage(this.dhtNodes.nodeId, this.infohash, token);
                                this.freshList.add(node);
                            }
                        }

                    } else if (x.equals("value")) {
                        System.out.println("decoding peers");
                        i++;
                        if ((char) data[i] == 'l') {
                            i++;
                            while ((char) data[i] != 'e') {
                                if ((char) data[i] == '6') {
                                    i++;
                                    byte[] ip = new byte[4];
                                    byte[] port = new byte[2];
                                    cu.copyBytes(data, ip, i, 0, 4);
                                    i += 4;
                                    cu.copyBytes(data, port, i, 0, 2);
                                    i += 2;
                                    PeerInfo newPeer = new PeerInfo(cu.convertIpFromByteArrayToString(ip), cu.convertPortFromByteArrayToInt(port));
                                    boolean pp = true;
                                    for (PeerInfo pi : this.peerList) {
                                        if ((pi.peer.ip).equals(newPeer.peer.ip)) {
                                            pp = false;
                                            break;
                                        }
                                    }
                                    if (pp) {
                                        System.out.println("found new peer");
                                        this.peerList.add(newPeer);
                                    }
                                }
                            }
                        }

                    }
                }
                i++;
            }

        }
    }

    int getStringLength() {
        int len = 0;
        String x = "";
        while (Character.isDigit((char) data[i])) {
            x = x + (char) data[i];
            i++;
        }
        len = Integer.parseInt(x);
        return len;
    }

    void decode() {
        while (i < this.data.length) {
            decodeNextObject();
            i++;
        }
    }

    void decodeNextObject() {
        char a = (char) this.data[i];
        if (a == dictionaryEnd || a == listEnd) {
            return;
        }
        if (a == dictionaryStart) {
            i++;
            decodeDictionary();
        } else if (a == numberStart) {
            i++;
            decodeNumber();
        } else if (Character.isDigit(a)) {
            String x = decodeString();
            if (x.equals("t") || x.equals("y") || x.equals("q")) {
                i++;
                this.decodeNextObject();
            } else if (x.equals("r")) {
                if (this.queryType == 3) {
                    this.handleGetPeerResponse();
                }
            }
        }
    }

    void decodeDictionary() {

        decodeNextObject();
    }

    public void run() {
        try {
            handleDhtResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getKademliaDistanceMetric(byte[] infoHash, byte[] nodeId) {
        byte[] met = new byte[20];
        for (int i = 0; i < 20; i++) {
            met[i] = (byte) (infoHash[i] ^ nodeId[i]);
        }
        return met;
    }

    public boolean isSmallerKademliaDistance(byte[] a, byte[] b) {
        CheckUtilities cu = new CheckUtilities();
        for (int i = 0; i < 20; i += 2) {
            int aa = cu.readShort(a, i);
            int bb = cu.readShort(b, i);
            if (aa < bb) {
                return true;
            } else if (aa > bb) {
                return false;
            }
        }
        return false;
    }

    public void start() {
        this.t = new Thread(this);
        t.start();
    }

    public void stopHandlingResponses() {
        this.isExit = true;
        try {
            this.t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
