/**
 *
 * @author std07078
 */
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;



public class Chat implements Runnable{

    public Form chatForm;
    public TextField chatBox;
    private TextField myMessageF;
    public TextBox chatTextBox;
    public static final Command CMD_BACK = new Command("Back", Command.BACK, 1);
    public static final Command CMD_SEND = new Command("Send",Command.OK ,1);
    public static final Command CMD_CHAT_TEXT_BOX = new Command("Chat Log",Command.OK ,1);


    private ClientMIDlet parent;
    private Display display;
    private Networking myNetworking;

    private Thread t;
    ChatReceive myChatReceive;
    ChatSend myChatSend;
    /**
     * The constructor of class Chat
     * @param m Parent MIDlet 
     * @param d Parent Display
     * @param n An instance of class Networking
     */
    Chat (ClientMIDlet m,Display d,Networking n){
        parent = m;
        display = d;
        myNetworking = n;
        chatBox = new TextField("Rival Said:","",150,TextField.ANY);
        chatBox.setString("Nothing at the moment");
        chatBox.setLayout(TextField.LAYOUT_TOP);
        myMessageF = new TextField("Enter Message ","",50,TextField.ANY);

        chatTextBox = new TextBox("Chat Log","",1000,TextField.ANY);
        chatTextBox.addCommand(CMD_BACK);
        chatTextBox.setCommandListener(m);
        chatForm = new Form("Chat",null);
        chatForm.append(chatBox);
        chatForm.append(myMessageF);
        chatForm.addCommand(CMD_BACK);
        chatForm.addCommand(CMD_SEND);
        chatForm.addCommand(CMD_CHAT_TEXT_BOX);
        chatForm.setCommandListener(m);

        //thread & io classes//
        t = new Thread(this);
        myChatReceive = new ChatReceive(parent,display,myNetworking,this);
        myChatSend = new ChatSend(parent,display,myNetworking,this);
        

    }


    /**
     * The run method of runnable class Chat
     */
    public void run(){
        myChatReceive.start();
        
    }
    /**
     * The start method of runnable class Chat
     */
    public void start(){
        t.start();
    }
    /**
     * Attempts to create a connection to the chat server of super peer
     * @param superPeerIp Description  Super Peer's Ip
     * @param superPeerPort Description Super Peer's Port
     * @param username  Description Player's username
     */
    public void connect(String superPeerIp,String superPeerPort,String username){
        myNetworking.connectSuperPeerChat(superPeerIp, superPeerPort,username);
    }
    /**
     * Sends to the chat server the contents of myMessageF textfield , the then sets it as Blank
     */
    public void send(){
        myChatSend.run();
        myMessageF.setString("");
    }
    /**
     * Accessor to the contents of textfield myMessageF
     * @return Description the string contents of myMessage
     */
    public String getMessage(){
        return myMessageF.getString();
    }


    

    


}
