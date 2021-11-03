import javax.swing.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.BorderLayout;
// import java.awt.GridLayout;
// import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class freshTomatoGUI extends JFrame{
    
    // Public class variables
    JPanel panel = new JPanel();
    ArrayList<String> foundTitles = new ArrayList<String>();
    JList foundTitlesList = new JList(foundTitles.toArray());
    JScrollPane scroll_pane_title_list = new JScrollPane(foundTitlesList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Scroller where list goes in
    JButton btn_back_to_welc = new JButton("Back");
    JLabel titleText = new JLabel("Rated titles, which therefore are options for Fresh Tomato");

    public freshTomatoGUI() {
        foundTitles.clear();
        
        // Connect to database
        MainFile mainFile = new MainFile();
        ResultSet rs = mainFile.runSQLString("SELECT A.media_id, A.media_title, A.media_type FROM mediacollection A WHERE A.media_id IN (SELECT B.media_id FROM customersratings B) ORDER BY A.media_title;");
        try {
            while (rs.next()) {
                foundTitles.add(rs.getString("media_title") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Frame configurations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Options");
        setSize(600, 900); // width , height

        // Panel configurations
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(null);

        // Components and component config
        titleText.setBounds(120, 50, 400, 30);
        scroll_pane_title_list.setBounds(20, 100, 550, 740);
        panel.add(titleText);
        panel.add(scroll_pane_title_list);

        // Back button
        btn_back_to_welc.setBounds(10, 10, 200, 25);
        add(btn_back_to_welc);
        btn_back_to_welc.addActionListener(e -> backFunc());

        // Add panel to frame and allow visibility
        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null); // center the window on the screen
        setVisible(true);

        // Display the titles
        foundTitlesList.setListData(foundTitles.toArray());
        foundTitlesList.repaint();
        scroll_pane_title_list.repaint();

    }

    public void backFunc() {
        new analyticsGUI();
        setVisible(false);
        dispose();
    }

}
