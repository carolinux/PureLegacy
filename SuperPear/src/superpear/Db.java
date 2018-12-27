/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package superpear;
import java.util.*;
import java.net.*;
/**
 *
 * @author caroline
 */


public class Db{   /* Game&Player Database */

        Vector<Player> players;
        Vector<Game> games;
        int player_count=0; //* IDS *
        int total_game_count=0;
        private CustomFrame myFrame;


        Db(CustomFrame f){


        players = new Vector(100,0);
        games = new Vector(100,0);
        myFrame = f;

        };

        public void disconnect_player(int pid){


                   Iterator vItr = players.iterator();
        while(vItr.hasNext()){
            Player temp = (Player) vItr.next();
            if(temp.getId()==pid){
                temp.setStatus("Disconnected");
                updatePlayer(temp);



            }
        }




        }

            public void reap(){
        Iterator vItr = players.iterator();
        while(vItr.hasNext()){
            Player temp = (Player) vItr.next();
            if(temp.status == "Disconnected"){
                try{
              //      temp.ChatSocket.close();
                //    temp.ClientSocket.close();
                }
                catch(Exception e){
                    System.out.println(e);
                }
                vItr.remove();
                myFrame.playerList.setListData(players);
                //will have to close the sockets here//



            }
        }


}
        public synchronized int increment_games(){
         ++total_game_count;
         System.out.println("synch:"+total_game_count);
        return total_game_count;
        }

    public synchronized int increment_players() {
        ++player_count;
        return player_count;
    }

public boolean isAlreadyPlaying(int pid){

     Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();

                if(temp.getId()==pid && temp.isPlaying()){
                 return true;


                }
                   }





return false;}

public void updatePlayer(Player myplayer){

    int pid = myplayer.getId();
    int i=0;
     Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getId()==pid){
                   //update the vector.. hao
                  players.set(i, temp);


                }
                i++;
                   }




}

 public void destroy_game(int gameid){ /* delete a game from the database */

       Iterator vItr = games.iterator();
            while(vItr.hasNext()){
                Game temp = (Game) vItr.next();

                System.out.println("A: "+temp.getId()+" vs "+gameid);
                if(temp.getId()==gameid){
                    System.out.println("destroyed a game:D");
                    vItr.remove();}



            }
     }

 public int find_gameid(int playerid){


     Iterator vItr = games.iterator();
            while(vItr.hasNext()){
                Game temp = (Game) vItr.next();


                if(temp.getPlayer1()==playerid || temp.getPlayer2()==playerid){
                    
                    return temp.getId();}



            }


 return -1;

 }

 public Game getGame(int gameid)
 {

    Iterator vItr = games.iterator();
            while(vItr.hasNext()){
                Game temp = (Game) vItr.next();
                if(temp.getId()==gameid)
                    return temp;


            }


 return null;
 }

    public synchronized int find_rival(Player myplayer){

            if(isAlreadyPlaying(myplayer.getId())){


                System.out.println("Match already set");

           return find_gameid(myplayer.getId()); } //which means, we already have a rival //meh

            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getId()!= myplayer.getId() && temp.isWaiting() && myplayer.match_with_device(temp) ){
                   System.out.println("Match found");
                    /*select player turn at random */
                    Random generator = new Random();
                    int numbar = generator.nextInt(2);
                    if(numbar==0){
                        myplayer.setTurn(1);
                        myplayer.setSymbol('x');
                    }
                    else
                    {   temp.setTurn(1);
                        temp.setSymbol('x');}
                    
                    /* update the status of both players */
                    myplayer.setRivalId(temp.getId());
                    myplayer.setRivalIp(temp.getIp());
                    myplayer.setStatus("Playing");
                    temp.setRivalId(myplayer.getId());
                    temp.setRivalIp(myplayer.getIp());
                    temp.setStatus("Playing");

                    /* update the vector in the database */

                   updatePlayer(temp);
                   updatePlayer(myplayer);

                   /* make a new game */
                   Game mygame = new Game(myplayer.getId(),temp.getId());
                   games.add(mygame);


                    return mygame.getId();

                }

                   }
          //we did not find a match :[
            System.out.println("No rival found");
            return -1;
  }

