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
public class TableEntryPeers {
     private SimpleStringProperty country;
    private SimpleStringProperty port;
    private SimpleStringProperty ip;
    private SimpleStringProperty speedUp;
    private SimpleStringProperty speedDown;

    public TableEntryPeers(SimpleStringProperty country, SimpleStringProperty port, SimpleStringProperty ip, SimpleStringProperty speedUp, SimpleStringProperty speedDown, SimpleStringProperty downlaoded) {
        this.country = country;
        this.port = port;
        this.ip = ip;
        this.speedUp = speedUp;
        this.speedDown = speedDown;
        this.downlaoded = downlaoded;
    }

    public SimpleStringProperty getCountry() {
        return country;
    }

    public void setCountry(SimpleStringProperty country) {
        this.country = country;
    }

    public SimpleStringProperty getPort() {
        return port;
    }

    public void setPort(SimpleStringProperty port) {
        this.port = port;
    }

    public SimpleStringProperty getIp() {
        return ip;
    }

    public void setIp(SimpleStringProperty ip) {
        this.ip = ip;
    }

    public SimpleStringProperty getSpeedUp() {
        return speedUp;
    }

    public void setSpeedUp(SimpleStringProperty speedUp) {
        this.speedUp = speedUp;
    }

    public SimpleStringProperty getSpeedDown() {
        return speedDown;
    }

    public void setSpeedDown(SimpleStringProperty speedDown) {
        this.speedDown = speedDown;
    }

    public SimpleStringProperty getDownlaoded() {
        return downlaoded;
    }

    public void setDownlaoded(SimpleStringProperty downlaoded) {
        this.downlaoded = downlaoded;
    }
    private SimpleStringProperty downlaoded;
    
}
