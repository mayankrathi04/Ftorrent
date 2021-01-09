/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class DhtHandler extends Thread {
     Thread t;
    TorrentFileInfo newTorrent;
    String peerId;
    List<PeerInfo> peerList;
    volatile boolean isExitDht;
    public DhtHandler()
    {
        this.peerList=new ArrayList<PeerInfo>();
        this.isExitDht=false;
    }
    @Override
    public void run()
    {
       Dht objj = new Dht("-MS1000-371752393256".getBytes());
        objj.start();
        objj.requesthandler.start();
        System.out.println("Initiating Dht bootstrap node search");
        for (DhtNode obj : Dht.getBootstrapNode()) {
            if(this.isExitDht){break;}
            objj.dhtNodes.ip = obj.ip;
            objj.dhtNodes.port = obj.port;
            System.out.println("Bootstrapping " + obj.ip);           
            objj.sendGetPeerMessage("-MS1000-371752393256", newTorrent.getInfoHash());
        }
        objj.requesthandler.waitTillCompletePendingTask();
        objj.moreNode = objj.freshList;
        objj.freshList = new ArrayList<DhtNode>();
        for (DhtNode dn : objj.moreNode) {
            dn.printDetails();
        }
        System.out.println("Searching recursively for nodes from bootstrap nodes");
        int i=0;
        while (!objj.moreNode.isEmpty()) {
                        if(this.isExitDht){break;}
            for (DhtNode obj : objj.moreNode) {
                objj.dhtNodes.ip = obj.ip;
                objj.dhtNodes.port = obj.port;       
                objj.sendGetPeerMessage("-MS1000-371752393256", newTorrent.getInfoHash());  
                if(this.isExitDht){break;}
               // objj.requesthandler.waitTillCompletePendingTask();
            }
            System.out.println("Finishing round "+i+" of recursive search");
            objj.requesthandler.waitTillCompletePendingTask();
            objj.moreNode.clear();
            objj.moreNode = objj.freshList;
            objj.freshList = new ArrayList<DhtNode>();
            if(this.isExitDht){break;}
            i++;
        }
        objj.requesthandler.stopAfterCompleted();
        objj.stopHandlingResponses();
         System.out.println("Recursive search completed for bootstrap nodes ");
        for (PeerInfo pi : objj.peerList) {
            pi.peer.printDetails();
        } 
        
    }
/*
    public void updateTrackerPeerList(Tracker obj)
    {
         List<PeerInfo> temp = obj.responseParameter.peersArray;
         System.out.println("Updating Dht peer list");
        for (PeerInfo pi : temp) {
            boolean found = false;
            for (PeerInfo npi :this.peerList) {
                if ((pi.peer.ip).equals(npi.peer.ip)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Found unique peer");
                this.peerList.add(pi);
            }
            found = false;
        }
    }
*/
    public void printPeerList()
    {
        System.out.println("PeerList tracker-");
        for(PeerInfo pi:this.peerList)
        {
           pi.peer.printDetails();
        }
    }
    public void exec(TorrentFileInfo newTor,String id)
    {
       this.peerId=id;
       this.newTorrent=newTor;
       t=new Thread(this,"dhthandler");
       t.start();
    }
    void exit()
    {
        this.isExitDht=true;
    }
}
