/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author std07078
 */
import java.io.IOException;

import java.util.*;
import javax.microedition.lcdui.*;

public class ChangeImage extends TimerTask{
          int index = 2;
          ClientMIDlet parent;
          ChangeImage(ClientMIDlet m){
              parent = m;
              System.out.println("Scheduler here");
          }
          public void run(){
              parent.waiting.deleteAll();
              try{
                parent.waitingImage = Image.createImage("/images/reindeer"+String.valueOf(index)+".png");
              }catch(Exception e){}
              System.out.println("Scheduler here leme");
              parent.waiting.append(parent.waitingImage);
              String tempMes = "Please wait until a valid opponent is found";
              if(index == 0){tempMes+=".";}
              else if(index == 1){ tempMes+="..";}
              else {tempMes+="...";}
              parent.waiting.append(tempMes+"\nPress Back anytime to cancel connection");
              
              index = ++index%3;
              
              
              System.out.println("Scheduler here");
        }
      }