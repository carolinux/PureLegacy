/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author std07078
 */
/*
 *
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//package customitem;

import javax.microedition.lcdui.*;

//for random numbers //
import java.util.Random;



/**
 *
 * @version 2.0
 */
public class Table extends CustomItem implements ItemCommandListener {
    private static final Command CMD_SEL = new Command("Select", Command.ITEM, 1);
    private static final int UPPER = 0;
    private static final int IN = 1;
    private static final int LOWER = 2;
    private Display display;
    private int rows = 3;
    private int cols = 3;
    private int dx = 50;
    private int dy = 20;
    private int location = UPPER;
    private int currentX = 0;
    private int currentY = 0;
    private String[][] data = new String[rows][cols];

    //debug//
    Alert debugAlert = new Alert("Debug");
    private List upperMenu;
    private ClientMIDlet parent;
    private P2pGame myGame;
    public int move[] = new int[2];
private static final Command CMD_CHAT = new Command("Chat",Command.ITEM ,1);
    //random numbers//
    Random generator = new Random();

    // Traversal stuff
    // indicating support of horizontal traversal internal to the CustomItem
    boolean horz;

    // indicating support for vertical traversal internal to the CustomItem.
    boolean vert;

    public Table(String title, Display d,ClientMIDlet m,P2pGame g) {
        super(title);
        display = d;
        myGame = g;

        setDefaultCommand(CMD_SEL);
        addCommand(CMD_CHAT);
        setItemCommandListener(this);

        int interactionMode = getInteractionModes();
        horz = ((interactionMode & CustomItem.TRAVERSE_HORIZONTAL) != 0);
        vert = ((interactionMode & CustomItem.TRAVERSE_VERTICAL) != 0);
        //debug//
        
        parent = m;
        this.resetTable();

    }
    private void setMove(int Y,int X){
        move[0] = Y;
        move[1] = X;
    }

    protected int getMinContentHeight() {
        return (rows * dy) + 1;
    }

    protected int getMinContentWidth() {
        return (cols * dx) + 1;
    }

    protected int getPrefContentHeight(int width) {
        return (rows * dy) + 1;
    }

    protected int getPrefContentWidth(int height) {
        return (cols * dx) + 1;
    }

