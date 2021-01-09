/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.Iterator;

/**
 *
 * @author root
 */
public class TorrentFile {

    private static char dictionaryStart = 'd';
    private static char dictionaryEnd = 'e';
    private static char listStart = 'l';
    private static char listEnd = 'e';
    private static char numberStart = 'i';
    private static char numberEnd = 'e';
    private static char byteArrayDivider = ':';
    private static int i = 0;
    byte[] data;
    String filePath;
    static boolean aaa = false, bbb = false, ccc = false, ddd = false;
    TorrentFileInfo newTorrentFile;
    String peerId;
    List<PeerInfo> globalPeerList;
    TrackerHandler th ;
    LsdHandler lh;
    DhtHandler dh ;
    PeerHandler ph ;

    TorrentFile(String path) {
        this.newTorrentFile = new TorrentFileInfo();
        this.peerId = "-MS1000-371752393256";
        this.filePath = path;
        this.globalPeerList = new ArrayList<PeerInfo>();
        TorrentFile.i=0;
        TorrentFile.aaa=false;
        TorrentFile.bbb=false;
        TorrentFile.ccc=false;
        TorrentFile.ddd=false;
    }

    void readTorrentFile() {
        try {
            File torrent_file = new File(this.filePath);
            if (torrent_file.length() > 1000000) {
                System.out.println("Size limit exceeds standard file size.Most probably an invalid torrent file");
            } else {
                this.data = Files.readAllBytes(torrent_file.toPath());
                System.out.println("array length-" + this.data.length);
                // decode();
            }
        } catch (Exception e) {
            System.out.println("Not found");
        }
        decode();
    }

    void createTorrentFile() {

    }

    void decode() {
        while (i < this.data.length) {
            //  System.out.println(i);
            decodeNextObject();
            i++;
        }
        this.getPieceHashes();
    }

    void decodeNextObject() {
        //if(i>=this.data.length)return;
        char a = (char) this.data[i];
        if (a == dictionaryEnd || a == listEnd) {
            return;
        }
        if (a == dictionaryStart) {
            i++;
            decodeDictionary();
        } else if (a == listStart) {
            i++;
            decodeList();
        } else if (a == numberStart) {
            i++;
            decodeNumber();
        } else if (Character.isDigit(a)) {
            String x = decodeString();
            if (x.equals("announce")) {
                System.out.println("Setting up announce");
                i++;
                String z = decodeString();
                this.newTorrentFile.setAnnounce(z);
            } else if (x.equals("announce-list")) {
                System.out.println("Setting up announce-list");
                setupAnnounce();
            } else if (x.equals("creation date")) {
                System.out.println("Setting up creation date");
                i = i + 2;
                long z = decodeNumber();
                String y = "" + z;
                this.newTorrentFile.setCreationDate(y);
            } else if (x.equals("created by")) {
                System.out.println("Setting up created by");
                i++;
                String z = decodeString();
                this.newTorrentFile.setCreatedBy(z);
            } else if (x.equals("encoding")) {
                System.out.println("Setting up encoding");
                i++;
                String z = decodeString();
                this.newTorrentFile.setEncoding(z);
            } else if (x.equals("comment")) {
                System.out.println("Setting up comment");
                i++;
                String z = decodeString();
                this.newTorrentFile.setComment(z);
            } else if (x.equals("private")) {
                System.out.println("Setting up isPrivate");
                i = i + 2;
                long z = decodeNumber();
                this.newTorrentFile.setIsPrivate(z > 0 ? true : false);
            } else if (x.equals("files")) {
                i++;
                setupFiles();
                ddd = true;
            } else if (x.equals("name")) {
                System.out.println("Setting up name");
                i++;
                String z = decodeString();
                aaa = true;
                this.newTorrentFile.setName(z);
            } else if (x.equals("piece length")) {
                i = i + 2;

                System.out.println("Setting up piece length");
                long yy = decodeNumber();
                bbb = true;
                this.newTorrentFile.setPieceLength(yy);
            } else if (x.equals("pieces")) {
                i = i + 1;

                System.out.println("Setting up pieces count");
                decodePiecesHash();
                ccc = true;
            } else if (x.equals("info")) {
                System.out.println("Setting up info dict");
                decodeInfoDictionary();
            }
        }

    }

    public TorrentFile(String path, String peerId) {
        this.peerId = peerId;
        this.filePath = path;
    }

    void decodeInfoDictionary() {
        //   System.out.println(" decoding info dict ");
        i++;
        int start = i;

        while (!(aaa && bbb && ccc)) {
            decodeNextObject();
            i++;
        }
        //  System.out.println(" decoding info dictionar complete -" + (char) this.data[i]);
        int end = i;
        //  System.out.println("length-" + data.length + " end-" + end);
        this.calculateInfoHash(start, end);

    }

    void decodeDictionary() {

        decodeNextObject();
    }

    void decodeList() {

        decodeNextObject();
    }

    long decodeNumber() {
        String a = "";
        while ((char) this.data[i] != numberEnd) {
            a = a + (char) this.data[i];
            i++;
        }
        return Long.parseLong(a);
    }

