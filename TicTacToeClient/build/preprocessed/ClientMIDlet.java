import javax.microedition.midlet.*;
//## change ##//
import javax.microedition.rms.*; /* for the Record Store */
//## end ##//
import java.util.*; /*for the Timer*/
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class ClientMIDlet extends MIDlet implements CommandListener {

   private static Display display;
   //private Form f;
   private boolean isPaused;
   //private StringItem si;
   //private TimeClient client;
   //private Item myItem;



   //commands 10.14//
   private static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
   private static final Command CMD_BACK = new Command("Back", Command.BACK, 1);
   private static final Command CMD_OK = new Command("Ok",Command.OK ,1);
   private static final Command CMD_CHAT = new Command("Chat",Command.ITEM ,1);
   //end of commands 10.14//

   private final String spMsg="";
   //items of 10.14//
   private List mainMenu;
   private Form singlePlayerF;
   public Form waiting;
   private Form multiPlayerF;
   //private Form waiting;
   private Form optionsF;
   private Form aboutF;
   private Ticker ticker;
   private Ticker wticker;
   private boolean firstTime;
   public Image menuImage;
  // private WaitScreen waitsc;
   //end of items 10.14//

  
   RecordStore rs;
   private Form ModifyRecordStore;
   private TextField usernameF;
   private TextField ipF;
   private TextField portF;
 


   private Table myTable;
 //     myTable = new Table("Game Board",display,this,null);

//   options//
   public Options myOptions;
   public ChoiceGroup optionsChoice;
   //end of table //


    //Networking stuff//
   public Networking myNetworking;

   //multiplayer//
   private TextField usernameTextF;
   private TextField ipTextF;
   private TextField portTextF;

   //chat//
   private Chat myChat;
   private ChatReceive myChatRcv;
   //chat//

   // p2p//
   private P2pGame myGame;
   //gameSounds//
   private GameSounds mySounds;
   //waiting screen//
   public Timer myTimer;
   public Image waitingImage;
   public ChangeImage myChangeImage;
   /**
    * The constructor of MIDlet ,all the essential variables of the forms
    * are set here
    */
   public ClientMIDlet() {

      wticker = new Ticker("waiting for game");
      display = Display.getDisplay(this);


      

      //table & Options 19.10//
      myOptions = new Options(this,true,true,true);
      optionsChoice = new ChoiceGroup("Toggle On/Off:",Choice.MULTIPLE);
      myTable = new Table("Game Board",display,this,null);



      //10.14 //

      singlePlayerF = new Form("single player");
      singlePlayerF.addCommand(CMD_BACK);
      singlePlayerF.append(myTable);
      singlePlayerF.setCommandListener(this);

      //## CAR ##//
      ModifyRecordStore = new Form("Modify Super Peer data");
      ModifyRecordStore.addCommand(CMD_BACK);
      ModifyRecordStore.addCommand(CMD_OK);

      usernameF = new TextField("Username", "lool", 15, TextField.PLAIN);
      ipF = new TextField("Super Peer IP", "", 15, TextField.PLAIN);
      portF = new TextField("Port", "", 15, TextField.PLAIN);
      ModifyRecordStore.append(usernameF);
      ModifyRecordStore.append(ipF);
      ModifyRecordStore.append(portF);
      ModifyRecordStore.setCommandListener(this);

    //## END ##//

      multiPlayerF = new Form("multi player");

      usernameTextF = new TextField("Username", "", 20, TextField.ANY);
      usernameTextF.setInitialInputMode("MIDP_UPPERCASE_LATIN");
      ipTextF = new TextField("Super Peer IP", "", 16, TextField.URL);
      portTextF = new TextField("Port", "", 15, TextField.ANY);
      multiPlayerF.append(usernameTextF);
      multiPlayerF.append(ipTextF);
      multiPlayerF.append(portTextF);

        //## END ##//

      //chat//
      myChat = new Chat(this,display,myNetworking);

      multiPlayerF.addCommand(CMD_OK);
      multiPlayerF.addCommand(CMD_BACK);
      multiPlayerF.addCommand(CMD_CHAT);
      multiPlayerF.setTicker(ticker);
      multiPlayerF.setCommandListener(this);


      //waiting Form //
      myTimer = new Timer();
      
      waiting = new Form("Waiting");
      
      waiting.setTicker(wticker);
      waiting.addCommand(CMD_BACK);
      waiting.setCommandListener(this);
      
      myChangeImage = new ChangeImage(this);
      myTimer.scheduleAtFixedRate(new ChangeImage(this), 1000,1000);


      optionsF = new Form("options");
      optionsF.addCommand(CMD_BACK);
      optionsF.append(optionsChoice);
      optionsChoice.append("Sound", null);
      optionsChoice.append("Music", null);
      optionsChoice.append("Vibration",null);
      
      optionsF.setTicker(ticker);
      optionsF.setCommandListener(this);

      aboutF = new Form("about");
      aboutF.append("Tic-tac-toe game created by Alexiou Caroline & Zorbas Dimitrios ,published under GPLv3 2010.");
      try {
                Image logo = Image.createImage("/images/gplv3logo.png");
                aboutF.append(logo);
            } catch (java.io.IOException err) {
                // ignore the image loading failure the application can recover.
            }
      //for the Animation//
       
       
       System.out.println("after scheduling");
      aboutF.setTicker(ticker);
      aboutF.addCommand(CMD_BACK);
      aboutF.setCommandListener(this);


      firstTime = true;

      mySounds = new GameSounds(myOptions);
      //end of  10.14//


   }
   /**
    * Method startApp of MIDlet at the first run the splash screen is displayed
    */
   public void startApp() {
      isPaused = false;

      if (firstTime) {
            // these are the images and strings for the choices.
            Image[] imageArray = null;

            try {
                // load the duke image to place in the image array
                Image icon = Image.createImage("/midp/uidemo/Icon.png");

                // these are the images and strings for the choices.
                imageArray = new Image[] { icon, icon, icon };
            } catch (java.io.IOException err) {
                // ignore the image loading failure the application can recover.
            }
            
            


        //LoadData();


            /* end Record Store loading */





            String[] stringArray = { "single player", "multiplayer", "options","about", "modify" };
            //##    end  ##//
            mainMenu = new List("Main Menu", Choice.IMPLICIT, stringArray, imageArray);
            ticker = new Ticker("TicTacToe Xmas Edition");
           
            mainMenu.setTicker(ticker);
            mainMenu.addCommand(CMD_EXIT);
            mainMenu.setCommandListener(this);
            new SplashScreen(display, mainMenu,"splash");
            //display.setCurrent(mainMenu);
            firstTime = false;
            mySounds.loadMusic();
            mySounds.startMusic();
            
       }




   }

   public void pauseApp() {
      isPaused = true;
   }

   public void destroyApp(boolean unconditional) {
   }
   /**
    * Open the recordstore and loads anything that has been stored
    */
   public void LoadData(){ /* Load Info from the RecordStore*/



                String str1 ="username";
                String str2 = "ip";
                String str3= "port";

        try{
                rs = RecordStore.openRecordStore("MyData",true);

                byte a[]=null;
                byte b[]=null;
                byte c[]=null;
                try{
                    a = rs.getRecord(1);
                    b = rs.getRecord(2);
                    c = rs.getRecord(3);

                }
                catch(RecordStoreException e){}


                if(a!=null)
                str1 = new String(a,0,a.length);
                if(b!=null)
                 str2 = new String(b,0,b.length);
                if(c!=null)
                str3 =new String(c,0,c.length);

                        System.out.println("AAA: "+str1);

                        try{
                            ipF.setString(str2);
                            usernameF.setString(str1);
                            portF.setString(str3);

                            usernameTextF.setString(str1);
                             ipTextF.setString(str2);
                            portTextF.setString(str3);
                        }
                        catch(NullPointerException e){
                           System.out.println(e);
                        }

                        }


                        catch(RecordStoreException e){}





            try{
            rs.closeRecordStore();}

            catch(RecordStoreException e){}




   }

   public void commandAction(Command c, Displayable d) {

       if (d.equals(mainMenu)) {
            // in the main list
            if (c == List.SELECT_COMMAND) {
                if (d.equals(mainMenu)) {
                    switch (((List)d).getSelectedIndex()) {
                    case 0:
                        mySounds.playSound("menuChoice");
                        display.setCurrent(singlePlayerF);

                        break;

                    case 1:{




         LoadData();
                        mySounds.playSound("menuChoice");
                        display.setCurrent(multiPlayerF);

                        //myNetworking.ConnectToSuperPeer();

                        break;}

                    case 2:
                        mySounds.playSound("menuChoice");
                        display.setCurrent(optionsF);

                        break;

                    case 3:
                        mySounds.playSound("menuChoice");
                        display.setCurrent(aboutF);

                        break;

                    case 4:{
                        mySounds.playSound("menuChoice");
                        try{
                            LoadData();
                            rs = RecordStore.openRecordStore("MyData",true);

                       // ipF.setString("klklklkl");
                        //portF.setString("adkjsksj");
                        //usernameF.setString("aaaa");
                        }


                        catch(RecordStoreException e){}


                        display.setCurrent(ModifyRecordStore);

                        break;}
                   
                    }
                }
            }
        }
       else {
            // in one of the sub-lists

           //if in options//
           if(d.equals(optionsF)){
               boolean selected[] = new boolean[optionsChoice.size()];
               optionsChoice.getSelectedFlags(selected);
               if(selected[0]){
                   myOptions.toggleSound();
               }
               if(selected[1]){
                   myOptions.toggleMusic();
               }
               if(selected[2]){
                   myOptions.toggleVibration();
               }
           }

           // CAR //
           if(d.equals(ModifyRecordStore)){

                if (c == CMD_BACK) {



                display.setCurrent(mainMenu);
                 try{
                rs.closeRecordStore();}
                catch(RecordStoreException e){}


                }

            

                if( c ==CMD_OK){
                //Update teh Record Store //
                boolean is_empty = false;

                 byte a1[]=null;
                byte b1[]=null;
                byte c1[]=null;
                try{
                    a1 = rs.getRecord(1);


                }
                catch(RecordStoreException e){}

                if(a1==null)
                    is_empty=true;





                String appt = usernameF.getString();
                byte bytes[] = appt.getBytes();
                String ip =ipF.getString();
                byte byteip[]= ip.getBytes();
                 byte byteport[]= portF.getString().getBytes();

                 if(!is_empty){
                try{
                rs.setRecord(1, bytes, 0, bytes.length);
                rs.setRecord(2, byteip, 0, byteip.length);
                 rs.setRecord(3, byteport, 0, byteport.length);


                }
                catch(RecordStoreException e){System.out.println("Den yparxoun reee");} /*ante */
                    }
 else{ //create the records for the first time
                     try{
                      rs.addRecord(bytes, 0, bytes.length);
                       rs.addRecord(byteip, 0, byteip.length);
                        rs.addRecord(byteport, 0, byteport.length);}


 catch(RecordStoreException e){System.out.println(e);}




 }





               Alert a = new Alert("RecordStore Manager", "RecordStore updated", null, AlertType.CONFIRMATION);
                a.setTimeout(Alert.FOREVER);

                display.setCurrent(a, mainMenu); //:D:D






           }
           }
         
           
 if(d.equals(waiting)){
            if(c==CMD_BACK){ /*If player gets tired of waiting, close the connection and exit to main menu */
                myNetworking.close();
                display.setCurrent(mainMenu);
            }
 }

           if(d.equals(multiPlayerF)){
               if(c==CMD_OK){
myNetworking = new Networking(this,display,ipTextF.getString(),usernameTextF.getString());

 //myNetworking.start();
                   //we need NEW thread //
 
                   myNetworking.getDeviceProfile();
                   myNetworking.deviceProfile = myNetworking.deviceProfile +usernameTextF.getString();
                   myNetworking.ConnectToSuperPeer(ipTextF.getString(), portTextF.getString());
                   myNetworking.sendBytes(myNetworking.os, myNetworking.deviceProfile);


   
                   String tempStr = "";


                 //  tempStr = myNetworking.readBytes(myNetworking.is);
                   display.setCurrent(waiting);
                      myChat = new Chat(this,display,myNetworking);
                      myGame = new P2pGame(this,display,myNetworking,myChat);
                    myNetworking.start(myChat,myGame); /* start the connection */
                                        /* and then the game in another thread*/
                                        /* so that the screen doesn't freeze*/
                //myChat= myNetworking.getChat();


                   

               }
               if(c==CMD_CHAT){

                   
                   display.setCurrent(new Alert("Error","Not Connected To Super Peer Yet!",null,AlertType.ERROR),multiPlayerF );
               }
           }

           if(d == myChat.chatForm){
               if(c == myChat.CMD_BACK){
                   System.out.println("BACK called!!");
                   display.setCurrent(myGame.getGameForm());
                  
               }
               if(c == myChat.CMD_SEND){
                    myChat.send();
               }
               if(c == myChat.CMD_CHAT_TEXT_BOX){
                   display.setCurrent(myChat.chatTextBox);}}

           if(d == myChat.chatTextBox){
                if(c == myChat.CMD_BACK){
                    display.setCurrent(myChat.chatForm);
                }
           }

           if (c == CMD_BACK) {
                display.setCurrent(mainMenu);
            }
        }

        if (c == CMD_EXIT) {
            destroyApp(false);
            notifyDestroyed();
        }
    }   
     /**
     * Accessor of variable mainMenu
     * @return mainMenu
     */
      public List getMainMenu(){
          return this.mainMenu;
      }
      public GameSounds getMySounds(){
            return this.mySounds;
      }
      private void loadwaitingImage(){
          try{
            waitingImage = Image.createImage("/images/reindeer1.png");
          }catch(Exception e){}
      }
      
      private void loadMenuImage2(){
          try{
            menuImage = Image.createImage("/images/reindeer2.png");
          }catch(Exception e){}
      }
   }
