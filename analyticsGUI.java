import javax.swing.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.BorderLayout;
// import java.awt.GridLayout;
// import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class analyticsGUI extends JFrame {

    // Public class variables
    JPanel panel = new JPanel();
    ArrayList<String> foundTitles = new ArrayList<String>();
    JList foundTitlesList = new JList(foundTitles.toArray());
    JScrollPane scroll_pane_title_list = new JScrollPane(foundTitlesList, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Scroller where list goes in

    JButton btn_back_to_welc = new JButton("Back to Welcome Page");

    JTextField startField = new JTextField();
    JTextField endField = new JTextField();
    JTextField title1 = new JTextField();
    JTextField title2 = new JTextField();
    JLabel titleText = new JLabel("Top 10 Most Watched Media");
    
    public analyticsGUI() {
        // Frame configurations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Analytics Portal");
        setSize(700, 500); // width , height

        // Panel configurations
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(null);

        // Components
        JButton freshTomatoHelpButton = new JButton("?");
        JButton enterButton = new JButton("Enter");
        JLabel top10Label = new JLabel("Top 10 Media From:");
        JLabel startLabel = new JLabel("Start Date");
        JLabel endLabel = new JLabel("End Date");

        JButton hollywoodPairs = new JButton("Hollywood Pairs");  //* PHASE 4
        JButton cultClassics = new JButton("Cult Classics");      // " "
        JLabel freshTomato = new JLabel("Fresh Tomato Number:");  // " "
        JButton enterTomato = new JButton("Enter");
        JLabel title1Label = new JLabel("Title 1");
        JLabel title2Label = new JLabel("Title 2");        

        // Components config
        startField.setEditable(true);
        endField.setEditable(true);
        
        hollywoodPairs.addActionListener(e -> showPairs());   //* PHASE 4 
        cultClassics.addActionListener(e -> showCult());
        title1.setEditable(true);
        title2.setEditable(true);
        freshTomatoHelpButton.addActionListener(e -> freshTomatoHelp());
        enterButton.addActionListener(e -> enterInfo());
        enterTomato.addActionListener(e -> enterTitles());
        
        // Back to welcome page button
        btn_back_to_welc.setBounds(10, 10, 200, 25);
        add(btn_back_to_welc);
        btn_back_to_welc.addActionListener(e -> backWelcFunc());

        // Configure component placements
        titleText.setBounds(270, 20, 200, 30);
        scroll_pane_title_list.setBounds(20, 60, 650, 240);

        top10Label.setBounds(480, 300, 140, 40);
        startField.setBounds(420, 360, 120, 40);
        startLabel.setBounds(460, 320, 120, 40);
        endLabel.setBounds(580, 320, 120, 40);
        endField.setBounds(560, 360, 120, 40);
        enterButton.setBounds(490, 410, 120, 30);
        
        hollywoodPairs.setBounds(40, 315, 200, 20);
        cultClassics.setBounds(40, 340, 200, 20);
        freshTomato.setBounds(95, 360, 200, 40);
        title1Label.setBounds(50, 400, 200, 20);
        title2Label.setBounds(160, 400, 200, 20);
        title1.setBounds(20, 420, 100, 30);
        title2.setBounds(130, 420, 100, 30);
        enterTomato.setBounds(240, 418, 100, 30);
        freshTomatoHelpButton.setBounds(40, 370, 45, 20);

        // Add components to frame
        panel.add(titleText);
        panel.add(scroll_pane_title_list);

        panel.add(top10Label);
        panel.add(startField);
        panel.add(startLabel);
        panel.add(endField);
        panel.add(endLabel);
        panel.add(enterButton);

        panel.add(hollywoodPairs);
        panel.add(cultClassics);
        panel.add(freshTomato);
        panel.add(title1Label);
        panel.add(title2Label);
        panel.add(title1);
        panel.add(title2);
        panel.add(enterTomato);
        panel.add(freshTomatoHelpButton);

        // Add panel to frame and allow visibility
        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null); // center the window on the screen
        setVisible(true);

    }

    public void freshTomatoHelp(){
        new freshTomatoGUI();
        setVisible(false);
        dispose();
    }

    public void enterInfo(){
        titleText.setText("Top 10 Most Watched Media");
        foundTitles.clear();
        
        String start_date = startField.getText();
        String end_date = endField.getText();

        MainFile mainFile = new MainFile();

        // Connect to database
        ResultSet rs = mainFile.runSQLString("SELECT A.media_title FROM mediacollection A WHERE A.media_id in (SELECT media_id FROM (SELECT media_id,COUNT(media_id) AS value_occurrence FROM customersratings WHERE (date_rated BETWEEN '" + start_date + "' AND '" + end_date + "') GROUP BY media_id ORDER BY value_occurrence DESC LIMIT 10) AS foo);");
        try {
            while (rs.next()) {
                foundTitles.add(rs.getString("media_title") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Display the titles
        foundTitlesList.setListData(foundTitles.toArray());
        foundTitlesList.repaint();
        scroll_pane_title_list.repaint();
        for (int i = 0; i < foundTitles.size(); i++) {
            System.out.println(foundTitles.get(i));
        }
    }
    
    public void backWelcFunc(){
        new welcomeGUI();
        setVisible(false);
        dispose();
    }
    
    //* Aidan
    public void showCult(){
        titleText.setText("Cult Classics");
        foundTitles.clear();
        
        MainFile mainFile = new MainFile();

        //Connect to database
        ResultSet rs = mainFile.runSQLString("SELECT media_title FROM mediacollection A WHERE A.media_id in( SELECT media_id FROM( SELECT media_id, COUNT(media_id) AS value_occurrence FROM customersratings WHERE customer_rating > 3 GROUP BY media_id ORDER BY value_occurrence DESC LIMIT 10) AS foo);");
        try{
            while(rs.next()){
                foundTitles.add(rs.getString("media_title") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Display found titles
        foundTitlesList.setListData(foundTitles.toArray());
        foundTitlesList.repaint();
        scroll_pane_title_list.repaint();
        for(int i = 0; i < foundTitles.size(); ++i){
            System.out.println(foundTitles.get(i));
        }
    }

    //* Brayden
    public void showPairs(){
        titleText.setText("HollyWood Pairs");
        foundTitles.clear();
        
        MainFile mainFile = new MainFile();

        //Connect to database
        ResultSet rs = mainFile.runSQLString("SELECT DISTINCT actor1.name as actor_1_name, actor2.name as actor_2_name, avg(average_rating) as avg_rating FROM mediacrewmembers crew1, mediacrewmembers crew2, crewmembers actor1, crewmembers actor2, mediacollection media1 WHERE crew1.media_id = crew2.media_id AND crew1.media_id = media1.media_id AND crew1.crew_id = actor1.crew_id AND crew2.crew_id = actor2.crew_id AND actor1.name < actor2.name GROUP BY actor1.name, actor2.name ORDER BY avg_rating desc LIMIT 10;");
        try{
            while(rs.next()){
                foundTitles.add(rs.getString("actor_1_name") + "   " + rs.getString("actor_2_name") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Display found titles
        foundTitlesList.setListData(foundTitles.toArray());
        foundTitlesList.repaint();
        scroll_pane_title_list.repaint();
        for(int i = 0; i < foundTitles.size(); ++i){
            System.out.println(foundTitles.get(i));
        }
    }

    //* Rose
    public void enterTitles(){

        titleText.setText("Fresh Tomato Number");
        foundTitles.clear();
        String firstTitle = title1.getText();
        String secondTitle = title2.getText();

        // STEP 0: Connect to database
        MainFile mainFile = new MainFile();
        mainFile.openConn();

        // STEP 1: Check if title1 is rated HIGHLY (4 or 5) by any customers
        int title1RatingCount = 0;
        ResultSet rs = mainFile.runFasterSQLString("SELECT COUNT(*) FROM customersratings A WHERE A.media_id in (SELECT B.media_id FROM mediacollection B WHERE b.media_title = '" + firstTitle + "') AND A.customer_rating >= 4;");
        try {
            while (rs.next()) {
                long count = rs.getLong(1);
                title1RatingCount = (int)count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // STEP 2: Check if title2 is rated HIGHLY (4 or 5) by any customers
        int title2RatingCount = 0;
        rs = mainFile.runFasterSQLString("SELECT COUNT(*) FROM customersratings A WHERE A.media_id in (SELECT B.media_id FROM mediacollection B WHERE b.media_title = '" + secondTitle + "') AND A.customer_rating >= 4;");
        try {
            while (rs.next()) {
                long count = rs.getLong(1);
                title2RatingCount = (int)count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // STEP 2.5: Get ID's of given title names
        String firstTitleID = "";
        String secondTitleID = "";
        rs = mainFile.runFasterSQLString("SELECT media_id FROM mediacollection WHERE media_title = '" + firstTitle + "';");
        try {
            if (rs.next()) {
                firstTitleID = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs = mainFile.runFasterSQLString("SELECT media_id FROM mediacollection WHERE media_title = '" + secondTitle + "';");
        try {
            if (rs.next()) {
                secondTitleID = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // STEP 3: Depending on ratings validity for title1 and title2, continue feature functionality
        if ( title1RatingCount==0 && title2RatingCount==0 ) { 
            JOptionPane.showMessageDialog(null, "Title 1 and Title 2 have no high ratings. Enter different titles.");
        } else if (title1RatingCount==0 && title2RatingCount>0) {
            JOptionPane.showMessageDialog(null, "Title 1 has no high ratings. Enter a different title.");
        } else if (title1RatingCount>0 && title2RatingCount==0) {
            JOptionPane.showMessageDialog(null, "Title 2 has no high ratings. Enter a different title.");
        } else { // Successful - Titles have ratings. 

            // 1. Get first chain link (Find a user that rated title1 highly)
            String userA = "";
            String userA_rating = "";
            rs = mainFile.runFasterSQLString("SELECT B.customer_id, B.customer_rating FROM mediacollection A INNER JOIN customersratings B ON A.media_id = B.media_id WHERE B.customer_rating >= 4 AND media_title = '" + firstTitle + "' ORDER BY customer_rating DESC;");
            try {
                if (rs.next()) {
                    userA = rs.getString(1);
                    userA_rating = rs.getString(2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 2. Second chain link (Find a user that rated title2 highly)
            String userB = "";
            String userB_rating = "";
            rs = mainFile.runFasterSQLString("SELECT B.customer_id, B.customer_rating FROM mediacollection A INNER JOIN customersratings B ON A.media_id = B.media_id WHERE B.customer_rating >= 4 AND media_title = '" + secondTitle + "' ORDER BY customer_rating DESC;");
            try {
                if (rs.next()) {
                    userB = rs.getString(1);
                    userB_rating = rs.getString(2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 3. Find a different title that both userA and userB rated highly.
            String middleTitleID = "";
            rs = mainFile.runFasterSQLString("SELECT media_id FROM (SELECT media_id, count(media_id) AS Cnt FROM (SELECT * FROM customersratings WHERE (customer_id = '" + userA + "' OR customer_id = '" + userB + "') AND (customer_rating >= 4) AND (media_id != '" + firstTitleID + "') AND (media_id != '" + secondTitleID + "')) AS foo GROUP BY media_id) AS foobar WHERE Cnt = '2';");
            try {
                if (rs.next()) {
                    middleTitleID = rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 4. Get both customer ratings of that middleTitle
            String middleUserA_Rating = "";
            String middleUserB_Rating = "";
            rs = mainFile.runFasterSQLString("SELECT customer_id, customer_rating FROM customersratings WHERE (customer_id = '1722054' OR customer_id = '530789') AND (customer_rating >= 4) AND (media_id != 'tt0118748') AND (media_id != 'tt0039645') AND (media_id = 'tt0034405');");
            try {
                while (rs.next()) {
                    if (rs.getString(1).equals(userA)) {
                        middleUserA_Rating = rs.getString(2);
                    } else if (rs.getString(1).equals(userB)) {
                        middleUserB_Rating = rs.getString(2);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 5. Get media_title of that middleTitleID
            String middleTitle = "";
            rs = mainFile.runFasterSQLString("SELECT media_title FROM mediacollection WHERE media_id = 'tt0034405';");
            try {
                if (rs.next()) {
                    middleTitle = rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            // Check variables in terminal
            System.out.println("UserA: " + userA + " | UserA_Rating: " + userA_rating + "\nUserB: " + userB + " | UserB_Rating: " + userB_rating);
            System.out.println("Intermediate movie: " + middleTitle + " | UserARated: " + middleUserA_Rating + " | UserBRated: " + middleUserB_Rating);

            // Display
            foundTitles.add("Shortest Chain:");
            foundTitles.add("     " + firstTitle + " -> " + middleTitle + " -> " + secondTitle);
            foundTitles.add(firstTitle + " was rated " + userA_rating + " stars by user " + userA + ".");
            foundTitles.add("     User " + userA + " also rated the title " + middleTitle + " highly with " + middleUserA_Rating + " stars. ");
            foundTitles.add("     " + middleTitle + " was rated " + middleUserB_Rating + " stars by user " + userB + " who also rated " + secondTitle + " " + userB_rating + " stars.");
            foundTitles.add("The shortest chain between these two titles then is " + firstTitle + " to " + userA);
            foundTitles.add("     to " + middleTitle + " to " + userB + " to " + secondTitle + ".");

            foundTitlesList.setListData(foundTitles.toArray());
            foundTitlesList.repaint();
            scroll_pane_title_list.repaint();

            mainFile.closeConn();
        }

    }
}