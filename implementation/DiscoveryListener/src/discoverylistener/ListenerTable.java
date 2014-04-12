/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package discoverylistener;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael
 */
public class ListenerTable implements Runnable{
   
   private LinkedList<TableEntry> table = new LinkedList<TableEntry>();
   private Thread thread;
   private static int maxTime = 10000; //durée max sans nouvelle 
   private static int refresh = 1000; //rafraichissement du tableau
   
   public ListenerTable(){
      thread = new Thread(this);
      thread.start();
   }
   
   public void updateTable(String name,boolean privacy,InetAddress ad){
      TableEntry tmp = new TableEntry(name,privacy,ad);
      boolean find = false;
      for(TableEntry entry : table){
         if(tmp.equals(entry)){
            entry.updateTime();
            find = true;
         }
         break;
      }
      if(!find){
         table.add(tmp);
      }
   }

   public LinkedList<TableEntry> getTable(){
      return table;
   }
   private void searchOld(){
      long actualTime = System.currentTimeMillis();
      
      for(TableEntry entry : table){
         if(actualTime-entry.lastReceive > maxTime){
            table.remove(entry);
         }
      }
   }
   
   @Override
   public void run() {
     while(true){
        try {
           Thread.sleep(refresh);
           searchOld();
           long actualTime = System.currentTimeMillis();
           System.out.println(actualTime + " : " + table);
           
        } catch (InterruptedException ex) {
           Logger.getLogger(ListenerTable.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
   }
   
   public class TableEntry{
      
      private String name;
      private boolean privacy;
      private long lastReceive;
      private InetAddress address;
      
      public TableEntry(String name,boolean privacy,InetAddress ad){
         this.name = name;
         this.privacy = privacy;
         this.address = ad;
         this.lastReceive = System.currentTimeMillis();
      }
      
      @Override
      public boolean equals(Object o){
         return o.getClass() == TableEntry.class && 
                 ((TableEntry)o).address.equals(this.address);
      }
      
      public void updateTime(){
         this.lastReceive = System.currentTimeMillis();
      }
      
      public String getName(){
         return name;
      }
      
      public InetAddress getAddress(){
         return address;
      }
      
      public boolean getPrivacy(){
         return privacy;
      }
      
      @Override
      public String toString(){
         return address.getHostAddress() + ":" + name + ":" + privacy;
         //return name + ":" + privacy;
      }
   }
}
