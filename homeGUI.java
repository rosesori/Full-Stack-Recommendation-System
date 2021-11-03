import javax.swing.*;

//import javax.swing.JComboBox;
import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;
import java.sql.*;
import java.awt.event.*;
// import java.awt.Dimension;
// import java.awt.BorderLayout;
// import java.awt.GridLayout;

public class homeGUI extends JFrame {

    JLabel lbl_start = new JLabel("Start Date: ");
    JTextField txt_start = new JTextField();

    JLabel lbl_end = new JLabel("End Date: ");
    JTextField txt_end = new JTextField();

    // scroll pane for recommended
    ArrayList<String> arr_list_rec = new ArrayList<String>();
    JList jList_rec = new JList(arr_list_rec.toArray());
    JScrollPane scroll_pane_rec = new JScrollPane(jList_rec, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Scroller where list goes in

    // Scroll pane setup for watch history
    ArrayList<String> arr_list_watch_hist = new ArrayList<String>();
    JList jList_watch_hist = new JList(arr_list_watch_hist.toArray());
    JScrollPane scroll_pane_watch_hist = new JScrollPane(jList_watch_hist,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Scroller
                                                                                                            // where
                                                                                                            // list goes
                                                                                                            // in

    // Variables that will be used in multiple functions of the class
    public String receivedID;
    public String startDate;
    public String endDate;

    JLabel recommendedForYou = new JLabel("Viewer Choice");

    public homeGUI(String receivedID) {

        this.receivedID = receivedID;

        // Frame configurations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // this will make the program shut down
        setTitle("Home");
        setLayout(null);
        setSize(700, 500);

        // viewer beware and veiwer recommendation buttons
        JButton viewerBeware = new JButton("Viewer Beware");
        viewerBeware.setBounds(200, 10, 130, 40);
        viewerBeware.addActionListener(e -> bewareFunc());
        JButton viewerChoice = new JButton("Viewer Choice");
        viewerChoice.setBounds(30, 10, 130, 40);
        viewerChoice.addActionListener(e -> choiceFunc());

        // Left side of screen


        recommendedForYou.setBounds(100, 20, 300, 100);
        scroll_pane_rec.setBounds(30, 90, 300, 300);
        JButton searchButton = new JButton("Search For Titles");
        searchButton.setBounds(30, 400, 150, 40);
        searchButton.addActionListener(e -> openSearch());

        // Right side of screen
        JLabel historyLabel = new JLabel("Your Watch History");
        historyLabel.setBounds(460, 20, 200, 100);
        scroll_pane_watch_hist.setBounds(360, 90, 310, 300);
        JButton search = new JButton("Search");
        search.setBounds(575, 400, 100, 50);
        search.addActionListener(e -> populateWatchHist());

        // START AND END DATES
        // x, y, width, height
        lbl_start.setBounds(220, 405, 175, 25);
        txt_start.setBounds(300, 405, 100, 25);

        lbl_end.setBounds(400, 405, 175, 25);
        txt_end.setBounds(470, 405, 100, 25);

        // Add components to frame
        add(scroll_pane_rec);
        add(scroll_pane_watch_hist);
        add(recommendedForYou);
        add(searchButton);
        add(historyLabel);
        add(lbl_start);
        add(txt_start);
        add(lbl_end);
        add(txt_end);
        add(search);
        add(viewerBeware);
        add(viewerChoice);

        jList_watch_hist.addMouseListener(mouseListener);

        // Center the frame window on middle of screen and then allow it to be visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void openSearch() {
        new searchGUI(receivedID);
        setVisible(false);
        dispose();
    }

    // Depends on startDate and endDate specified by customer
    public void populateWatchHist() {

        startDate = txt_start.getText();
        endDate = txt_end.getText();

        arr_list_watch_hist.clear();

        MainFile mainFile = new MainFile();

        ResultSet rs = mainFile.runSQLString(
                "SELECT * FROM mediacollection JOIN customersratings ON mediacollection.media_id=customersratings.media_id WHERE (customersratings.customer_id = '"
                        + receivedID + "') AND (customersratings.date_rated BETWEEN '" + startDate + "' AND '" + endDate
                        + "') ORDER BY date_rated DESC;");
        ;

        // nullptr exception catch

        try {
            try {
                while (rs.next()) {
                    arr_list_watch_hist.add(rs.getString("media_title") + "\n");
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Check your dates and try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (arr_list_watch_hist.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Titles Found");
        }

        jList_watch_hist.setListData(arr_list_watch_hist.toArray());
        jList_watch_hist.repaint();
        scroll_pane_watch_hist.repaint();
    }

    // Makes titles of media clickable (to open up the watch screen)
    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {

                String selectedItem = (String) jList_watch_hist.getSelectedValue();
                System.out.println("found Title: " + selectedItem);

                new watchGUI(selectedItem, receivedID);
            }
        }
    };

    //TODO - Daniel
    public void choiceFunc() {

        arr_list_rec.clear();
        recommendedForYou.setText("Viewer Choice");

        MainFile mainFile = new MainFile();

        ResultSet rs = mainFile.runSQLString(
                "SELECT genre FROM (SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id WHERE customersratings.customer_id = '" + receivedID + "') AS mergedTable GROUP BY genre ORDER BY COUNT(*) DESC LIMIT 3;");
                //"SELECT genre FROM (SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id WHERE
                //customersratings.customer_id = '923517') AS mergedTable GROUP BY genre ORDER BY COUNT(*) DESC LIMIT 3;"
        // SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id;
            // this merges mediagenres and customerratings by media_id

        // SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id WHERE customerratings.custoemr_id = '923517';
            // now were doing it for a certain customer only

        // ! SELECT genre COUNT(genre) AS 'value_occurrence' FROM ('SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id WHERE customersratings.customer_id = '923517') GROUP BY genre ORDER BY 'value_occurrence' DESC LIMIT 1;

        // SELECT genre FROM (SELECT * FROM mediagenres JOIN customersratings ON mediagenres.media_id=customersratings.media_id WHERE customersratings.customer_id = '923517') AS mergedTable GROUP BY genre ORDER BY COUNT(*) DESC LIMIT 3;

        // SELECT * FROM mediagenres WHERE mediagenres.genre = 'Comedy';

        String favoriteGenre = "";
        String secondFavGenre = "";
        String thirdFaveGenre = "";

        try {
            rs.next();
            favoriteGenre = rs.getString("genre");

            rs.next();
            secondFavGenre = rs.getString("genre");

            rs.next();
            thirdFaveGenre = rs.getString("genre");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //* SECOND QUERY -----------------


        
        // SELECT media_title FROM (SELECT * FROM mediacollection JOIN mediagenres ON mediacollection.media_id=mediagenres.media_id WHERE mediagenres.genre = 'Comedy' AND mediacollection.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customerswatchedlist.customer_id = '923517')) AS mergedTable;
        // SELECT media_title FROM (SELECT * FROM mediacollection JOIN mediagenres ON mediacollection.media_id=mediagenres.media_id WHERE mediagenres.genre = 'Comedy' AND mediacollection.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customerswatchedlist.customer_id = '923517') AND mediacollection.average_rating > 9.0) AS mergedTable;

        //favorite genre result set
        ResultSet rs1 = mainFile.runSQLString("SELECT media_title FROM (SELECT * FROM mediacollection JOIN mediagenres ON mediacollection.media_id=mediagenres.media_id WHERE mediagenres.genre = '" + favoriteGenre + "' AND mediacollection.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customerswatchedlist.customer_id = '" + receivedID + "') AND mediacollection.average_rating > 9.0) AS mergedTable LIMIT 10;");

        // second favorite genre result set
        ResultSet rs2 = mainFile.runSQLString("SELECT media_title FROM (SELECT * FROM mediacollection JOIN mediagenres ON mediacollection.media_id=mediagenres.media_id WHERE mediagenres.genre = '" + secondFavGenre + "' AND mediacollection.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customerswatchedlist.customer_id = '" + receivedID + "') AND mediacollection.average_rating > 9.0) AS mergedTable LIMIT 10;");

        //third favorite genre result set
        ResultSet rs3 = mainFile.runSQLString("SELECT media_title FROM (SELECT * FROM mediacollection JOIN mediagenres ON mediacollection.media_id=mediagenres.media_id WHERE mediagenres.genre = '" + thirdFaveGenre + "' AND mediacollection.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customerswatchedlist.customer_id = '" + receivedID + "') AND mediacollection.average_rating > 9.0) AS mergedTable LIMIT 10;");

        
        try {
            while(rs1.next()){
                arr_list_rec.add(rs1.getString("media_title") + "\n");
            }

            while(rs2.next()){
                arr_list_rec.add(rs2.getString("media_title") + "\n");
            }

            while(rs3.next()){
                arr_list_rec.add(rs3.getString("media_title") + "\n");
            }

            // for(int i = 0; i < arr_list_watch_hist.size(); i++){
            //     System.out.println(arr_list_watch_hist.get(i));
            // }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (arr_list_rec.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Titles Found");
        }

        jList_rec.setListData(arr_list_rec.toArray());
        jList_rec.repaint();
        scroll_pane_rec.repaint();
    }

    public void bewareFunc() {
        arr_list_rec.clear();
        recommendedForYou.setText("Viewer Choice");

        MainFile mainFile = new MainFile();

        // 2625420
        // 2439493
        // (have many occurences where they dislike same movies) 266

        // SELECT COUNT(*) FROM (SELECT * FROM customersratings WHERE customer_id = '923517') AS A INNER JOIN (SELECT * FROM customersratings WHERE customer_id = '2625420') AS B ON A.customer_rating < 3 AND B.customer_rating< 3 AND A.media_id = B.media_id;

        ResultSet rs = mainFile.runSQLString(
                "SELECT customer_id FROM (SELECT second.customer_id FROM (SELECT * FROM customersratings WHERE customer_id = '" + receivedID + "') AS first JOIN (SELECT * FROM customersratings) AS second ON first.customer_rating < 3 AND second.customer_rating < 3 AND first.media_id=second.media_id) AS final GROUP BY customer_id ORDER BY COUNT(*) DESC LIMIT 2;");

        
        String dislike_match = "";

        try {
            rs.next();
            rs.next();
            dislike_match = rs.getString("customer_id");

            System.out.println(dislike_match);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //* SECOND QUERY -----------------

        // SELECT media_title FROM (SELECT * FROM (SELECT * FROM (SELECT media_id FROM customersratings WHERE customer_id = 'USERTWO' AND customer_rating < 3) AS first WHERE first.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customer_id = 'USERONE')) AS final JOIN mediacollection ON final.media_id=mediacollection.media_id) AS finaltwo;
        ResultSet rs2 = mainFile.runSQLString(
                "SELECT media_title FROM (SELECT * FROM (SELECT * FROM (SELECT media_id FROM customersratings WHERE customer_id = '" + dislike_match + "' AND customer_rating < 3) AS first WHERE first.media_id NOT IN (SELECT media_id FROM customerswatchedlist WHERE customer_id = '" + receivedID + "')) AS final JOIN mediacollection ON final.media_id=mediacollection.media_id) AS finaltwo LIMIT 30;");

        try {
            while(rs2.next()){
                arr_list_rec.add(rs2.getString("media_title") + "\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // for(int i = 0; i < arr_list_rec.size(); i++){
        //     System.out.println(arr_list_rec.get(i));
        // 

        if (arr_list_rec.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Titles Found");
        }

        jList_rec.setListData(arr_list_rec.toArray());
        jList_rec.repaint();
        scroll_pane_rec.repaint();
    }
}
