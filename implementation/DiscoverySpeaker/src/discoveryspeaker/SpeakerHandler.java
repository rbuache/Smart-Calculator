/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package discoveryspeaker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael
 */
public class SpeakerHandler implements Runnable{

   private String name;
   private boolean privacy;
   private DatagramSocket socket;
   private Thread thread;
   
   public SpeakerHandler(String name,boolean privacy) throws ExceptionNameLenght{
      
         if(name.getBytes().length > 20)
            throw new ExceptionNameLenght();
         
         this.name = name;
         this.privacy = privacy;
         thread = new Thread(this);
         thread.start();
         
      try {
         socket = new DatagramSocket();
      } catch (SocketException ex) {
         Logger.getLogger(SpeakerHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void sendInfo(){
      byte[] buffer = new byte[DiscoveryProtocol.dataLenght];
      byte[] tmp = name.getBytes();
      System.arraycopy(tmp, 0, buffer, 0, tmp.length);
      buffer[DiscoveryProtocol.dataLenght-1] = (byte)(privacy?1:0);
      
      
      try {
         DatagramPacket p = new DatagramPacket(buffer, DiscoveryProtocol.dataLenght, InetAddress.getByName(DiscoveryProtocol.multicast), DiscoveryProtocol.portListener);
         socket.send(p);
         
      } catch (UnknownHostException ex) {
         Logger.getLogger(SpeakerHandler.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(SpeakerHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      long time = System.currentTimeMillis();
      System.out.println(time + " : sendInfo ok");
   }
   
   @Override
   public void run() {
      try {
         while(true){
            Thread.sleep(2000);
            sendInfo();
         }
      } catch (InterruptedException ex) {
         Logger.getLogger(SpeakerHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
   
}
