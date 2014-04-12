/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package discoverylistener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael
 */
public class ListenerHandler implements Runnable{
   
   private MulticastSocket socket;
   private ListenerTable table;
   private Thread thread;
   
   public ListenerHandler(ListenerTable t) {
      table = t;
      
      try {
         socket = new MulticastSocket(DiscoveryProtocol.portListener);
         //socket.joinGroup(InetAddress.getByName(DiscoveryProtocol.multicast));
         socket.setBroadcast(true);
         
      } catch (SocketException ex) {
         Logger.getLogger(ListenerHandler.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(ListenerHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   
      thread = new Thread(this);
      thread.start();
   }

   private void newPaquet(DatagramPacket p){
      byte[] tmp = new byte[DiscoveryProtocol.dataLenght-1];
      
      String name = "";
      boolean privacy;
      
      System.arraycopy(p.getData(), 0, tmp, 0, tmp.length);
      try {
         name = new String(tmp, "UTF-8");
         name = name.trim();
         
//         while(name.charAt(name.lastIndexOf(0)) == 0){
//            name.substring(0, name.length()-1);
//         }
      } catch (UnsupportedEncodingException ex) {
         Logger.getLogger(ListenerHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      if(p.getData()[DiscoveryProtocol.dataLenght-1] == 0)
         privacy = false;
      else
         privacy = true;
      
      table.updateTable(name, privacy,p.getAddress());
   }
   @Override
   public void run() {
      while(true){
         byte[] data = new byte[DiscoveryProtocol.dataLenght];
         DatagramPacket p = new DatagramPacket(data,data.length);
         try {
            socket.receive(p);
            newPaquet(p);
            
         } catch (IOException ex) {
            Logger.getLogger(ListenerHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
   
  
}
