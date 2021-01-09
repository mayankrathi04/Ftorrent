/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author root
 */
public class TableEntryTrackers {

    private SimpleStringProperty hash;
    private SimpleStringProperty url;
    private SimpleStringProperty recieved;
    private SimpleStringProperty seeds;
    private SimpleStringProperty peers;
    private SimpleStringProperty downlaoded;

    public SimpleStringProperty getHash() {
        return hash;
    }

    public void setHash(SimpleStringProperty hash) {
        this.hash = hash;
    }

    public SimpleStringProperty getUrl() {
        return url;
    }

    public void setUrl(SimpleStringProperty url) {
        this.url = url;
    }

    public SimpleStringProperty getRecieved() {
        return recieved;
    }

    public void setRecieved(SimpleStringProperty recieved) {
        this.recieved = recieved;
    }

    public SimpleStringProperty getSeeds() {
        return seeds;
    }

    public void setSeeds(SimpleStringProperty seeds) {
        this.seeds = seeds;
    }

    public SimpleStringProperty getPeers() {
        return peers;
    }

    public void setPeers(SimpleStringProperty peers) {
        this.peers = peers;
    }

    public SimpleStringProperty getDownlaoded() {
        return downlaoded;
    }

    public void setDownlaoded(SimpleStringProperty downlaoded) {
        this.downlaoded = downlaoded;
    }

    public TableEntryTrackers(SimpleStringProperty hash, SimpleStringProperty url, SimpleStringProperty recieved, SimpleStringProperty seeds, SimpleStringProperty peers, SimpleStringProperty downlaoded) {
        this.hash = hash;
        this.url = url;
        this.recieved = recieved;
        this.seeds = seeds;
        this.peers = peers;
        this.downlaoded = downlaoded;
    }

}
