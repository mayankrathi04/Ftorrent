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
public class DhtRequestHandlerData {

    public static final int DEFAULT_TIMEOUT = 5000;//in milli seconds
    public static final short MAX_RETRAMISSIONS = 3;
    byte[] request;
    byte[] response;
    DhtNode node;
    short queryType;
    long timeoutPeriod;
    short transmissionsLeft;
    long sendTime;
    DhtRequestHandlerData(byte[] req, DhtNode node, short query) {
        this.request = req;
        this.node = node;
        this.queryType = query;
        this.transmissionsLeft =DhtRequestHandlerData.MAX_RETRAMISSIONS;
        this.timeoutPeriod=DhtRequestHandlerData.DEFAULT_TIMEOUT;
    }
    DhtRequestHandlerData(byte[] req,byte[] res, DhtNode node, short query) {
        this.request = req;
        this.node = node;
        this.response=res;
        this.queryType = query;
        this.transmissionsLeft =DhtRequestHandlerData.MAX_RETRAMISSIONS;
        this.timeoutPeriod=DhtRequestHandlerData.DEFAULT_TIMEOUT;
    }
    DhtRequestHandlerData(byte[] req,byte[] res,byte[] id,String ip,int port, short query) {
        this.request = req;
        this.node = new DhtNode(ip,port,id);
        this.response=res;
        this.queryType = query;
        this.transmissionsLeft =DhtRequestHandlerData.MAX_RETRAMISSIONS;
        this.timeoutPeriod=DhtRequestHandlerData.DEFAULT_TIMEOUT;
    }
    public void updateTimeoutPeriod() {
        this.timeoutPeriod = this.timeoutPeriod * ((int) (Math.pow(2, (DhtRequestHandlerData.MAX_RETRAMISSIONS-this.transmissionsLeft))));
    }
}
