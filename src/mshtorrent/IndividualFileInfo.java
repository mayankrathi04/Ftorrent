
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
public class IndividualFileInfo {
    private long fileLength;
    private String path;
    private String md5sum;
    IndividualFileInfo(int fileLength,String path)
    {
        this.fileLength=fileLength;
        this.path=path;
    }
    IndividualFileInfo(){}
    long getLength()
    {
        return this.fileLength;
    }
    String getPath()
    {
        return this.path;
    }
    String getMd5sum()
    {
        return this.md5sum;
    }
    boolean setLength(long a)
    {
        this.fileLength=a;
        return true;     
    }
    boolean setPath(String a)
    {
        this.path=a;
        return true;
    }
    boolean setMd5sum(String a)
    {
        this.md5sum=a;
        return true;
    }
}
