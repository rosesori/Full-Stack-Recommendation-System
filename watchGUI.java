import javax.swing.*;

//import javax.swing.JComboBox;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;
// import java.sql.*;
// import java.awt.event.*;
// import java.awt.Dimension;
// import java.awt.BorderLayout;
// import java.awt.GridLayout;

//pic stuff
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class watchGUI extends JFrame {

    public String receivedID;

    public String foundTitle;

    public BufferedImage myPicture;
    public JLabel picLabel;

    public watchGUI(String foundTitle, String receivedID){
        this.foundTitle = foundTitle;
        this.receivedID = receivedID;


        // picture loading
        try {
            myPicture = ImageIO.read(new File("play.png"));
            picLabel = new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            System.out.println("failed on image stuff");
        }




        // Frame configurations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // this will make the program shut down
        setTitle(foundTitle);
        setLayout(null);
        setSize(700,500);

        //Left side of screen
        // JLabel mediaTitle = new JLabel(foundTitle);
        //     mediaTitle.setBounds(300, 0, 300, 100);
        JButton btn_back_to_browse = new JButton("Back to Browse");
            btn_back_to_browse.setBounds(5, 20, 175, 25);
            btn_back_to_browse.addActionListener(e -> backHome());


        picLabel.setBounds(200, 80, 300, 300);
        
        // add(mediaTitle);
        add(btn_back_to_browse);

        //picture
        add(picLabel);
        

        // Add panels to frame, and make it visible
        
        setLocationRelativeTo(null); // center the frame on the screen when it opens
        //pack();
        setVisible(true);
    }

    

    public void backHome() {
        new homeGUI(receivedID);
        setVisible(false);
        dispose();
    }
}