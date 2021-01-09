/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshtorrent;
/**
 *
 * @author root
 */
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.UnknownHostException;
import java.util.Enumeration;
public class CheckUtilities {

    boolean isUrl(String a) {
        return true;
    }

    boolean isValidEncoding(String a) {
        return true;
    }

    boolean isDate(String a) {
        return true;
    }

    public void copyBytes(byte[] a, byte[] b, int starta, int startb,int bytes) {
        for (int i = starta,j=startb; i <=(starta+bytes-1); i++,j++) {
            b[j] = a[i];
        }
    }

    public byte[] getByte(byte[] a, int start, int end) {
        byte[] aa = new byte[end - start + 1];
        int i = 0;
        for (int j = start; j <= end; j++) {
            aa[i] = a[j];
            i++;
        }
        return aa;
    }

    public boolean checkNullArray(byte[] arr) {
        boolean empty = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                empty = false;
                break;
            }

        }
        return empty;
    }

    public int readInt(byte a[], int start) {
        ByteBuffer in;
        byte inb[] = new byte[4];
        int b = 0;
        for (int i = start, j = 0; i <= start + 3; i++, j++) {
            inb[j] = a[i];
        }
        in = ByteBuffer.wrap(inb);
        b = in.getInt();
        return b;
    }

    public long readLong(byte a[], int start) {
        ByteBuffer in;
        byte inb[] = new byte[8];
        long b = 0;
        for (int i = start, j = 0; i <= start + 7; i++, j++) {
            inb[j] = a[i];
        }
        in = ByteBuffer.wrap(inb);
        b = in.getLong();
        return b;
    }

    public short readShort(byte a[], int start) {
        ByteBuffer in;
        byte inb[] = new byte[2];
        short b = 0;
        for (int i = start, j = 0; i <= start + 1; i++, j++) {
            inb[j] = a[i];
        }
        in = ByteBuffer.wrap(inb);
        b = in.getShort();
        return b;
    }

    public void putInt(byte a[], int start, int data) {
        byte[] temp = new byte[4];
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(data);
        temp = bb.array();
        for (int i = start, j = 0; i <= start + 3; i++, j++) {
            a[i] = temp[j];
        }
    }

    public void putLong(byte a[], int start, long data) {
        byte[] temp = new byte[8];
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(data);
        temp = bb.array();
        for (int i = start, j = 0; i <= start + 7; i++, j++) {
            a[i] = temp[j];
        }
    }

    public void putShort(byte a[], int start, short data) {
        byte[] temp = new byte[2];
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(data);
        temp = bb.array();
        for (int i = start, j = 0; i <= start + 1; i++, j++) {
            a[i] = temp[j];
        }
    }

    public void putString(byte a[], int start, String data, int bytes) {
        byte[] temp = new byte[bytes];
        temp = data.getBytes();
        for (int i = start, j = 0; i <= start + bytes-1; i++, j++) {
            a[i] = temp[j];
        }
    }

    byte[] generateSHA1Hash(byte[] bytes) {
        try {
            byte[] hash = new byte[20];
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            hash = sha.digest(bytes);

            return hash;
        } catch (NoSuchAlgorithmException e) {
            System.err
                    .println("Error: [TorrentFileHandler.java] \"SHA-1\" is not a valid algorithm name.");
            System.exit(1);
        }
        return null;
    }

    String byteArrayToByteString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0) {
            return null;
        }

        String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};
        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble
            ch = (byte) (ch >>> 4); // shift the bits down
            ch = (byte) (ch & 0x0F); // must do this is high order bit is on!
            out.append(pseudo[(int) ch]); // convert the nibble to a String
            // Character
            ch = (byte) (in[i] & 0x0F); // Strip off low nibble
            out.append(pseudo[(int) ch]); // convert the nibble to a String
            // Character
            i++;
        }

        String rslt = new String(out);

        return rslt;
    }

    String byteArrayToURLString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0) {
            return null;
        }

        String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};
        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length) {
            // First check to see if we need ASCII or HEX
            if ((in[i] >= '0' && in[i] <= '9')
                    || (in[i] >= 'a' && in[i] <= 'z')
                    || (in[i] >= 'A' && in[i] <= 'Z') || in[i] == '$'
                    || in[i] == '-' || in[i] == '_' || in[i] == '.'
                    || in[i] == '+' || in[i] == '!') {
                out.append((char) in[i]);
                i++;
            } else {
                out.append('%');
                ch = (byte) (in[i] & 0xF0); // Strip off high nibble
                ch = (byte) (ch >>> 4); // shift the bits down
                ch = (byte) (ch & 0x0F); // must do this is high order bit is
                // on!
                out.append(pseudo[(int) ch]); // convert the nibble to a
                // String Character
                ch = (byte) (in[i] & 0x0F); // Strip off low nibble
                out.append(pseudo[(int) ch]); // convert the nibble to a
                // String Character
                i++;
            }
        }

        String rslt = new String(out);

        return rslt;

    }

    public String getSystemIp() {
        String currentHostIpAddress = "";
        if (currentHostIpAddress == "") {
            Enumeration<NetworkInterface> netInterfaces = null;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();

                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
                        //                      log.debug("Inetaddress:" + addr.getHostAddress() + " loop? " + addr.isLoopbackAddress() + " local? "
                        //                            + addr.isSiteLocalAddress());
                        if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                                && !(addr.getHostAddress().indexOf(":") > -1)) {
                            currentHostIpAddress = addr.getHostAddress();
                        }
                    }
                }
                if (currentHostIpAddress == null) {
                    currentHostIpAddress = "127.0.0.1";
                }

            } catch (SocketException e) {
//                log.error("Somehow we have a socket error acquiring the host IP... Using loopback instead...");
                currentHostIpAddress = "127.0.0.1";
            }
        }
        return currentHostIpAddress;
    }

    int getfreePort() {
        int port =6885;
        return port;
    }

    public long convertIpFromStringToLong(String address) {
        long result = 0;
        int i=3;
        for (String part : address.split(Pattern.quote("."))) {
            long p=(((long)Math.pow(256, i))*(Long.parseLong(part)));
           result=result+p;
           i--;
        }
        return result;

    }
    public byte[] convertIpFromStringToByteArray(String ip)
    {
       long iplong=this.convertIpFromStringToLong(ip);
       byte[] ipp=new byte[8];
       this.putLong(ipp, 0, iplong);
       byte[] ipByte=new byte[4];
       this.copyBytes(ipp, ipByte, 4, 0, 4);
       return ipByte;
    }
    public String convertIpFromByteArrayToString(byte[] ip)
    {
        String ipadd="";
        byte temp[]=new byte[8];
        this.copyBytes(ip, temp, 0, 4,4);
        long aa=this.readLong(temp, 0);
        ipadd=this.convertIpFromLongToString(aa);
        return ipadd;
    }
     public int convertPortFromByteArrayToInt(byte[] p)
    {
        int port=0;
        byte[] temp=new byte[4];
        temp[2]=p[0];
        temp[3]=p[1];
        port=this.readInt(temp,0);
        return port;
    }
    public byte[] convertPortFromIntToByteArray(int port)
    {
       byte[] ipp=new byte[4];
       this.putInt(ipp, 0, port);
       byte[] portByte=new byte[2];
       this.copyBytes(ipp, portByte, 2, 0, 2);
       return portByte;
    }
    public String convertIpFromLongToString(long ip)
    {
        String ipadd="";
        for(int i=1;i<=4;i++)
        {
            long b=ip%256;
            ip=ip/256;
            if(i!=1)
            ipadd=b+"."+ipadd;
            else
            ipadd=b+ipadd;    
        }
        return ipadd;
    }
    public String hexStringToUtf8(String hexString)
    {
       
        return("");
    }
    public String readString(byte[] data,int start,int bytes)
    {
        String dat="";
        byte[] temp=new byte[bytes];
        (new CheckUtilities()).copyBytes(data, temp, start,0, bytes);
        dat=(new CheckUtilities()).byteArrayToByteString(temp);
        return dat;
    }
    public boolean areSameByteArray(byte[] a,byte[] b)
    {
        if(a.length!=b.length)
            return false;
        for(int i=0;i<a.length;i++)
        {
            if(a[i]!=b[i])
                return false;
        }
        return true;
    }
    public static void main(String args[]) {
        String x = (new CheckUtilities()).getSystemIp();
        System.out.println(x);
        System.out.println((new CheckUtilities()).convertIpFromStringToLong(x));
    }
    
}
