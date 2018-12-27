/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author std07078
 */
import java.io.IOException;

import java.util.*;
import javax.microedition.lcdui.*;

public class SplashScreen extends Canvas
{
    private Display     display;
    private Displayable next;
    private Timer       timer = new Timer();
    private Image       splashImage;
    private Image       imageToShow;
    private String      toShow;
    /**
     * The constructor of class SplashScreen
     * @param display Description Parent Display
     * @param next Description The next Displayable to appear
     * @param imageName Description image filename
     */
    public SplashScreen(Display display, Displayable next,String imageName)
    {
        this.display = display;
        this.next = next;
        this.toShow = imageName;
        display.setCurrent( this );
    }
    /**
     * A method to dismiss the splash screen if any key is pressed
     * @param keyCode Any keypress
     */
    protected void keyPressed( int keyCode ){
        dismiss();
    }
    /**
     * Canva's paint , all the code for the images to appear is here
     * @param g The Default Graphics parameter of paint
     */
    protected void paint( Graphics g ){
        // do your drawing here
        int width = getWidth();
        int height = getHeight();

        // Fill the background using black
        g.setColor(0xffffff);
        g.fillRect(0, 0, width, height);

        if (splashImage == null) {
            try {
                splashImage = Image.createImage("/images/splash_tictactoe.png");
            } catch (IOException ex) {
                g.setColor(0xffffff);
                g.drawString("Failed to load image!", 0, 0, Graphics.TOP | Graphics.LEFT);
                return;
            }
        }
        if(toShow.equals("win")){
            try{
                imageToShow = Image.createImage("/images/santaWin.png");
            } catch (IOException ex) {
                g.setColor(0xffffff);
                g.drawString("Failed to load image!", 0, 0, Graphics.TOP | Graphics.LEFT);
                return;
            }
        }
        else if(toShow.equals("lose")){
            try{
                imageToShow = Image.createImage("/images/santaLose.png");
            } catch (IOException ex) {
                g.setColor(0xffffff);
                g.drawString("Failed to load image!", 0, 0, Graphics.TOP | Graphics.LEFT);
                return;
            }
        }


        if(toShow.equals("splash")){
            g.drawImage(splashImage, width/2, height/2, Graphics.VCENTER | Graphics.HCENTER);
        }
        else{
            g.drawImage(imageToShow, width/2, height/2, Graphics.VCENTER | Graphics.HCENTER);

        }






    }
    /**
     * A method to dismiss the splash screen if the pointer is pressed
     * @param x Description any position on the x axis
     * @param y Description any position on the y axis
     */
    protected void pointerPressed( int x, int y ){
        dismiss();
    }
    /**
     * Creates a countdown timer set at 5 secs after which the displayable changes
     */
    protected void showNotify()
    // called automatically when the Canvas is put on screen
    {  timer.schedule( new CountDown(), 5000 );
               // CountDown started after 5 secs
    }
    /**
     * Changes the displayable and cancels the countdown timer
     */
    private void dismiss(){
        timer.cancel();
        display.setCurrent( next );
    }

    // ----------------------------------------
    /**
     * When the timer is out the dismiss method is called
     */
    private class CountDown extends TimerTask
    {
        public void run(){
            dismiss();
        }
    }
}