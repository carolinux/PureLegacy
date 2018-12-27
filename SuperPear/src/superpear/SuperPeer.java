package superpear;

import java.io.*;
import java.net.*;
import java.util.*;/* for the timestamp */


//Zorbash:for the UI//
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * Η κλάση <code>SimpleServer</code> μοντελοποιεί ένα απλό εξυπηρετητή με χρήση TCP
 * sockets. Ο εξηπηρετητής "ακούει" για εισερχόμενα μηνύματα το περιεχόμενο των
 * οποίων εκτυπώνει στην οθόνη.
 *
 * @author UoA
 */
public class SuperPeer extends JFrame {

    //Ui stuff here//

    public static UserInterface myUi;
    public static CustomFrame myFrame;

    //  //

    public static Db gamedb; /*databse for the games and players */
    /* hmmm this should be accessible by all :( the threads! */







    public static void main(String[] args) throws IOException {

        //Zorbash:UI code here//

        myFrame = new CustomFrame();
        myFrame.setVisible(true);

        gamedb = new Db(myFrame);
        myFrame.setDb(gamedb);
        //show(new SuperPearFrame());

        System.out.println("Welcome To My First Java Program ");


        /* Statistics thread start */
             myFrame.ipLabel.setText("Super Peer public Ip:"+getExternalIp());
        myFrame.localIpLabel.setText("Super Peer local Ip:"+getLocalIp());

        Stats stats = new Stats(gamedb);
        System.out.println("Statistics Thread Started!");
        myFrame.logTextArea.append("Statistics Thread Started!\n");
        Thread statThread = new Thread(stats);
       statThread.start();

        /* Start Listening for Connections */

        ServerSocket serverSocket = null;
        try {
            // Δημιουργία ενος socket Εξυπηρετητή στην πόρτα 4444
            // και στην τοπική διεύθυνση.
            serverSocket = new ServerSocket(4444);
        }catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            myFrame.logTextArea.append("ERROR: Could not listen on port: 4444.\n");
            System.exit(1);
        }

        Socket clientSocket = null;

        /* start listening for chat */

        ChatAcceptor cs = new ChatAcceptor(myFrame); /*we start a server that listens :) */


        System.out.println("Chat Server started.");
        myFrame.logTextArea.append("Chat Server started.\n");
        Thread myThread1 = new Thread(cs);
        myThread1.start();

        /* Accept New Incoming Connections */

