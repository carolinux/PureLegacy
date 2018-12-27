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
import javax.microedition.media.*;

public class GameSounds {
    public Player myMusicPlayer;
    public Player mySoundPlayer;
    Options myOptions;
    GameSounds(Options o){
        myOptions = o;
    }
    public void loadMusic(){
        try {
            myMusicPlayer = Manager.createPlayer("http://cgi.di.uoa.gr/~std07078/anaptyksiSounds/jingle.mp3");
            System.out.println("MP3 HERE HERE");
            myMusicPlayer.setLoopCount(99);
            myMusicPlayer.realize();
            myMusicPlayer.prefetch();
            
       } 
       catch(IOException ioe) {} 
       catch(MediaException e) {}                         
    }
    public void startMusic(){
        if(myOptions.getSound() == true && myMusicPlayer!=null ){
            try{
                myMusicPlayer.start();
            }
            catch(MediaException e) {}
        }
        else{
            System.out.println("Sounds are disabled , cannot play sound");
        }
    }
    public void stopMusic(){
        try{
            myMusicPlayer.stop();
        } 
        catch(MediaException e) {} 
    }
    public void playSound(String soundName){
        String tempSound = "";
        if(myOptions.getSound() == true){  
            if(soundName.equals("moveMe")){tempSound = "moveMe";}
            else if(soundName.equals("moveRival")){ tempSound = "moveRival"; }
            else if(soundName.equals("menuChoice")){ tempSound = "menuChoice"; }
            try{
                mySoundPlayer = Manager.createPlayer("http://cgi.di.uoa.gr/~std07078/anaptyksiSounds/"+tempSound+".mp3");
                mySoundPlayer.realize();
                mySoundPlayer.prefetch();
                mySoundPlayer.start();
            }
            catch(IOException ioe) {}
            catch(MediaException e) {}
        }
        else{
            System.out.println("Sounds are disabled , cannot play sound");
        }
    }
     
     
}
