import javax.swing.*;
import java.sql.*;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;

// import java.awt.event.*;
// import java.awt.Dimension;

// First screen, prompt the user to enter their customerID

public class welcomeGUI extends JFrame {

    // Components
    JLabel lbl_customerID = new JLabel("Customer ID: ");
    JTextField txt_customerID = new JTextField();
    JLabel lbl_instruction = new JLabel("Enter your customer ID");
    JButton btn_save = new JButton("Enter");
    JButton btn_see_analytics = new JButton("See Analytics");

    // Public variable to the class
    public String receivedID; // public boolean validIDFound;

    public welcomeGUI() {

        // Frame configurations
        super("Eleven Tech Solutions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 400);
        setLayout(null);

        // Place components
        lbl_instruction.setBounds(100, 10, 400, 25);    // Create a scroller, set its size, set scroller in constructor give it list
        add(lbl_instruction);
        
        // label for customer id
        lbl_customerID.setBounds(100, 40, 175, 25);
        add(lbl_customerID);

        // text custome id
        txt_customerID.setBounds(190, 40, 160, 25);
        add(txt_customerID);

        // save button
        btn_save.setBounds(150, 250, 100, 25);
        add(btn_save);
        btn_save.addActionListener(e -> saveID());

        // see analytics
        btn_see_analytics.setBounds(135, 300, 130, 25);
        add(btn_see_analytics);
        btn_see_analytics.addActionListener(e -> analytics());
        
        setLocationRelativeTo(null); // center the window on the screen
        setVisible(true);
    }
    
    public void saveID() {

        // validIDFound = false;
        receivedID = txt_customerID.getText();

        //Valid ID:  1488844
        System.out.println("I got the following customer id: " + receivedID);

        MainFile mainFile = new MainFile();

        // this function returns result of type ResultSet
        ResultSet rs = mainFile.runSQLString("SELECT COUNT(*) FROM customerswatchedlist WHERE customer_Id = '" + receivedID + "';");

        int intCount = 0;

        try {
            while (rs.next()){
                
                long count = rs.getLong(1);
                intCount = (int)count;
                System.out.println("intCount: " + intCount);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(intCount > 0){
            // validIDFound = true;
            new homeGUI(receivedID);
            setVisible(false);
            dispose();
        }else{
            lbl_instruction.setText("ERROR: Please enter an ID with at least 1 entry.");
        }

        // return validIDFound;
        
    }

    public void analytics(){
        new analyticsGUI();
        setVisible(false);
        dispose();
    }

    public String getCustomerID(){
        return receivedID;
    }
}
