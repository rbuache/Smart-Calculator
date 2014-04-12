/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package discoverylistener;

/**
 *
 * @author Raphael
 */
public class DiscoveryListener {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      
      ListenerTable table = new ListenerTable();
      new ListenerHandler(table);
   }
}