public void updateDB(String Result,int Playerid,int Rivalid){ /* after a game has ended.. */
    
    int win=0;
    int tie=0;
    int loss=0;
    if(Result.equalsIgnoreCase("WIN"))
        win=1;
     if(Result.equalsIgnoreCase("TIE"))
        tie=1;
      if(Result.equalsIgnoreCase("LOSS"))
        loss=1;

    Player p = getPlayer(Playerid);
    Player r = getPlayer(Rivalid);
    if(win==1){

    p.assign_win();
    r.assign_loss();
    }

    if(loss==1){
    r.assign_win();
    p.assign_loss();

    }
    if(tie==1){
    p.assign_tie();
    r.assign_tie();}

   r.setSymbol('o');
   r.setTurn(0);
   p.setSymbol('o');
   p.setTurn(0);
   r.setStatus("Disconnected");
   p.setStatus("Disconnected");
   r.setChatSocket(null);
   p.setChatSocket(null);
   r.setRivalId(0);
   r.setRivalIp("");
   p.setRivalId(0);
   p.setRivalIp("");

   updatePlayer(p);
   updatePlayer(r);

return;}





        public void create_new_game(){


            /* find a rival */
            /* create the game */

            Game mygame;

        
            //games.add(mygame);


        }

       public int getIdFromUsername(String username){

          // String myip= SuperPeer.getIp(sock);
            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getUsername().equalsIgnoreCase(username)){
                   return temp.getId();
                }
                   }
  return -1;}


public int getRivalIdFromSocket(Socket sock){

           String myip= SuperPeer.getIp(sock);
            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getIp().equalsIgnoreCase(myip)){
                   return temp.getRivalId();
                }
                   }
  return -1;}

public Player setReplaying(Player myplayer){

    Iterator vItr = players.iterator();

            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getUsername().equals(myplayer.getUsername()) && temp.getIp().equals(myplayer.getIp())){
                    temp.setStatus("Waiting");
                    updatePlayer(temp);
                    return temp;
                } /*bwahaha */



            }


            return null;
}

public int getRivalIdFromUsername(String username){

           //String myip= SuperPeer.getIp(sock);
            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getUsername().equalsIgnoreCase(username)){
                   return temp.getRivalId();
                }
                   }
  return -1;}


public int getIdFromSocket(Socket sock){

           String myip= SuperPeer.getIp(sock);
            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getIp().equalsIgnoreCase(myip)){
                   return temp.getId();
                }
                   }
  return -1;}


public Socket getChatSocketFromId(int pid){



            Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getId()==pid){
                   return temp.getChatSocket();
                }
                   }
  return null;




}






        public boolean chat_validate(Socket sock){

            int pid=getIdFromSocket(sock);
            if(pid==-1){
                System.out.println("Player does not exist.");
                /*ip does not correspond to an existing player */
                return false;
            }

                Iterator vItr = players.iterator();
              while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getId()==pid){
                  if(temp.getStatus().equalsIgnoreCase("Playing")){
                      return true;

                  }
                  else{
                      return false; /* player is not currently playing */
                  }


                }
                   }

                return false;}

        


        public void print_players(){


               Iterator vItr = players.iterator();
            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                temp.print();


            }



        }


        public int register_new_player(Player myplayer){

            if(user_exists(myplayer)) /* they are replaying.. let them */
            {                         /* change their status to 'waiting' */
                System.out.println("someone is replaying!");
                Player temp= getPlayer(myplayer.getId());
                temp.setStatus("Waiting"); 

                return 1;
            }
            if(username_exists(myplayer)) /*reject them */
            {   return 0;
            }

  //increment_players(); //synchronized... but players may still get the same id..
            players.add(myplayer);
            myFrame.playerList.setListData(players);

        return 1;}
        
        
        
           public boolean user_exists(Player myplayer){
               
                       Iterator vItr = players.iterator();

            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getUsername().equals(myplayer.getUsername()) && temp.getIp().equals(myplayer.getIp())){
                    return true;} /*bwahaha */
                
                
                
            }
               
               
           
           return false;
           }


           public boolean username_exists(Player myplayer){


               Iterator vItr = players.iterator();

            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();
                if(temp.getUsername().equals(myplayer.getUsername())){
                    return true;} /*bwahaha */



            }






    return false;
    }



        public int getCount(){

        return player_count;



        }

        void assign_win(int playerid){
          Player myplayer = getPlayer(playerid);
          if(myplayer==null){

          System.out.println("Invalid id specified");
          return;
          }

          myplayer.assign_win();
          return;
        }


              void assign_loss(int playerid){
          Player myplayer = getPlayer(playerid);
          if(myplayer==null){

          System.out.println("Invalid id specified");
          return;
          }

          myplayer.assign_loss();
          return;
        }


                    void assign_tie(int playerid){
          Player myplayer = getPlayer(playerid);
          if(myplayer==null){

          System.out.println("Invalid id specified");
          return;
          }

          myplayer.assign_tie();
          return;
        }




        Player getPlayer(int playerid){ /*return the player with given id */

            Iterator vItr = players.iterator();

            while(vItr.hasNext()){
                Player temp = (Player) vItr.next();

            if(temp.getId()==playerid){
                return temp;

            }


            }

            return null;

        }

       /*

        int register_new_player(string ,int,string);
        int create_new_game(int id);
        int find_player_by_socket(int socket);
	void delete_game(int id);
	void assign_win(int);
	void assign_tie(int);
	void assign_loss(int);
	int get_my_rival(int id);
	int get_my_game(int id);
        vector<Player> players;
        vector<Game> games;
        */




}

