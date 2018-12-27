/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 *
 * @author std07078
 *
*/


import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;

public class P2pGame implements Runnable{


    private ClientMIDlet parent;
    private Display display;
    private Thread t;

    //rival info//
    private String rivalIp = "";
    private String rivalSymbol = "";
    private String rivalTurn = "";

    //my Info//
    private String mySymbol;
     TextField status;

    // networking vars //
    Networking myNetworking; //sp ip, p ip

    //debug//
    String tempStr = "";
    private int[] A;

    //Displayables
    private Form gameForm;
    private Table myTable;


    //game variables//
    private boolean playing;
    private boolean mymove;
    private String gameStatus;
    private int rivalMove[];
    Chat myChat;
    /**
     * The constructor of class P2pGame
     * @param m Description Parent MIDlet
     * @param d Description Parent Display
     * @param n Description An instance of class Networking
     * @param c Description An instance of class Chat
     */
    P2pGame(ClientMIDlet m,Display d,Networking n,Chat c){
        parent = m;
        display = d;
        myNetworking = n;
        myChat=c;
        unsetInitiative();
        status = new TextField("Game Status", "", 15, TextField.PLAIN);
        //Initializing the displayables//
        gameForm = new Form("MultiPlayer");
        myTable = new Table("GameBoard",display,parent,this);
        gameForm.append(myTable);
        gameForm.append(status);
        playing=true;


        
        t = new Thread(this);
    }
    /**
     * Method start of runnable class P2pGame
     */
    public void start(){
        t.start();
    }
    /**
     * Extracts the parametres Ip,Symbol,Turn
     * @param Msg A condensed string of parametres with the separating character between them
     * @return The Parametres Ip,Symbol,Turn
     */
    public String[] parseRivalInfo(String Msg){

        String A[]= this.split(Msg);

        System.out.println("Rival ip "+A[0]);
        System.out.println("Symbol "+A[1]);
        System.out.println("Turn "+A[2]);

        rivalIp = A[0];
        rivalSymbol = A[1];
        rivalTurn = A[2];
        //we also initialize mySymbol variable here//
        if(A[1].equals("o")) mySymbol = "x";
        else mySymbol = "o";

        return A;
    }
    /**
     * Splits the given string by the character "#"
     * @param original The string to be split
     * @return Description An array of the strings that came out of the split procedure
     */
    private String[] split(String original) { //?????
        Vector nodes = new Vector();
        String separator = "#";
        //System.out.println("split start...................");
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while(index>=0) {
            nodes.addElement( original.substring(0, index) );
            original = original.substring(index+separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement( original );

        // Create splitted string array
        String[] result = new String[ nodes.size() ];
        if( nodes.size()>0 ) {
            for(int loop=0; loop<nodes.size(); loop++){
                result[loop] = (String)nodes.elementAt(loop);
                //System.out.println(result[loop]);
            }
        }

        return result;
    }
    /**
     * Accessor to the variable mymove
     * @return Description True if it is player's turn else false
     */
    public boolean iPlay(){ return mymove;}
    /**
     * Accessor to the variable mymove
     * @return Description True if it is player's turn else false
     */
    public boolean getInitiative(){return mymove;}
    /**
     * Mutator of variable mymove , sets mymove = true
     */
    public void setInitiative(){mymove=true;}
    /**
     * Mutator of variable mymove , sets mymove = false
     */
    public void unsetInitiative(){mymove=false;}

    /**
     * Method run of runnable class P2pGame
     */
    public void run(){
        //display 3liza
        System.out.println("paizw?");
        display.setCurrent(gameForm);
        playing = true;
       

        if(rivalTurn.equals("0")){


            /* connect to rival */
            int connected=0;
            int tries=0;
            while(connected==0)
            {
                System.out.println("Trying to connect to peer, attempt "+tries);
                tries++;
             connected=myNetworking.connectPeer(rivalIp, "4446");
            try{Thread.currentThread().sleep(1000);}
            catch(InterruptedException e)
            {}

            }
            System.out.println("Paizwww");

            /*connected */

            while(playing){
                status.setString("my turn");
                System.out.println("myturrn");

                  setInitiative();

                //we wait for the user to make his move//

                synchronized(this){
                        try{
                            this.wait();
                        }
                        catch(InterruptedException e){
                            System.out.println(e);
                        }
                }
                //we send the move//


                unsetInitiative();

                System.out.println("Stelnw ypotithetai "+String.valueOf(myTable.move[0])+String.valueOf(myTable.move[1]));
                myTable.updateTableMyMove(myTable.move[0],myTable.move[1]);
                myNetworking.sendBytes(myNetworking.osp, String.valueOf(myTable.move[0])+String.valueOf(myTable.move[1]));
                parent.getMySounds().playSound("moveMe"); 
//tin esteila//

               gameStatus = myTable.checkGameStatus();

                if(!gameStatus.equals("ONGOING"))
                {playing=false;
                    break;}
status.setString("rival's turn");
                System.out.print("waiting for rival mooove");

                tempStr = myNetworking.readBytes(myNetworking.isp);
                //parseRivalMove(tempStr);

               System.out.println("DiavasA "+tempStr);
               System.out.println("sdasjgheruhi");
               
                if(tempStr.equals("")){
                myNetworking.close();
                display.setCurrent(parent.getMainMenu());
                return;
            }
            char[] ch = tempStr.toCharArray();
          int a0=0;
          int a1=0;

            if(ch[0]=='0')
                a0=0;
           if(ch[0]=='1')
                a0=1;
           if(ch[0]=='2')
                a0=2;
             if(ch[1]=='0')
                a1=0;
           if(ch[1]=='1')
                a1=1;
           if(ch[1]=='2')
                a1=2;
System.out.println("a0: "+a0+" a1: "+a1);


              myTable.updateTableMove(a0,a1);
              parent.getMySounds().playSound("moveRival");
              
               gameStatus = myTable.checkGameStatus();

                if(!gameStatus.equals("ONGOING"))
                {playing=false;
                    break;}




            }



        }
        else {
            unsetInitiative();

                    System.out.println("1111");
            myNetworking.peerServer(rivalIp);
            System.out.println("edw ftanw arage?");

            if(myNetworking.isp==null){

                      System.out.println("to rival socket einai null, turn 1");
            }
status.setString("rivals turn");
         System.out.println("Perimenw na diavasw");

         //* RECEIVE RIVAL MOVE *//
         

            tempStr = myNetworking.readBytes(myNetworking.isp);  // kai kala kanei listen edw :)
            if(tempStr.equals("")){
                myNetworking.close();
                display.setCurrent(parent.getMainMenu());
                return;
            }
            
            System.out.println("DiavasA "+tempStr);
            

            char[] ch = tempStr.toCharArray();
          int a0=0;
          int a1=0;

            if(ch[0]=='0')
                a0=0;
           if(ch[0]=='1')
                a0=1;
           if(ch[0]=='2')
                a0=2;
             if(ch[1]=='0')
                a1=0;
           if(ch[1]=='1')
                a1=1;
           if(ch[1]=='2')
                a1=2;

              myTable.updateTableMove(a0,a1);
            gameStatus = myTable.checkGameStatus();

            if(!gameStatus.equals("ONGOING"))
                playing=false;

while(playing){
            
                      setInitiative();
status.setString("my turn");
                //we wait for the user to make his move//
                
                synchronized(this){
                        try{
                            this.wait();
                        }
                        catch(InterruptedException e){
                            System.out.println(e);
                        }
                }
                //we send the move//

                
                unsetInitiative();

               System.out.println("Stelnw 1 ypotithetai"+String.valueOf(myTable.move[0])+String.valueOf(myTable.move[1]));
               myTable.updateTableMyMove(myTable.move[0],myTable.move[1]);

                int a = myNetworking.sendBytes(myNetworking.osp, String.valueOf(myTable.move[0])+String.valueOf(myTable.move[1]));
                if(a == -1){
                    myNetworking.close();
                    display.setCurrent(parent.getMainMenu());
                    return;
                }
//tin esteila//
           
               gameStatus = myTable.checkGameStatus();
               
                if(!gameStatus.equals("ONGOING"))
                {playing=false;
                    break;}
             status.setString("rivals turn");
                System.out.print("waiting for rival mooove");

                tempStr = myNetworking.readBytes(myNetworking.isp);
                //parseRivalMove(tempStr);

               System.out.println("DiavasA "+tempStr);
               System.out.println("sdasjgheruhi");
               
                if(tempStr.equals("")){
                myNetworking.close();
                display.setCurrent(parent.getMainMenu());
                return;
            }

       
             ch = tempStr.toCharArray();
          a0=0;
           a1=0;

            if(ch[0]=='0')
                a0=0;
           if(ch[0]=='1')
                a0=1;
           if(ch[0]=='2')
                a0=2;
             if(ch[1]=='0')
                a1=0;
           if(ch[1]=='1')
                a1=1;
           if(ch[1]=='2')
                a1=2;


              myTable.updateTableMove(a0,a1);
               gameStatus = myTable.checkGameStatus();
               
                if(!gameStatus.equals("ONGOING"))
                {playing=false;
                    break;}

}

            //send game result to super peer :D

System.out.println("Sending result to sp :"+gameStatus);
int a=myNetworking.sendBytes(myNetworking.os,gameStatus);

             if(a==-1){
                myNetworking.close();
                display.setCurrent(parent.getMainMenu());
                return;
            }
        }
status.setString("game ended");


//Thread.currentThread().sleep(1000);
 Alert a = new Alert("Game Ended", "Result: "+gameStatus, null, AlertType.CONFIRMATION);
                a.setTimeout(Alert.FOREVER);
//clean up sokkits etc//
//alert etc//

    System.out.println("Game Thread Exited.");


    display.setCurrent(a,parent.getMainMenu());

    myNetworking.close();
    return;

    }
    /**
     * 
     * Accessor of variable gameForm
     * @return gameForm
     */
    public Form getGameForm(){return gameForm;}
    /**
     * Accessor of variable mySymbol
     * @return mySymbol
     */
    public String getMySymbol(){

        return mySymbol;
    }
    /**
     * Accessor of rivalSymbol
     * @return rivalSymbol
     */
    public String getRivalSymbol(){

        return rivalSymbol;
    }
    /**
     * Extracts rival move from the given string
     * @param m A condensed string of the move coordinates
     * @return an array of the coordinates
     */
    private int[] parseRivalMove(String m){
        int[] A={2,3};
        
        System.out.println("i r parsing");
        String [] result = this.split(m);//m.split("#");
        A[0] = Integer.parseInt(result[0]);
        A[1] = Integer.parseInt(result[1]);
        System.out.println("argh");
        return A;
        
    }



}

