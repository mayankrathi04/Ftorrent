/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;
import java.util.Random;
/**
 *
 * @author root
 */
enum Event
{
    started,stopped,completed;
}
public class TrackerRequestParameter {
    public byte[] info_hash;
    public String peerId;
    public int port;
    public long uploaded;
    public long downloaded;
    public long left;
    public short compact;
    public short no_peer_id;
    public Event event  ;
    public String ip;
    public int numwant;
    public int key;
    public String trackerid;
    TrackerRequestParameter(byte[] info,long up,long down,long lef,int peers,Event eve,String peerid)
    {
        /*
       Random r = new Random();
       long randomValue =r.nextLong();
       if(randomValue<0)
           randomValue=-randomValue;
       this.peerId="-MSH1000-"+randomValue;
*/
        this.peerId=peerid;
       this.ip=(new CheckUtilities()).getSystemIp();
       this.port=(new CheckUtilities()).getfreePort();     
       this.info_hash=info;
       this.downloaded=down;
       this.uploaded=up;
       this.left=lef;
       this.numwant=peers;
       this.event=eve;
       this.no_peer_id=1;
       this.compact=1;
       this.key=0;
       this.trackerid="";
    }
    void printRequest()
    {
        System.out.println("Peer id:"+this.peerId);
        System.out.println("ip:"+this.ip);
        System.out.println("port:"+this.port);
        System.out.println("info_hash:"+this.info_hash);
        System.out.println("Downloaded :"+this.downloaded);
        System.out.println("Uploaded:"+this.uploaded);
        System.out.println("Left:"+this.left);
    }
}
