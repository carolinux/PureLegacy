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

public class ChatSend implements Runnable{

    private ClientMIDlet parent;
    private Display display;
    private Chat myChat;

    private String tempmsg="";
    private Networking myNetworking;
    private Thread t;

    /**
     * Constructor of class ChatSend
     * @param m Description Parent MIDlet
     * @param d Description Parent Display
     * @param n Description An instance of class Networking 
     * @param c Description An instance of class Chat
     */
    ChatSend(ClientMIDlet m,Display d,Networking n,Chat c){
        parent = m;
        display = d;
        myNetworking = n;
        myChat = c;
        Thread t = new Thread(this);


    }
    /**
     * Method run of runnable class ChatSend
     */
    public void run(){

     

        if(myChat.getMessage().equals("")||myChat.getMessage().equals(" ")||myChat.getMessage().equals("\n")){

                Alert a = new Alert("Chat", "Please enter a message", null, AlertType.CONFIRMATION);
                a.setTimeout(Alert.FOREVER);

                display.setCurrent(a);
        
        return;}

        int status=myNetworking.sendBytes(myNetworking.osc, myChat.getMessage());
        if(status==-1)
        {   System.out.println("Network disconnected.");
            return;}

       // System.out.println("chat send: I sent!");

    }
    /**
     * Method start of runnable class ChatSend
     */
    public void start(){
        Thread t = new Thread(this);
        t.start();
    }
}