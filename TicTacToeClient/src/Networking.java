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




public class Networking implements Runnable{

    private ClientMIDlet parent;
    private Display display;
    private String spmsg;
    private SocketConnection sc;
    public InputStream is;
    public OutputStream os;
    private String spAddress;
    private StringItem rcvMsg;
       private Chat myChat;
   private ChatReceive myChatRcv;
   //chat//

   // p2p//
   P2pGame myGame;

    //chat//
    private SocketConnection scc;
    public InputStream isc;
    public OutputStream osc;



    // p2p //
    private SocketConnection scp;
    public InputStream isp;
    public OutputStream osp;
    public ServerSocketConnection scps;
    public SocketConnection scpc;


    //debug form//
   // private Form waiting;
    private Form myDbgForm;
    private TextBox myTextBox;
String ip;
String username;

    public String deviceProfile="";


    /**
     * Constructor of class Networking
     * @param m Description Parent MIDlet 
     * @param d Description Parent Display
     */
    Networking(ClientMIDlet m,Display d,String s,String s2){
        parent = m;
        display = d;
        ip=s;
        username=s2;
    }


    /**
     * Accessor of variable myChat
     * @return 
     */
    public Chat getChat(){return myChat;}
    /**
    * Method run of runnable class Networking
    * 
    */
    public void run(){
    
        String tempStr= readBytes(is);
        System.out.println("SP Answer: "+tempStr);
                   if(tempStr.equals("Username exists")){
                  System.out.println("The username exists already");

                  Alert a = new Alert("Chat", "Please enter another username", null, AlertType.CONFIRMATION);
                a.setTimeout(Alert.FOREVER);
                  close();

                  display.setCurrent(a);
                  return;





                   }



                   //myChat = new Chat(parent,display,this);
                   myChat.connect(ip, "4445", username);
                  // myGame = new P2pGame(parent,display,this,myChat);
                   myGame.parseRivalInfo(tempStr);
                   myGame.start();
                   myChat.start();







                    return;
    }
    /**
     * Method start of runnable class Networking
     * @param c
     * @param p 
     */
     public void start(Chat c, P2pGame p){
         myChat=c;
         myGame=p;
        Thread t = new Thread(this);
        t.start();
    

    }
     /**
     * Method that closes all streams and sockets
     */
    public void close(){

        try{
    if(sc!=null)
        sc.close();
    if(scc!=null)
        scc.close();
    if(isc!=null)
    isc.close();
    if(osc!=null)
    osc.close();
    if(scp!=null)
    scp.close();
    if(scps!=null)
    scps.close();
    
        }
        catch(IOException e){}


    return;}
    /**
     * Connects MIDlet to Super Peer
     * @param superPeerIp Description Super Peer's Ip
     * @param superPeerPort Description Super Peer's Port
     */
    public void ConnectToSuperPeer(String superPeerIp,String superPeerPort){



        myDbgForm = new Form("The message i received is: ");

        try {
            sc = (SocketConnection)

            //na valw na dexetai super peer adress //
            Connector.open("socket://"+superPeerIp+":"+superPeerPort);
            is = sc.openInputStream();
            os = sc.openOutputStream();

            os.write("play\n".getBytes());




      } catch(IOException e) {

         Alert a = new Alert
            ("TimeClient", "Cannot connect to super peer. ", null, AlertType.ERROR);
         a.setTimeout(Alert.FOREVER);
         display.setCurrent(a);

      }


    }

