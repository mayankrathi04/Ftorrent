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
public class TrackerHandler extends Thread {
    Thread t;
    TorrentFileInfo newTorrent;
    String peerId;
    volatile boolean isExitTracker;
    List<PeerInfo> peerList;
    
    public TrackerHandler() {
        this.peerList = new ArrayList<PeerInfo>();
        this.isExitTracker=false;
    }
    @Override
    public void run()
    {
        List<Tracker> xx = this.newTorrent.getAnnounce();
        int i = 1;
        for (Tracker obj : xx) {
            if(this.isExitTracker)return;
           if (i > 11 && i != 12 && i != 13 && i != 14 && i != 15 && i != 16 && i != 19 && i != 20 && i != 23 && i != 28 && i != 18 && i != 41) {
                if (obj.url.contains("http://") || obj.url.contains("https://")) {
                    System.out.println(i + ") http/https request");
                    obj.createRequest(newTorrent.getInfoHash(), 0, 0,0, 1000, Event.started, this.peerId);
                } else if (obj.url.contains("udp://")) {
                    obj.createRequest(newTorrent.getInfoHash(), 0, 0,0, 1000, Event.started, this.peerId);
                    System.out.println(i + ") udp request");
                    obj.requestTracker();
                    this.updateTrackerPeerList(obj);
                }
            //    obj.requestTracker();
              // this.updateTrackerPeerList(obj);

            }
            i++;
            System.out.println("Seeders-"+obj.responseParameter.seeders);
            System.out.println("Leechers-"+obj.responseParameter.leechers);
        }
        this.printPeerList();
    }
    public void updateTrackerPeerList(Tracker obj)
    {
         List<PeerInfo> temp = obj.responseParameter.peersArray;
         System.out.println("Updating tracker peer list");
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
       t=new Thread(this,"trackerhandler");
       t.start();
    }
   void exit()
   {
       this.isExitTracker=true;
   }
}