    void decodePiecesHash() {
        String a = "";
        while (this.data[i] != (int) byteArrayDivider) {
            a = a + (char) this.data[i];
            i++;
        }

        int llength = Integer.parseInt(a);
        i++;
        byte[] b = new byte[llength];
        for (int j = i; j < i + llength; j++) {
            b[i] = data[j];
        }
        i = i + llength - 1;
        // System.out.println("String decoded :" + b + " " + (char) data[i]);
        this.newTorrentFile.setNumberOfPieces(b);
    }

    String decodeString() {
        String a = "";
        while (this.data[i] != (int) byteArrayDivider) {
            a = a + (char) this.data[i];
            i++;
        }
        String b = "";
        int llength = Integer.parseInt(a);
        i++;
        for (int j = i; j < i + llength; j++) {
            b = b + (char) this.data[j];
        }
        i = i + llength - 1;
        // System.out.println("String decoded :" + b + " " + (char) data[i]);
        return b;
    }

    void setupAnnounce() {
        i++;
        if ((char) data[i] == 'l') {
            i++;
            while ((char) data[i] != 'e') {
                if ((char) data[i] == 'l') {
                    i++;
                    while ((char) data[i] != 'e') {
                        String yy = decodeString();
                        this.newTorrentFile.setAnnounce(yy);
                        i++;
                    }
                    i++;
                }
            }
        }
    }

    void setupFiles() {
        System.out.println("Setting up files");
        if ((char) data[i] == 'l') {
            i++;
            while ((char) data[i] != 'e') {
                if ((char) data[i] == 'd') {
                    i++;
                    IndividualFileInfo newFile = new IndividualFileInfo();
                    while ((char) data[i] != 'e') {
                        String zz = decodeString();
                        if (zz.equals("path")) {
                            i++;
                            if ((char) data[i] == 'l') {
                                i++;
                                String yy = "";
                                while ((char) data[i] != 'e') {
                                    yy = yy + "/" + decodeString();
                                    i++;
                                }
                                newFile.setPath(yy);
                            }
                        } else if (zz.equals("length")) {
                            i = i + 2;
                            long yy = decodeNumber();
                            newFile.setLength(yy);
                        }
                        i++;
                    }
                    this.newTorrentFile.addFiles(newFile);
                }
                i++;
            }
        }
    }

    boolean getPieceHashes() {
        String hash_string = (new CheckUtilities()).byteArrayToByteString(this.newTorrentFile.getNumberOfPieces());
        if (hash_string.length() % 20 != 0) {
            System.err.println("Error: [TorrentFile.java] The SHA-1 hash for the file's pieces is not the correct length.");
            return false;
        }

        byte[] binary_data = new byte[hash_string.length()];
        byte[] individual_hash;
        int number_of_pieces = binary_data.length / 20;
        this.newTorrentFile.setLengthFull((number_of_pieces * this.newTorrentFile.getPieceLength()));
        for (int i = 0; i < binary_data.length; i++) {
            binary_data[i] = (byte) hash_string.charAt(i);
        }

        for (int i = 0; i < number_of_pieces; i++) {
            individual_hash = new byte[20];
            for (int j = 0; j < 20; j++) {
                individual_hash[j] = binary_data[(20 * i) + j];
            }
            this.newTorrentFile.setPieceHash(individual_hash);
        }

        return true;
    }

    void calculateInfoHash(int start, int end) {

        //System.out.println((char)this.data[start-1]+" "+(char)this.data[start+1]+"start -"+(char)this.data[start]+" end-"+(char)this.data[end]+" diff="+(end-start)+" "+(char)this.data[end-1]+" "+(char)this.data[end+1]);
        byte[] a = new byte[end - start + 1];
        int j = 0;
        while (start <= end) {
            a[j] = this.data[start];
            start++;
            j++;
        }
        this.newTorrentFile.setInfoHash((new CheckUtilities().generateSHA1Hash(a)));
    }

    void encode() {

    }

    public static void main(String args[]) throws Exception {

    }

    public void startExec() {
        this.readTorrentFile();
        Table.TableEntry ob=new Table.TableEntry(this.newTorrentFile.getName(),this.newTorrentFile.getLengthFull(),0.0,"Downloading","0","0","0","0","0","0","0","0","-","29/03/2018","-");
        Table.data.add(ob);
        this.th = new TrackerHandler();
        this.lh = new LsdHandler(this.newTorrentFile.getInfoHash());
        this.dh = new DhtHandler();
        this.ph = new PeerHandler(this.globalPeerList, this.newTorrentFile.getInfoHash(), this.peerId);
        //lh.exec();
        th.exec(this.newTorrentFile, this.peerId);
        //dh.exec(this.newTorrentFile, this.peerId);
        //ph.exec();
        this.newTorrentFile.printDetails();
    }
    public void stopAllHandlers()
    {
        this.dh.exit();
        this.lh.exit();
        this.ph.exit();
        this.th.exit();
    }
}