    protected void paint(Graphics g, int w, int h) {
        g.setColor(0x00FF007F);
        for (int i = 0; i <= rows; i++) {
            if(i==0||i==3) continue;
            else g.drawLine(0, i * dy, cols * dx, i * dy);
        }

        for (int i = 0; i <= cols; i++) {
            if(i==0||i==3) continue;
            else g.drawLine(i * dx, 0, i * dx, rows * dy);
        }

        int oldColor = g.getColor();
        g.setColor(0x00006400);
        g.fillRect((currentX * dx) + 1, (currentY * dy) + 1, dx - 1, dy - 1);
        g.setColor(oldColor);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] != null) {
                    // store clipping properties
                    int oldClipX = g.getClipX();
                    int oldClipY = g.getClipY();
                    int oldClipWidth = g.getClipWidth();
                    int oldClipHeight = g.getClipHeight();
                    g.setClip((j * dx) + 1, i * dy, dx - 1, dy - 1);
                    g.drawString(data[i][j], (j * dx) + 2, ((i + 1) * dy) - 2,
                        Graphics.BOTTOM | Graphics.LEFT);

                    // restore clipping properties
                    g.setClip(oldClipX, oldClipY, oldClipWidth, oldClipHeight);
                }
            }
        }
    }

    protected boolean traverse(int dir, int viewportWidth, int viewportHeight, int[] visRect_inout) {
        if (horz && vert) {
            switch (dir) {
            case Canvas.DOWN:

                if (location == UPPER) {
                    location = IN;
                } else {
                    if (currentY < (rows - 1)) {
                        currentY++;
                        repaint(currentX * dx, (currentY - 1) * dy, dx, dy);
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else {
                        location = LOWER;

                        return false;
                    }
                }

                break;

            case Canvas.UP:

                if (location == LOWER) {
                    location = IN;
                } else {
                    if (currentY > 0) {
                        currentY--;
                        repaint(currentX * dx, (currentY + 1) * dy, dx, dy);
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else {
                        location = UPPER;

                        return false;
                    }
                }

                break;

            case Canvas.LEFT:

                if (currentX > 0) {
                    currentX--;
                    repaint((currentX + 1) * dx, currentY * dy, dx, dy);
                    repaint(currentX * dx, currentY * dy, dx, dy);
                }

                break;

            case Canvas.RIGHT:

                if (currentX < (cols - 1)) {
                    currentX++;
                    repaint((currentX - 1) * dx, currentY * dy, dx, dy);
                    repaint(currentX * dx, currentY * dy, dx, dy);
                }
            }
        } else if (horz || vert) {
            switch (dir) {
            case Canvas.UP:
            case Canvas.LEFT:

                if (location == LOWER) {
                    location = IN;
                } else {
                    if (currentX > 0) {
                        currentX--;
                        repaint((currentX + 1) * dx, currentY * dy, dx, dy);
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else if (currentY > 0) {
                        currentY--;
                        repaint(currentX * dx, (currentY + 1) * dy, dx, dy);
                        currentX = cols - 1;
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else {
                        location = UPPER;

                        return false;
                    }
                }

                break;

            case Canvas.DOWN:
            case Canvas.RIGHT:

                if (location == UPPER) {
                    location = IN;
                } else {
                    if (currentX < (cols - 1)) {
                        currentX++;
                        repaint((currentX - 1) * dx, currentY * dy, dx, dy);
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else if (currentY < (rows - 1)) {
                        currentY++;
                        repaint(currentX * dx, (currentY - 1) * dy, dx, dy);
                        currentX = 0;
                        repaint(currentX * dx, currentY * dy, dx, dy);
                    } else {
                        location = LOWER;

                        return false;
                    }
                }
            }
        } else {
            // In case of no Traversal at all: (horz|vert) == 0
        }

        visRect_inout[0] = currentX;
        visRect_inout[1] = currentY;
        visRect_inout[2] = dx;
        visRect_inout[3] = dy;

        return true;
    }
    /**
     * Resets the Table data to "" and calls the repaint method
     */
    public void resetTable(){

        int i,j;

        for(i=0;i<=2;i++){
            for(j=0;j<=2;j++){

                 data[j][i] = "";
                 repaint(j * dx, i * dy, dx, dy);
            }
        }
    }
    /**
     * Sets the text of the currently selected cell according to the given string
     * @param text Description The text to be put in the cell
     */
    public void setText(String text) {
        data[currentY][currentX] = text;
        repaint(currentY * dx, currentX * dy, dx, dy);
    }

    public void commandAction(Command c, Item i) {
        if(this.myGame == null){
            Alert b = new Alert("Tic-Tac-Toe", "Under Development ,press Done to proceed to alpha version", null, AlertType.CONFIRMATION);
            b.setTimeout(Alert.FOREVER);
            display.setCurrent(b);
            if (c == CMD_SEL ) { /* if we have selected a thing */
                //che
                //data[currentY][currentX] = "   X";
                if(!valid_move(currentY,currentX)){

                 Alert a = new Alert("Tic-Tac-Toe", "Invalid Move", null, AlertType.CONFIRMATION);
                    a.setTimeout(Alert.FOREVER);


                    display.setCurrent(a);
                }

             if(valid_move(currentY,currentX)){
                    setText("   X");
                
                    if(check_win("   X")){
                        //debugAlert.setString("player wins!");
                        //display.setCurrent(debugAlert);
                        resetTable();
                        if(parent.myOptions.getSound()==true) AlertType.INFO.playSound(display);
                        if(parent.myOptions.getVibration()==true) display.vibrate(300);
                    }
                    else{
                        simple_ai(currentY,currentX);
                        if(check_win("   O")){
                            //debugAlert.setString("computer wins!");
                            //display.setCurrent(debugAlert);
                            resetTable();
                            new SplashScreen(display,parent.getMainMenu(),"lose");
                            if(parent.myOptions.getSound()==true) AlertType.WARNING.playSound(display);
                            if(parent.myOptions.getVibration()==true) display.vibrate(300);
                        }
                    }
                }
                //TextInput textInput = new TextInput(data[currentY][currentX], this, display);
                //display.setCurrent(textInput);
            } //END IF
        }

        if(c == CMD_CHAT){
                display.setCurrent(myGame.myChat.chatForm);

            }
        else{
            if (c == CMD_SEL && myGame.iPlay()) {

                     if(!valid_move(currentY,currentX)){

                 Alert a = new Alert("Tic-Tac-Toe", "Invalid Move", null, AlertType.CONFIRMATION);
                    a.setTimeout(Alert.FOREVER);


                    display.setCurrent(a);
                }

print_data();
                if(valid_move(currentY,currentX)){
                   // setText("   "+myGame.getMySymbol());
                    setText(myGame.getMySymbol());

                    setMove(currentY,currentX);
                    synchronized(myGame){
                        myGame.notify();
                    }
                    myGame.unsetInitiative();
                }




                
            } //else do nothing! :D
        }
    }
    /**
     * A very simple A.I for the single player that makes a random move
     * @param y Description the currently selected y position
     * @param x Description the currently selected x position
     */
    public void simple_ai(int y,int x){

            int randomX = generator.nextInt(2);
            int randomY = generator.nextInt(2);
            while (!valid_move(randomY, randomX)){

                //debugAlert.setString(Integer.toString(randomY)+Integer.toString(randomY));
                //display.setCurrent(debugAlert);
                //display.vibrate(1);
                randomX = generator.nextInt(2);
                randomY = generator.nextInt(2);
                //debugAlert.setString(Integer.toString(randomY)+Integer.toString(randomY));
                //display.vibrate(1);
            }
            data[randomY][randomX] = "   O";
            setText("   O");
            //Alert myAlert;
            //myAlert = new Alert("occupied_tile","Already Occupied Tile",null,AlertType.INFO);


    }
    /**
     * Checks if the given cell of the table is occupied 
     * @param y Description The position on the y axis of the cell to be checked
     * @param x Description The position on the x axis of the cell to be checked
     * @return True if the cell is not occupied , else false
     */
    public boolean valid_move(int y,int x){
        if(data[y][x].equals(""))
            return true;
        return false;
    }
    /**
     * Prints the contents of the table to the standard output
     */
    public void print_data(){ //debugging
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                System.out.print(data[i][j]);
            }
        System.out.println();
        }
    }
    /**
     * Checks if the player with the given symbol has won
     * @param mySymbol Description Player's symbol
     * @return True if player has won else false
     */
    public boolean check_win(String mySymbol){
        boolean win=false;
       /*
        if( mySymbol.equals(data[0][0]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[2][2]) ){
            
            win = true;
            return win;
        }
        if( mySymbol.equals(data[2][0]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[0][2]) ){

            win = true;
            return win;
        }
        int i,j;

        int counter = 0;
        for(i=0;i<2;i++){
            for(j=0;j<2;j++){

                if(mySymbol.equals(data[j][i])){
                    counter++;
                }
                if(counter==3){
                    win=true;
                    return win;
                    
                }
                
            }
            counter=0;
        }
        */
        System.out.println("Checking win! fro symbol:"+mySymbol+"endof");

        if(mySymbol.equals(data[0][0]) && mySymbol.equals(data[0][1]) && mySymbol.equals(data[0][2]))
        return true;

        if(mySymbol.equals(data[1][0]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[1][2]))
        return true;

        if(mySymbol.equals(data[2][0]) && mySymbol.equals(data[2][1]) && mySymbol.equals(data[2][2]))
        return true;

        if(mySymbol.equals(data[0][0]) && mySymbol.equals(data[1][0]) && mySymbol.equals(data[2][0]))
        return true;

        if(mySymbol.equals(data[0][1]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[2][1]))
        return true;

        if(mySymbol.equals(data[0][2]) && mySymbol.equals(data[1][2]) && mySymbol.equals(data[2][2]))
        return true;

        if(mySymbol.equals(data[0][0]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[2][2]))
        return true;

        if(mySymbol.equals(data[0][2]) && mySymbol.equals(data[1][1]) && mySymbol.equals(data[2][0]))
        return true;



        
        return false;

    }
    /**
     * Checks the current status of the gameboard
     * @return WIN if player has won , LOSS if player has lost,DRAW if gameboard is
     * full with no winner , ONGOING if gameboard is not full with no winner
     */
    public String checkGameStatus(){
        String  status="";

        if(check_win(myGame.getMySymbol())){
            status = "WIN";
            //showing win image here , i have to change that in the future//
            new SplashScreen(display, parent.getMainMenu(),"win");
            if(parent.myOptions.getVibration()==true) display.vibrate(300);
            return status;
        }
        if(check_win(myGame.getRivalSymbol())){
            status = "LOSS";
            new SplashScreen(display, parent.getMainMenu(),"lose");
            return status;
        }

        if(gameFull()){
            if((check_win(myGame.getMySymbol()) == false && check_win(myGame.getRivalSymbol()) == false )){
                status = "TIE";
                return status;
            }
        }
        else{
            status = "ONGOING";
            return status;
        }
        return status;

    }
    /**
     * Checks if all the cells of the gameboard are occuppied
     * @return true if game is full else false 
     */
    public boolean gameFull(){

        boolean full = true;
        int i,j;
        for(i=0;i<=2;i++){
            for(j=0;j<=2;j++){

                 if(data[j][i].equals(""))
                 {full = false;
                 return full;}
            }
        }
        return full;
    }
    /**
     * Fills the cell of the given coords with rival's symbol
     * @param Y Description The Y axis position of the cell to be set
     * @param X Description The X axis position of the cell to be set
     */
    public void updateTableMove(int Y,int X){

        System.out.println("updating teh table");
        data[Y][X] = myGame.getRivalSymbol();
        repaint(Y * dx, X * dy, dx, dy);
    }
     /**
     * Fills the cell of the given coords with player's symbol
     * @param Y Description The Y axis position of the cell to be set
     * @param X Description The X axis position of the cell to be set
     */
    public void updateTableMyMove(int Y,int X){
        System.out.println("updating teh table (main)");
        data[Y][X] = myGame.getMySymbol();
        repaint(Y * dx, X * dy, dx, dy);
    }


    
}

