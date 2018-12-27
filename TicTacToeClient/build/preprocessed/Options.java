/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author std07078
 */
public class Options {
    ClientMIDlet parent;
    private boolean enableSound;
    private boolean enableMusic;
    private boolean enableVibration;
    /**
     * The constructor of class Options ,both sound and Vibration are enabled at start
     */
    Options(ClientMIDlet p,boolean sound,boolean music,boolean vibration){
        parent = p;
        enableSound = sound;
        enableMusic = music;
        enableVibration = vibration;
        
    }
    /**
     * Toggles sound on/off
     */
    public void toggleSound(){
        if (enableSound==true) enableSound = false;
        else enableSound = true;
    }
    /**
     *  Toggles Music on/off
     */
    public void toggleMusic(){
        if(enableMusic == true){
            enableMusic = false;
            parent.getMySounds().stopMusic();
        }
        else{
            enableMusic = true;
            parent.getMySounds().startMusic();
        }
        
    }
            
    /**
     * Toggles VIbration on/off
     */
    public void toggleVibration(){
        if (enableVibration==true) enableVibration = false;
        else enableVibration = true;
    }
    /**
     * Accessor of sound state
     * @return Description true if enableSound is true else false.
     */
    public boolean getSound(){
        return enableSound;
    }
    /**
     * Accessor of Vibration state
     * @return Description true if enableVibration is true else false.
     */
    public boolean getVibration(){
        return enableVibration;
    }
    /**
     *  Accessor of Music state 
     * @return Description true if enableMusic is true else false.
     */
    public boolean getMusic(){
        return enableMusic;
    }
}
