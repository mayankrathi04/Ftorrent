/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.sql.*;

/**
 *
 * @author root
 */
public class TorrentDatabase {

    int insertDataIntoTrackers(TableEntryTrackers obj) {
        //return 1 if successfully inserted else return 0
        
        return 1;
    }

    int insertDataIntoPeers(TableEntryPeers obj) {
        //return 1 if successfully inserted else return 0
        
        return 1;
    }

    int insertDataIntoTorrents(Table.TableEntry obj) {
        //return 1 if successfully inserted else return 0
        
        return 1;
    }

    int updateDataIntoTrackers(byte[] infoHash,TrackersTable obj) {

    }

    int updateDataIntoPeers() {
        
    }

    TableEntryPeers getDataFromPeers(byte[] infoHash) {
        TableEntryPeers obj;
        //fetch data from database
        return obj;
    }

    Table.TableEntry getDataFromTorrents(byte[] infoHash) {
        Table.TableEntry obj;
        //fetch data from database
        return obj;
    }

    TableEntryTrackers getDataFromTrackers(byte[] infoHash) {
        TableEntryTrackers obj;
        //fetch data from database
        return obj;
    }

    int deleteDataFromTrackers(byte[] infoHash) {
        //return 1 if successfully deleted else return 0
        return 1;
    }

    int deleteDataFromPeers(byte[] infoHash) {
        //return 1 if successfully deleted else return 0
        
        return 1;
    }

    int deleteDataFromTorrents(byte[] infoHash) {
        //return 1 if successfully deleted else return 0
        
        return 1;
    }
    
    
    
    public static void main( String args[] ) {
      Connection c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:Torrent.db");
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "CREATE TABLE Torrents" +
                        "(InfoHex VARCHAR(40) PRIMARY KEY,"+
                        "InfoHash blob NOT NULL," +
                        "Size DOUBLE NOT NULL," + 
                        "Done DOUBLE NOT NULL," + 
                        "Status TEXT CHECK( Status IN('Downloaded','Paused','Stopped','Seeding'))," +
                        "Seeds INT," +
                        "Peers INT," +
                        "Uploaded DOUBLE,"+
                        "AddedOn VARCHAR(255))"; 
         
         String sql1="CREATE TABLE Trackers" +
                 "(FORIEGN KEY(InfoHash) REFERENCES Torrents(InfoHash),"+
                 " Id INT NOT NULL,"+
                 " Url VARCHAR(255) NOT NULL,"+
                  "Status VARCHAR(255) NOT NULL,Received DOUBLE,Seeds INT,Peers INT,Downloaded DOUBLE)";
         String sql2="CREATE TABLE Peers"+
                  "(FORIEGN KEY(InfoHash) REFERENCES Torrents(InfoHash),Country VARCHAR(255),Ip VARCHAR(255) NOT NULL,Port INT NOT NULL)";
         String sql3=sql+";"+sql1+";"+sql2+";";
         stmt.executeUpdate(sql1);
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }
}
