/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.util.*;

/**
 *
 * @author root
 */
public class TrackerResponseParameter {

    public String failureReason;
    public String warningMessage;
    public long interval;
    public int minInterval;
    public String trackerId;
    public long complete;
    public long incomplete;
    public int seeders;
    public int leechers;
    public List<PeerInfo> peersArray;

    TrackerResponseParameter() {
        this.peersArray = new ArrayList<PeerInfo>();
        this.failureReason = "";
        this.warningMessage = "";
        this.interval = 0;
        this.minInterval = 0;
        this.leechers = 0;
        this.seeders = 0;
        this.trackerId = "";
        this.incomplete = 0;
        this.complete = 0;
    }

    void printResponse() {
        System.out.println("---------------");
        if (this.failureReason != "") {
            System.out.println("Failure Reason -" + this.failureReason);
        }
        if (this.warningMessage != "") {
            System.out.println("Warning Message -" + this.warningMessage);
        }
        System.out.println("Interval -" + this.interval);
        if (this.minInterval != 0) {
            System.out.println("min Interval-" + this.minInterval);
        }
        if (this.trackerId != "") {
            System.out.println("trackerID- " + this.trackerId);
        }
        System.out.println("Complete -" + this.complete);
        System.out.println("Incomplete -" + this.incomplete);
        System.out.println("Leechers -" + this.leechers);
        System.out.println("Seeders -" + this.seeders);
        int i = 1;
        for (PeerInfo obj : this.peersArray) {
            System.out.println(i + ")" + "ip-" + obj.peer.ip + "port-" + obj.peer.port);
            i++;
        }
        System.out.println("----------------");
    }
}
