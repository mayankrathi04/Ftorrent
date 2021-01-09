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
public class TorrentFileInfo {
    private List<Tracker> tracker;
    private String comment;
    private String creationDate; 
    private String createdBy;
    private String name;
    private String encoding;
    private long lengthFull;
    private byte[] pieces;
    private List<byte[]> pieceHashList;
    private long pieceLength;
    private List<IndividualFileInfo> files;
    private boolean isPrivate;
    private byte[] info_hash;
    private TorrentCurrentInfo currentInfo;
    TorrentFileInfo(List<Tracker> tracker,String name,String comment,String creationDate,String createdBy,String encoding,boolean isPrivate,List<IndividualFileInfo> files)
    {
       super();
       this.tracker=new ArrayList<Tracker>();
       this.tracker=tracker;
       this.creationDate=creationDate;
       this.createdBy=createdBy;
       this.isPrivate=isPrivate;
       this.encoding=encoding;
       this.comment=comment;
       this.name=name;
       this.files=new ArrayList<IndividualFileInfo>();
       this.pieceHashList=new ArrayList<byte[]>();
       this.files=files;
       this.currentInfo=new TorrentCurrentInfo();
    }
    public long getLengthFull() {
        return lengthFull;
    }

    public void setLengthFull(long lengthFull) {
        this.lengthFull = lengthFull;
    }
    public TorrentCurrentInfo getCurrentInfo() {
        return currentInfo;
    }

    public void setCurrentInfo(TorrentCurrentInfo currentInfo) {
        this.currentInfo = currentInfo;
    }
    TorrentFileInfo(){
    this.tracker=new ArrayList<Tracker>();
    this.files=new ArrayList<IndividualFileInfo>();
    this.pieceHashList=new ArrayList<byte[]>();
    this.currentInfo=new TorrentCurrentInfo();
    }
    byte[] getInfoHash()
    {
        return this.info_hash;
    }
    boolean setInfoHash(byte[] a)
    {
        this.info_hash=a;
        return true;
    }
    List<Tracker> getAnnounce()
    {
        return this.tracker;
    }
    String getComment()
    {
        return this.comment;
    }
    String getCreationDate()
    {
        return this.creationDate;
    }
    String getCreatedBy()
    {
        return this.createdBy;
    }
    String getEncoding()
    {
        return this.encoding;
    }
    String getName()
    {
        return this.name;
    }
    byte[] getNumberOfPieces()
    {
        return this.pieces;
    }
    long getPieceLength()
    {
        return this.pieceLength;
    }
    List<IndividualFileInfo> getFiles()
    {
        return this.files;
    }
    boolean checkIsPrivate()
    {
        return this.isPrivate;
    }
    boolean setAnnounce(String a)
    {
        this.tracker.add(new Tracker(a));
        return true;
    }
    boolean setComment(String a)
    {
        this.comment=a;
        return true;
    }
    boolean setCreationDate(String a)
    {
        this.creationDate=a;
        return true;
    }
    boolean setCreatedBy(String a)
    {
        this.createdBy=a;
       return true;
    }
    boolean setEncoding(String a)
    {
        this.encoding=a;
        return true;
    }
    boolean setName(String a)
    {
        this.name=a;
        return true;
    }
    boolean addFiles(IndividualFileInfo a)
    {
        this.files.add(a);
        return true;
    }
    boolean setIsPrivate(boolean a)
    {
        this.isPrivate=a;
        return true;
    }
    boolean setPieceHash(byte[] a)
    {
        this.pieceHashList.add(a);
        return true;
    }
     boolean setNumberOfPieces(byte[] a)
    {
        this.pieces=a;
        return true;
    }
    boolean setPieceLength(long a)
    {
        this.pieceLength=a;
        return true;    
    }
    void saveDataToDisk()
    {
        
    }
     void printDetails()
    {
        System.out.println("----------------------------------------------");
        System.out.println("Announce:");
        int ii=1;
        for(Tracker obj:this.tracker)
        {
            System.out.println(ii+")"+obj.url);
            ii++;
        }
        System.out.println("");
        System.out.println("Created By:"+this.createdBy);
        System.out.println("Creation Date:"+this.creationDate);
        System.out.println("Comments:"+this.comment);
        System.out.println("Name:"+this.name);
        System.out.println("Encoding:"+this.encoding);
        System.out.println("isPrivate:"+this.isPrivate);
        System.out.println("Info-hash :"+(new CheckUtilities()).byteArrayToByteString(this.info_hash));

        System.out.println("Piece Length:"+this.pieceLength);
       // System.out.println("Pieces:"+this.pieces);
        System.out.println("Files:");
        ii=1;
        for(IndividualFileInfo obj:files)
        {
            System.out.print(ii+")"+obj.getPath()+"\tSize:");
             System.out.println(obj.getLength());
            ii++;
        }
        this.currentInfo.printDetails();
         System.out.println("----------------------------------------------");

    }
}