    //## AYTI EDW I SYNARTISI ALLAKSE, EHEI ENA ORISMA KAI STELNEI KIOLAS ##//
    /**
     * Connects MIDlet to Super Peer Chat
     * @param superPeerIp Description Super Peers Ip
     * @param superPeerPort Description Super Peers Port
     * @param username Description Player's username
     */
    public void connectSuperPeerChat(String superPeerIp,String superPeerPort, String username){
         try {

              String un = username +"\n";
            scc = (SocketConnection)

            Connector.open("socket://"+superPeerIp+":"+superPeerPort); //tis theias sou! :S
            System.out.println("graou");
            isc = scc.openInputStream();
            osc = scc.openOutputStream();
             osc.write(un.getBytes());
             //wraia??



        } catch(IOException e) {

            e.printStackTrace();
            Alert a = new Alert
            ("Chat Connector", "Cannot connect to super peer chat. ", null, AlertType.ERROR);
            a.setTimeout(Alert.FOREVER);
            display.setCurrent(a);


      }

    }

    //## END OF CHANGES ##//


    /**
     * Listens for a connection of a valid rival
     * @param rival Description valid Rival Ip
     */
    public void peerServer(String rival){
                System.out.println("Peer Server");
                boolean success=false;

                while(!success){
        try{
            scps = (ServerSocketConnection) Connector.open("socket://:4446");
            System.out.println(scps);
            try{

            scpc = (SocketConnection) scps.acceptAndOpen();
          
            //IF not VALID

             if(!scpc.getAddress().equals(rival)){
                 /* den mporoume na paroume tis local dief8ynseis se kalo format */
                 System.out.println("Invalid Connection. Rejected!");
                 //scpc.close();
                 //continue;

             }

             else{
                System.out.println("Valid Connection accepted");}
            
           System.out.println("Accepted a connection from "+scpc.getAddress()+" vs.. "+rival); //APELPISIA
            success=true;
            scpc.setSocketOption(scpc.DELAY, 0);
            isp = scpc.openInputStream();
            osp = scpc.openOutputStream();}
            catch(NullPointerException n){
            System.out.println("NL PTR :"+n);
            }


        }
        catch(IOException e ){
            System.out.println(e);

        }
                }
    }



    /**
     * Connects to a peer
     * @param PeerIp Description Peer's Ip to connect to 
     * @param PeerPort Description Peer's Port to connect to 
     * @return Description On success 1 , else 0
     */
    public int connectPeer(String PeerIp,String PeerPort){

        try {
            System.out.println(PeerIp);
            System.out.println(PeerPort);
            try{
            scp = (SocketConnection)


            //na valw na dexetai super peer adress //
            Connector.open("socket://"+PeerIp+":"+PeerPort);}
            catch(NumberFormatException e){System.out.println("blah: "+e); return 0;}
            isp = scp.openInputStream();
            osp = scp.openOutputStream();


           return 1;



      } catch(IOException e) {
          System.out.println(e);

       
      return 0;}

    }
    /**
     * Gathers properties of the device needed for the game
     */
    public void getDeviceProfile(){


         deviceProfile="";
        if(display.isColor()) deviceProfile = deviceProfile+"color#";
        else deviceProfile = deviceProfile+"black#";

        deviceProfile = deviceProfile+(display.getCurrent().getWidth())+"#";
        deviceProfile = deviceProfile+(display.getCurrent().getHeight())+"#";



    }
    /**
     * Sends a string in bytes adding the terminating character "\n" 
     * @param os Description The stream to send the message through
     * @param Msg Description The message to be sent
     * @return Description On success 1 else -1
     */
    public int sendBytes(OutputStream os, String Msg){

        try{

            Msg = Msg+"\n"; /* terminating character   */

            os.write(Msg.getBytes());
            return 1;
        }
         catch(IOException e){
            System.out.println(e) ;
            return -1;


        }
    }
    /**
     * Reads a string in bytes until reaching the terminating character "\n"
     * @param is The stream to read from
     * @return Description The string that was read else null
     */
    public String readBytes(InputStream is){

      

        try{
            String rcvMsg;

            StringBuffer sb = new StringBuffer();
            System.out.print("Reading bytes...");
            int c = 0;
            while (((c = is.read()) != '\n') && (c != -1)) {
                sb.append((char) c);
            }

            rcvMsg= sb.toString();
            return rcvMsg;
        }
         catch(IOException e){
            System.out.println(e);
            return null;

        }




    }





}