        while(true){
            try {
            // Αναμονή για σύνδεση πελάτη.
            clientSocket = serverSocket.accept();
            ClientHandler ch = new ClientHandler(clientSocket);


            System.out.println("Accepted connection!");
            myFrame.logTextArea.append("Accepted connection!\n");
            Thread myThread = new Thread(ch);
            myThread.start();


            } catch (IOException e) {
                System.err.println("Accept failed.");
                myFrame.logTextArea.append("ERROR: Accept failed.");
                System.exit(1);
            }

        } /*end of loop forever *////sdk 2.5.2


    }

    public static String readBytes(InputStream is){

        try{
        String rcvMsg;

        StringBuffer sb = new StringBuffer();
        System.out.print("Reading bytes...");
        myFrame.logTextArea.append("Reading bytes...\n");
            int c = 0;
            while (((c = is.read()) != '\n') && (c != -1)) {
                sb.append((char) c);
            }

            rcvMsg= sb.toString();
            return rcvMsg;
        }
         catch(IOException e){
            System.out.println (e);
            return null;

        }




    }

     public static String getExternalIp(){
        String ip="";
        try {
            java.net.URL url = new java.net.URL(
                    "http://whatismyip.com/automation/n09230945.asp");

            java.net.HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();

            java.io.InputStream stream = con.getInputStream();

            java.io.InputStreamReader reader = new java.io.InputStreamReader(
                    stream);

            java.io.BufferedReader bReader = new java.io.BufferedReader(reader);
            ip = bReader.readLine();

            return ip;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
    public static String getLocalIp(){
        /*
        String temp = "";
        try {
        Inet4Address addr = (Inet4Address) Inet4Address.

        // Get IP Address

        String ip = new String(addr.getHostAddress());
        temp = ip;
        // Get hostname
        //String hostname = addr.getHostName();


        } catch (UnknownHostException e) {
        }
        return temp;
         *
         */
        return "";
    }

    public static String getIp(Socket clientSocket){

         InetAddress clientInetAddress = clientSocket.getInetAddress();
        // System.out.println("IP: "+clientInetAddress.getHostAddress());
         String clientAddr = clientInetAddress.getHostName(); /* works */
         return clientAddr;
    }


       public static String parseProfil(String Msg){




       return Msg;}

        public static int sendBytes(OutputStream os, String Msg){

        try{

            Msg = Msg+"\n"; /* terminating character :D */

            os.write(Msg.getBytes());
            return 1;
        }
         catch(IOException e){
            System.out.println (e);

            return -1;
        }




    } /*end of function send bytes */






    public static String readSocket(BufferedReader in){ /* returns the string read from socket */

        try{

         String inputLine=in.readLine();
            //  String inputLine=in.

       /* while ((inputLine = in.readLine()) != null){
            System.out.println ("Received: " + inputLine);
            out.println(inputLine);
        }*/

         //in.close();
         return inputLine;}

        catch(IOException e){
            System.out.println (e);
            return null;

        }




    }

    public static String getTimeStamp(){


        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendar.get(Calendar.MINUTE);
        int seconds= calendar.get(Calendar.SECOND);

        String Tiem=""+hour+":"+minutes+":"+seconds;
        

        return Tiem;

    }



    public static void sendSocket(PrintWriter out,String Msg){


            out.println(Msg);




    } /*end of send socket function */






}


class ChatAcceptor implements Runnable{

    public static CustomFrame myFrame;

    public ChatAcceptor(CustomFrame f){
        myFrame = f;
    }

    public void run(){


      ServerSocket serverSocket = null;
      try {
            // Δημιουργία ενος socket Εξυπηρετητή στην πόρτα 4444
            // και στην τοπική διεύθυνση.
            serverSocket = new ServerSocket(4445);
      }catch (IOException e) {
            System.err.println("Chat Server could not listen on port: 4445.");
            myFrame.logTextArea.append("ERROR: Chat Server could not listen on port: 4445.\n");
            System.exit(1);
       }

        Socket ClientSocket = null;



        while(true){
            try {
                System.out.println("Waiting for chaaat");
                // Αναμονή για σύνδεση πελάτη FOR CHATTING :D.
                ClientSocket = serverSocket.accept();

                if(!SuperPeer.gamedb.chat_validate(ClientSocket)){
                    ClientSocket.close();
                    System.out.println("Rejected a connection from non-player");
                    myFrame.logTextArea.append("Rejected a connection from non-player\n");
                    continue;

                } /*end if */


              /*  int id= SuperPeer.gamedb.getIdFromSocket(ClientSocket);
                Player temp = SuperPeer.gamedb.getPlayer(id);
                temp.setChatSocket(ClientSocket);
                SuperPeer.gamedb.updatePlayer(temp);*/

            //find out the id //
              InputStream is =ClientSocket.getInputStream();
              String Msg = SuperPeer.readBytes(is);
              System.out.println("Username is: "+Msg);
              myFrame.logTextArea.append("Username is: "+Msg+"\n");


              ChatServer ch = new ChatServer(ClientSocket,Msg,myFrame);


              System.out.println("Accepted chat connection!");
              myFrame.logTextArea.append("Accepted chat connection!\n");
              Thread myThread = new Thread(ch);
              myThread.start();


        }

            catch (IOException e) {
            System.err.println("Accept failed.");
            myFrame.logTextArea.append("ERROR: Accept failed.\n");
            System.exit(1);
        }

System.out.println("must be reached!!!");

    } //end of loop forever



             } /* end of run */


}


class ChatServer implements Runnable{ /*we need two of them */
    Socket ClientSocket;
    String username;
    private CustomFrame myFrame;

public ChatServer(Socket mysock,String username_rcv,CustomFrame f){
    this.ClientSocket=mysock;
    this.username= username_rcv;
    this.myFrame = f;
}




public void run(){



            System.out.println("accessed once");


            int id= SuperPeer.gamedb.getIdFromUsername(username);
            Player temp = SuperPeer.gamedb.getPlayer(id);
            temp.setChatSocket(ClientSocket);
            SuperPeer.gamedb.updatePlayer(temp);
            System.out.println("My id is "+id +" and my socket is "+ClientSocket);
            myFrame.logTextArea.append("My id is "+id +" and my socket is "+ClientSocket+"\n");
            int rid = SuperPeer.gamedb.getRivalIdFromUsername(username);
            System.out.println("My rival id is "+rid );
            myFrame.logTextArea.append("My rival id is "+rid +"\n");
            Socket RivalSocket=null;


            while(RivalSocket==null){
                System.out.println("Attempting to find valid rival socket");
                myFrame.logTextArea.append("Attempting to find valid rival socket"+"\n");
                RivalSocket = SuperPeer.gamedb.getChatSocketFromId(rid);
                try{
                    SuperPeer.gamedb.print_players();
                    Thread.currentThread().sleep(1000);
                }
                catch(InterruptedException e){
                    //do nothing for the tiem being
                }

            }






              ChatClient ch = new ChatClient(ClientSocket,RivalSocket,temp.getId());


                System.out.println("Chatting should start now");
                Thread myThread = new Thread(ch);
                myThread.start();
                System.out.println("neeext");


        }


} /* end of class Chat Server */



class ChatClient implements Runnable{ /*we need two of them */


    Socket send;
    Socket rcv;
    int playerid;
    PrintWriter out = null;
    BufferedReader in = null;
    OutputStream os = null;
    InputStream is =null;

    public ChatClient(Socket rcv,Socket send,int id){


        this.rcv=rcv;
        this.send=send;
        this.playerid=id;




        try{
        /* socket set up */

        /* sending */
            try{
                os = send.getOutputStream();
            }catch(NullPointerException e){



            }
            out = new PrintWriter(os, true);

            /*receiving */

            is =rcv.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            in = new BufferedReader(isr);
            }
        catch(IOException e){

         System.err.println(e);
            System.exit(1);

        }
    }

    String format_message(String Msg, String Ip){

    /*msn style formatting :D */
    String Final=Msg+"("+SuperPeer.getTimeStamp()+")";


    return Final;
    }

    public void run() {

             /* start rcving and sending messages, after appending timestamp */


             while(true){
                String Msg = SuperPeer.readBytes(is);
                if(Msg==null || Msg.equals("")){
                System.out.println("Chat Disconnected");
                
                /* mark player as disconnected */
                SuperPeer.gamedb.disconnect_player(playerid);
                
                //is.close();
                return;

                }
                System.out.println ("Received:" + Msg+"+::::");
                //String Msg= "O HAI!!";
                String curr_time=SuperPeer.getTimeStamp();
                String peer_ip=SuperPeer.getIp(rcv);
                System.out.println ("Sending: " +Msg+" sent on: "+curr_time);
                String sent=format_message(Msg,peer_ip);

                int status=SuperPeer.sendBytes(os,sent);
                 if(status==-1){
                System.out.println("Chat Disconnected");
                //is.close();
                return;

                }
            }

    }

/*at some point, the sockets need to close though */
/* I will close them through main, I don't care */



}


class ClientHandler implements Runnable{

        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Player myplayer= null;
        OutputStream os = null;
        InputStream is =null;


    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;

                try{
        /* socket set up */

        /* sending */
        os = clientSocket.getOutputStream();
        out = new PrintWriter(os, true);

        /*receiving */

         is =clientSocket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        in = new BufferedReader(isr);
        }
        catch(IOException e){

         System.err.println(e);
            System.exit(1);

        }







    }

    String[] parse_profile(String Msg){

        String A[]= Msg.split("#");

        System.out.println("Color? "+A[0]);
        System.out.println("WIDTH: "+A[1]);
        System.out.println("HEIGHT: "+A[2]);
        System.out.println("USERNAME: "+A[3]);









    return A;}



    public void run() {
        int gameid=-1;



      /*   while(true){*/
        // Ένας πελάτης έχει συνδεθεί. Λήψη εισερχόμενων δεδομένων.

       System.out.println ("aaaaaaaaaaaaaa");


         InetAddress clientInetAddress = clientSocket.getInetAddress();
         String clientAddr = clientInetAddress.getHostName(); /* works */ //NOT LOCALHOST :s

         System.out.println ("I have a connection from " + clientAddr);

         if(clientInetAddress.isSiteLocalAddress()){
         System.out.println("Local Address : "+clientSocket.getLocalAddress());
         ///.....
         
         }
         
         String Msg = SuperPeer.readBytes(is);
        System.out.println ("Received: " + Msg);

         if(Msg.equalsIgnoreCase("play")){

          System.out.println ("I have a new playerz :D"); //CHECK IF REPLAYING!

          /* username, screensize, screencolors, buttons Mexe standard */

            String Msg1 = SuperPeer.readBytes(is);
            System.out.println ("Received 2: " + Msg1);

            /*Initialize Player */

            String PlayerData[] = parse_profile(Msg1);
            boolean color=false;
            if(PlayerData[0].equals("color"))
                color=true;

            int width = Integer.parseInt(PlayerData[1]);
            int height = Integer.parseInt(PlayerData[2]);
            //exc

            myplayer= new Player(-1,clientAddr, PlayerData[3],color, width, height);
            if(SuperPeer.gamedb.user_exists(myplayer)){
            System.out.println("Someone is replaying! :)");
            //set their status to playing
            myplayer = SuperPeer.gamedb.setReplaying(myplayer);
            //get updated myplayer...


            }else if(SuperPeer.gamedb.user_exists(myplayer)){
            System.out.println("User already exists -_-");
            SuperPeer.sendBytes(os,"Username exists");
            
            try{
     clientSocket.close();
}
catch(IOException e){}
            
            return;
            }


            else{
            int pid =SuperPeer.gamedb.increment_players(); //WHAT IF REPLAYING?? :/


            myplayer = new Player(pid,clientAddr, PlayerData[3],color, width, height);
         ///////////////////////////////////////////////////////////
          SuperPeer.gamedb.register_new_player(myplayer);}

          SuperPeer.gamedb.print_players(); /* debugging */

          /* Match with a rival */
         
          while(gameid==-1 ){
                gameid = SuperPeer.gamedb.find_rival(myplayer);
                try{
                Thread.sleep(1000); /* pause for a second so that other threads may access the gamedb */
                }
                catch(InterruptedException e){
                    //do nothing for the tiem being
                }

          }     System.out.println("sp:"+gameid);
            SuperPeer.gamedb.print_players();

           myplayer=SuperPeer.gamedb.getPlayer(myplayer.getId()); //get updated player from gamedb !
          
            String RivalInfo = myplayer.getRivalIp()+"#"+myplayer.getSymbol()+"#"+myplayer.getTurn();
            System.out.println("Sending Rival info");

            SuperPeer.sendBytes(os,RivalInfo);
            if(myplayer.getTurn()==1){

                  System.out.println("Will wait for game result.");

                String Result = SuperPeer.readBytes(is);
                if(Result!=null){

                    System.out.println("Gaem Result was "+Result);
                //update the gamedb (and teh playrs) wif statistics
                    System.out.println("spff:"+gameid);
                    SuperPeer.gamedb.updateDB(Result,myplayer.getId(),myplayer.getRivalId());
                    SuperPeer.gamedb.destroy_game(gameid);}

                else{
                    System.out.println("Game did not finish properly.");
                    SuperPeer.gamedb.destroy_game(gameid);}

                }

            }
                //destroy the game..hao?


         





         else{ //did not receive play - received some other sort of gibberish :[

               System.out.println ("Connection did not request to play");

            // exit!

         }
try{
     clientSocket.close();
}
catch(IOException e){}


        }
    }



    class Stats implements Runnable{ /*unused, telika */



    public Stats(Db gamedb){








        }









    public void run() {
        //read command
        //print stats




        }



    public void print_stats(){
    System.out.println("Statistics called");
    //say sth!
    System.out.println("  Id \t|IP\t\t|Games\t| W \t|  T\t|  L \t| Per\t|Name\t");
     Iterator vItr = SuperPeer.gamedb.players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                System.out.println(temp.getId());
                System.out.println(temp.getIp());
                System.out.println(temp.getGames());
                System.out.println(temp.getWins());
                System.out.println(temp.getTies());
                System.out.println(temp.getLosses());
                System.out.println(temp.getPercentage());
                System.out.println(temp.getUsername());





    }
     System.out.println("Ongoing Games");
        Iterator vItr2 = SuperPeer.gamedb.games.iterator();
            while(vItr2.hasNext()){
                Game temp = (Game) vItr2.next();
                   System.out.println("Game "+temp.getId()+": Peer "+temp.getPlayer1()+" vs Peer "+temp.getPlayer2());

            }


    }

    }
