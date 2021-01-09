/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;

/**
 *
 * @author root
 */
public class PeerInfo {

    PeerDetails peer;
    boolean choked;
    boolean interested;
    byte[] info_hash;
    PeerInfo(String id, String ipadd, int portadd) {
        this.peer=new PeerDetails(ipadd,portadd,id);
        this.choked = true;
        this.interested = false;
    }

    PeerInfo(String ipadd, int portadd) {
        this.peer=new PeerDetails(ipadd,portadd);
        this.choked = true;
        this.interested = false;
    }

    void sendHandshake(byte[] infoHash, String peerId) {
        CheckUtilities cu = new CheckUtilities();
        String protocol = "BitTorrent protocol";//BTP/1.0
        short length = 19;
        byte[] len = new byte[2];
        cu.putShort(len, 0, length);
        System.out.println(len[1]);
        byte[] handshake = new byte[68];
        cu.copyBytes(len, handshake, 1, 0, 1);
        cu.putString(handshake, 1, protocol, 19);
        System.out.println(handshake[1]);
        //8 bytes reserved
        byte[] reserved=new byte[8];
        reserved[7]=0x01;//support Dht
        reserved[5]=0x10;
        System.out.println(cu.byteArrayToByteString(reserved));
        cu.copyBytes(reserved, handshake,0,20,8);
        cu.copyBytes(infoHash, handshake, 0, 28, 20);
        cu.putString(handshake, 48, peerId, 20);
        System.out.println(cu.byteArrayToByteString(handshake));
        byte[] resp = this.sendData(handshake, 68);
        handleHandshakeResponse(resp);
    }

    void handleHandshakeResponse(byte[] response) {
        CheckUtilities cu = new CheckUtilities();
        byte[] len = new byte[2];
        len[1] = response[0];
        short length = cu.readShort(len, 0);
        String protocol = cu.readString(response, 2, 19);
        String reserved = cu.readString(response, 21, 8);
        String info = cu.readString(response, 48, 20);
        System.out.println("Response from peer -");
        System.out.println("protocol -" + protocol);
        System.out.println("reserved -" + reserved);
        System.out.println("info -" + info);
    }

    byte[] sendData(byte[] data, int responseLengthInBytes) {
        byte[] response = new byte[responseLengthInBytes];
        try {
            Socket connectPeer = new Socket(this.peer.ip,this.peer.port);
            DataOutputStream outToPeer = new DataOutputStream(connectPeer.getOutputStream());
            outToPeer.write(data);
            DataInputStream inFromPeer = new DataInputStream(connectPeer.getInputStream());
            inFromPeer.read(response);

        } catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        }
        return response;
    }

    void sendData(byte[] data) {
        try {
            Socket connectPeer = new Socket((new CheckUtilities()).getSystemIp(), (new CheckUtilities()).getfreePort());
            DataOutputStream outToPeer = new DataOutputStream(connectPeer.getOutputStream());
            outToPeer.write(data);
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        }
    }

    void sendKeepAlivePacket() {
        byte[] keepAlive = new byte[4];
        this.sendData(keepAlive);
    }

    void sendChoke() {
        CheckUtilities cu = new CheckUtilities();
        byte[] choke = new byte[5];
        cu.putInt(choke, 0, 1);
        cu.copyBytes(this.getOneByteNumber((short) 0), choke, 0, 4, 1);
        this.sendData(choke);
    }

    void sendUnchoke() {
        CheckUtilities cu = new CheckUtilities();
        byte[] unchoke = new byte[5];
        cu.putInt(unchoke, 0, 1);
        cu.copyBytes(this.getOneByteNumber((short) 1), unchoke, 0, 4, 1);
        this.sendData(unchoke);
    }

    void sendInterested() {
        CheckUtilities cu = new CheckUtilities();
        byte[] interested = new byte[5];
        cu.putInt(interested, 0, 1);
        cu.copyBytes(this.getOneByteNumber((short) 2), interested, 0, 4, 1);
        this.sendData(interested);
    }

    void sendNotInterested() {
        CheckUtilities cu = new CheckUtilities();
        byte[] notInterested = new byte[5];
        cu.putInt(notInterested, 0, 1);
        cu.copyBytes(this.getOneByteNumber((short) 3), notInterested, 0, 4, 1);
        this.sendData(notInterested);
    }

    void sendHave(int pieceIndex) {
        CheckUtilities cu = new CheckUtilities();
        byte[] have = new byte[9];
        cu.putInt(have, 0, 5);
        cu.copyBytes(this.getOneByteNumber((short) 4), have, 0, 4, 1);
        cu.putInt(have, 5, pieceIndex);
        this.sendData(have);
    }

    void sendBitfield(byte[] bitf) {
        CheckUtilities cu = new CheckUtilities();
        byte[] bitfield = new byte[5 + bitf.length];
        cu.putInt(bitfield, 0, 1);
        cu.copyBytes(this.getOneByteNumber((short) 5), bitfield, 0, 4, 1);
        cu.copyBytes(bitf, bitfield, 0, 5, bitf.length);
        this.sendData(bitfield);
    }

    void sendRequest() {
        CheckUtilities cu = new CheckUtilities();
        byte[] request = new byte[17];
        cu.putInt(request, 0, 13);
        cu.copyBytes(this.getOneByteNumber((short) 6), request, 0, 4, 1);
        this.sendData(request);
    }

    byte[] recieveData(int bytes) {
        byte[] resp = new byte[bytes];
        return resp;
    }

    byte[] getOneByteNumber(short data) {
        CheckUtilities cu = new CheckUtilities();
        byte[] len = new byte[2];
        cu.putShort(len, 0, data);
        byte[] num = new byte[1];
        num[0] = len[1];
        return num;
    }
    public static void main(String args[])
    {
        CheckUtilities cu = new CheckUtilities();
        PeerInfo pi=new PeerInfo("47.30.187.159",6881);
        byte[] jj=new byte[20];
        cu.putString(jj, 0,"AEF8BF4E43B99B7DA70DB875F4E66CFC66B86AC2", 20);
        pi.sendHandshake(jj,"-MS1000-371752393256");
    }
}
