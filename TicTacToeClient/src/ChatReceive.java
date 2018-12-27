/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author std07078
 */
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class ChatReceive implements Runnable{

    private ClientMIDlet parent;
    private Display display;
    private Chat myChat;

    private String tempmsg="";
    private Networking myNetworking;
    private Thread t;
    /**
     * Constructor of class ChatReceive
     * @param m Description Parent MIDlet
     * @param d Description Parent Display
     * @param n An instance of class networking
     * @param c An instance of class Chat
     */
    ChatReceive(ClientMIDlet m,Display d,Networking n,Chat c){
        parent = m;
        display = d;
        myNetworking = n;
        myChat = c;
        Thread t = new Thread(this);
    }

    
    /**
     * Method run of runnable class ChatReceive
     */
    public void run(){

        while(true){
            
            System.out.println("Before reading chat");
            tempmsg = myNetworking.readBytes(myNetworking.isc);
            if(tempmsg==null)
            {   System.out.println("Network disconnected.");
                return;
            }
            System.out.println("Lets see what i received "+tempmsg);
            
            myChat.chatBox.setString(tempmsg);
            myChat.chatTextBox.insert(tempmsg, 0);
            
        }
    }
    /**
     * Method start of runnable class ChatReceive
     */
    public void start(){
        Thread t = new Thread(this);
        t.start();
    }
}
