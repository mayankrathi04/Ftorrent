/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class TorrentCurrentInfo {
    long downloaded;
    long uploaded;
    long left;
    long complete;
    long incomplete;
    boolean choked;
    boolean interested;
    public int leechers;
    public int seeders;
    List<PeerInfo> peerList;
    TorrentCurrentInfo()
    {
        this.complete=0;
        this.incomplete=0;
        this.downloaded=0;
        this.uploaded=0;
        this.left=0;
        this.choked=false;
        this.interested=false;
        this.seeders=0;
        this.leechers=0;
        this.peerList=new ArrayList<PeerInfo>();
    }
    void printDetails()
    {
        System.out.println("Complete -"+this.complete);
        System.out.println("Incomplete -"+this.incomplete);
        System.out.println("Downloaded -"+this.downloaded);
        System.out.println("Uploaded -"+this.uploaded);
        System.out.println("Left -"+this.left);
        System.out.println("Seeders -"+this.seeders);
        System.out.println("Leechers -"+this.leechers);
        int i=1;
        for(PeerInfo pi:this.peerList)
        {
            System.out.println(i+")Ip-"+pi.peer.ip+"Port-"+pi.peer.port);
            i++;
        }
    }
}
