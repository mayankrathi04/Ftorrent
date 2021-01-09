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
import java.util.List;
import java.util.ArrayList;

public class AllTorrentHandler {

    static List<TorrentFile> allTorents = new ArrayList<TorrentFile>();

    public static void addTorrent(TorrentFile newTf) {
        AllTorrentHandler.allTorents.add(newTf);
        newTf.startExec();
    }

    public static void loadTorrentsFromDatabase() {
        
    }

    public static void removeTorrentByName(String name) {
        TorrentFile tobeRemove;
        boolean found=false;
        for(TorrentFile ob:AllTorrentHandler.allTorents)
        {
            if(name.equals(ob.newTorrentFile.getName()))
            {
                ob.stopAllHandlers();
                tobeRemove=ob;
                found=true;
                break;
            }
        }
        if(found)
        {
            AllTorrentHandler.allTorents.remove(tobeRemove);
        }
    }
}
