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
public class DhtNode {

    String ip;
    int port;
    byte[] nodeId;

    public DhtNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.nodeId = new byte[20];
    }

    public DhtNode(byte[] nodeId) {
        this.nodeId = nodeId;
    }

    public DhtNode(String ip, int port, byte[] nodeId) {
        this.ip = ip;
        this.port = port;
        this.nodeId = nodeId;
    }

    public void printDetails() {
        System.out.println("Node id-" + (new CheckUtilities()).byteArrayToByteString(this.nodeId));
        System.out.println("Ip-" + this.ip);
        System.out.println("Port-" + this.port);
    }

}
