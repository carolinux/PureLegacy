/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package superpear;

/**
 *
 * @author std07078
 */

import java.io.*;
import java.net.*;
import java.util.*;/* for the timestamp */


//Zorbash:for the UI//
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;


//Zorbash:Ui variables//






public class UserInterface extends JFrame{

    private JLabel logLabel;
    private JTextArea logTextArea;
    private ImageIcon mylogo;


    UserInterface(){
        this.setTitle("Super Peer");
        logLabel = new JLabel("Java Technology Dive Log",new ImageIcon("/serverLogo.png"),JLabel.LEFT);
        logTextArea = new JTextArea();
        logTextArea.setBounds(320,320,320,320);
	add(logLabel);
        add(logTextArea);
        this.setSize(640, 480);
	// pack();
	//setVisible(true);


    }
    public void appendLog(String t){
        logTextArea.append(t);

    }

}
