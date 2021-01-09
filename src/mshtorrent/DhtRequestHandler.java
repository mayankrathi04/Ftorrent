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
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DhtRequestHandler extends Thread {

    private volatile Lock lock;
    private volatile Lock con;
    public volatile Lock respon;
    DatagramSocket ds;
    volatile ConcurrentHashMap<byte[], DhtRequestHandlerData> requests;
    volatile ConcurrentHashMap<byte[], DhtRequestHandlerData> responded;
    Thread t1;
    Thread t2;
    volatile boolean isExit;

    public DhtRequestHandler() {
        this.requests = new ConcurrentHashMap<byte[], DhtRequestHandlerData>();
        this.responded = new ConcurrentHashMap<byte[], DhtRequestHandlerData>();
        this.lock = new ReentrantLock();
        this.con = new ReentrantLock();
        this.respon = new ReentrantLock();
        try {
            this.ds = new DatagramSocket(6881);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.isExit = false;
    }

    void putRequest(byte[] transid, byte[] req, DhtNode nod, short query) {
        this.lock.lock();
        /*
        try {
            System.out.println("Putting new request transaction id:" + (new String(transid, "UTF-8")));
        } catch (Exception e) {
            System.err.println("Reponse format not utf-8");
        }
        */
        DhtNode node = new DhtNode(nod.ip, nod.port);
        this.requests.put(transid, (new DhtRequestHandlerData(req, node, query)));
        this.lock.unlock();
        //  System.out.println("yyyy");
    }

    void sendRequest() {
        while (!this.isExit) {
            // System.out.println("Sending pending requests");
            this.lock.lock();
            try {
                //  System.out.println("zzzz");
                if (!this.requests.isEmpty()) {
                    //   System.out.println("aaaa");
                    for (byte[] obj : this.requests.keySet()) {
                        DhtRequestHandlerData ob = this.requests.get(obj);
                        try {
                            if (ob.transmissionsLeft > 0 && ((new Date()).getTime() - ob.sendTime) > ob.timeoutPeriod) {
                                if (this.sendMessage(ob.request, ob.node, ob)) {
                                    ob.updateTimeoutPeriod();
                                    ob.sendTime = (new Date()).getTime();
                                    //System.out.println("transmissions left-" + ob.transmissionsLeft);
                                    ob.transmissionsLeft--;
                                }
                            } else if (ob.transmissionsLeft <= 0) {
                                this.requests.remove(obj);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                this.lock.unlock();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //  System.out.println("All pending request sent");
        }
    }

    public boolean sendMessage(byte[] data, DhtNode nod, DhtRequestHandlerData ob) throws Exception {
        try {
            InetAddress ip = InetAddress.getByName(nod.ip);
            //System.out.println("ip-" + ip + " port-" + nod.port);
            DatagramPacket dp = new DatagramPacket(data, data.length, ip, nod.port);
            this.con.lock();
            try {
             //   System.out.println("Sending message-" + data);
                this.ds.send(dp);
            } finally {
                this.con.unlock();
            }
            //ds.close();
        } catch (UnknownHostException e) {
            System.out.println("problem in resolving");
            System.err.println(e.getMessage());
            //ds.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            //ds.close();
        }
        return true;
    }

    void recieve() throws SocketException {
        while (!this.isExit) {
            byte[] recieve = new byte[1400];
            DatagramPacket p = new DatagramPacket(recieve, recieve.length);
            this.con.lock();
            try {
                ds.setSoTimeout(3000);
                ds.receive(p);
                this.handleResponse(recieve);
            } catch (IOException e) {
                System.out.println("No response");
            } finally {
                this.con.unlock();
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //ds.close();
        }
    }

    void handleResponse(byte[] response) {
        String x = "1:t2:";
        byte[] transId = new byte[2];
        for (int i = 0; i < response.length; i++) {
            if ((char) response[i] == '1') {
                boolean found = true;
                for (int j = 1; j < x.length(); j++) {
                    if ((char) response[i + j] != x.charAt(j)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    transId[0] = response[i + 5];
                    transId[1] = response[i + 6];
                    break;
                }
            }
        }
        try {
         //   System.out.println("Transaction id of response :" + new String(transId, "UTF-8"));
            // System.out.println("response -");
            // System.out.println(new String(response, "UTF-8"));
        } catch (Exception e) {
            System.err.println("Reponse format not utf-8");
        }
        boolean match = false;
       this.lock.lock();
        for (byte[] key : this.requests.keySet()) {
            try {
                // System.out.println("request :" + new String(key, "UTF-8"));
                if ((new CheckUtilities()).areSameByteArray(key, transId)) {
                    DhtRequestHandlerData data = (this.requests.get(key));
                    DhtRequestHandlerData datacopy = new DhtRequestHandlerData(data.request, response, data.node.nodeId, data.node.ip, data.node.port, data.queryType);
                    this.respon.lock();
                    this.responded.put(transId, datacopy);
                    this.respon.unlock();
                    this.requests.remove(key);
                    match = true;
                    break;
                }
            } catch (Exception e) {
                System.err.println("Reponse format not utf-8");
            }
        }
        this.lock.unlock();
        if (!match) {
            System.err.println("Invalid response");
            try {
                //  System.out.println("Transaction id of response :" + new String(transId, "UTF-8"));
                 System.out.println("response -");
                System.out.println(new String(response, "UTF-8"));
            } catch (Exception e) {
                System.err.println("Reponse format not utf-8");
            }
        }
    }

    @Override
    public void run() {
        try {
            Thread t = Thread.currentThread();
            String name = t.getName();
            if (name.equals("response")) {
                this.recieve();
            } else if (name.equals("request")) {
                this.sendRequest();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start() {
        this.t1 = new Thread(this, "request");
        this.t2 = new Thread(this, "response");
        this.t1.start();
        this.t2.start();
    }

    public void stopAfterCompleted() {
        while (true) {
            // this.lock.lock();
            this.printCountRequests();
            this.printCountResponded();
            if (this.requests.isEmpty() && this.responded.isEmpty()) {
                System.out.println("stopping");
                this.isExit = true;
                break;
            }
            if (this.isExit) {
                try {
                    this.t2.join();
                    this.t1.join();
                    this.isExit = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //this.lock.unlock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void waitTillCompletePendingTask() {
        while (true) {
            //   this.lock.lock();
            // this.printCountRequests();
            // this.printCountResponded();
            if (this.requests.isEmpty() && this.responded.isEmpty()) {
                //System.out.println("All pending task completed");
                break;
            }
            //   this.lock.unlock();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printCountRequests() {
        int k = 0;
        for (byte[] it : this.requests.keySet()) {
            k++;
        }
        System.out.println("Total requests pending-" + k);
    }

    public void printCountResponded() {
        int k = 0;
        for (byte[] it : this.responded.keySet()) {
            k++;
        }
        System.out.println("Total pending response handling -" + k);
    }
}
