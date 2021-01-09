
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
public class PortService {
    static int startPort=6881;
    static int endPort=6889;
    static boolean[] isAcquired=new boolean[PortService.endPort-PortService.startPort+1]; 
    int getFreePort()
    {
       for(int i=0;i<PortService.isAcquired.length;i++)
       {
           if(!PortService.isAcquired[i])
           {
              PortService.isAcquired[i]=true;
              return (PortService.startPort+i);
           }
       }
     return -1;
    }
    void releaseAcquiredPort(int port)
    {
        if(port>=PortService.startPort && port<=PortService.endPort)
        {
        PortService.isAcquired[(port-PortService.startPort)]=false;
        }
    }
}
