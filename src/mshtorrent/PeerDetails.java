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
public class PeerDetails {

    String ip;
    int port;
    String peerId;

    public PeerDetails(String ip, int port, String peerId) {
        this.ip = ip;
        this.port = port;
        this.peerId = peerId;
    }

    public PeerDetails(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void printDetails() {
        System.out.println("PeerIp-"+this.ip);
        System.out.println("PeerPort-"+this.port);        
    }
}
