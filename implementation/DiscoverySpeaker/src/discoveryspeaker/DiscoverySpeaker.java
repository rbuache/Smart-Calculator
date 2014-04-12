/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package discoveryspeaker;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael
 */
public class DiscoverySpeaker {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      
      try {
         new SpeakerHandler("ComputeEngine1",true);
      } catch (ExceptionNameLenght ex) {
         Logger.getLogger(DiscoverySpeaker.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
