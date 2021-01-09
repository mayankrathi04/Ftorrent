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
public class PeerHandler extends Thread {
    Thread t;
    byte[] infoHash;
    String peerId;
    List<PeerInfo> globalPeerList;
    volatile boolean isExitPeerhandler;
    public PeerHandler(List<PeerInfo> globalPeerList,byte[] info,String id) {
        this.globalPeerList = globalPeerList;
        this.infoHash=info;
        this.peerId=id;
        this.isExitPeerhandler=false;
    }
    @Override
    public void run()
    {
      for (PeerInfo obj :this.globalPeerList) {
            obj.sendHandshake(this.infoHash, this.peerId);
        }
    }
    public void exec()
    {
     t=new Thread(this,"peerhandler");
     t.start();
    }
    void exit()
    {
        this.isExitPeerhandler=true;
    }
}