class Player{

     public enum Status {WAITING,PLAYING,DISCONNECTED;}



   // enum status_enum{Waiting , Playing, Disconnected};
    private String ip="";
    private String username="";
    int id;
    int turn=0;
    int ties,wins,losses,games;
    int rivalId;
    String rivalIp;
    String status;
    boolean color;
    int height;
    int width;
    Socket ChatSocket=null;
    char symbol='o';


    public Player(int id,String ip, String username, boolean color, int height, int width){ /* initialize as waiting */
        this.id=id;
        this.ip=ip;
        this.color=color;
        this.height=height;
        this.width=width;
        this.username=username;
        ties=0;
        wins=0;
        losses=0;
        games=0;
        status="Waiting";




    }


    /* device data */
    public int getRivalId(){return this.rivalId;}
    public Socket getChatSocket(){return this.ChatSocket;}
    public int getId(){ return this.id; }
    public String getUsername(){return this.username;}
    public String getIp(){return this.ip;}
    public String getRivalIp(){return this.rivalIp;}
    public void setRivalId(int rid){this.rivalId=rid;}
    public void setRivalIp(String rip){this.rivalIp=rip;}

    public void assign_win(){games++;wins++;}
    public void assign_loss(){games++;losses++;}
    public void assign_tie(){games++;ties++;}
    public String getStatus(){return status;}
    public int getWins(){return wins;}
    public int getTies(){return ties;}
    public int getLosses(){return losses;}
    public int getGames(){return games;}
    public double getPercentage(){
         if(games==0) return 0;

    else return  100 * (double)wins/(double) games;



    }
    public void setTurn(int turn){this.turn=turn;}
    public int getTurn(){return this.turn;}
    public void setChatSocket(Socket sock){this.ChatSocket = sock;}
    public void setStatus(String newstatus){status=newstatus;}
    public void setSymbol(char x){this.symbol=x;}
    public char getSymbol(){return this.symbol;}
    public void assign_rival(int rid, String rip){

        rivalId=rid;
        rivalIp=rip;

    }



   public void print(){

   System.out.println("Id : "+ id);
   System.out.println("IP : "+ ip);
   System.out.println("Username : "+ username);
   System.out.println("Status : "+ status);
   System.out.println("RivalId : "+ rivalId );
   System.out.println("Chat Socket : "+ ChatSocket );

   return;
   }


   public int getWidth(){return width;}
   public int getHeight(){return height;}
   public boolean getColor(){return color;}
  

    public boolean match_with_device(Player otherplayer){

    if(this.width!=otherplayer.getWidth())
        return false;

    if(this.height!=otherplayer.getHeight())
        return false;
    if(this.color!=otherplayer.getColor())
        return false;



    return true;}


    public boolean isWaiting(){
    if(status.equalsIgnoreCase("Waiting"))
        return true;
    return false;

    }

     public boolean isPlaying(){
    if(status.equalsIgnoreCase("Playing"))
        return true;
    return false;

    }

    public boolean is_same(Player otherplayer){

    if(this.username.equals(otherplayer.getUsername()))
        return true;

    return false;
    }

}

class Game{ /*toso dyskolo */

    int player1=0;
    int player2=0;
    int id;

    public int getId(){return id;}
    public int getPlayer1(){ return player1;}
    public int getPlayer2(){ return player2;}


    Game(int id1,int id2){
    player1=id1;
    player2=id2;
    id = SuperPeer.gamedb.increment_games();
    }


}


