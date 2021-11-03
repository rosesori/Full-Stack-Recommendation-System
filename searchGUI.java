import javax.swing.*;
import java.util.ArrayList;
import java.sql.*;
import java.awt.event.*;

public class searchGUI extends JFrame {

    String receivedID;

    // Global variables
    public String receivedTitle;
    ArrayList<String> foundTitles = new ArrayList<String>();

    // Components
    JList foundTitlesList = new JList(foundTitles.toArray());
    JButton btn_back_to_browse = new JButton("Back to Browse");
    JTextField txt_search = new JTextField();
    JButton btn_enter = new JButton("Enter");
    JScrollPane scroll_pane_title_list = new JScrollPane(foundTitlesList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Scroller where list goes in

    public searchGUI(String receivedID) {

        super("Search");
        this.receivedID = receivedID;

        // Frame configurations
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(null);

        // setBounds(x, y, width, height) of the components
        btn_back_to_browse.setBounds(5, 20, 175, 25);
        btn_back_to_browse.addActionListener(e -> backHome());
        txt_search.setBounds(190, 20, 400, 25);
        btn_enter.setBounds(600, 20, 100, 25);
        scroll_pane_title_list.setBounds(10, 50, 680, 400);

        // Adding to screen
        add(btn_back_to_browse);
        add(txt_search);
        add(btn_enter);
        add(scroll_pane_title_list);

        // If button clicked then run the save function
        btn_enter.addActionListener(e -> enterForTitleFunc());
        foundTitlesList.addMouseListener(mouseListener);

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    public void enterForTitleFunc() {

        foundTitles.clear();
        receivedTitle = txt_search.getText();

        // Valid ID: 1488844
        System.out.println("I got the following media_title: " + receivedTitle);

        MainFile mainFile = new MainFile();

        // this function returns result of type ResultSet
        ResultSet rs = mainFile.runSQLString("SELECT * FROM mediacollection WHERE media_title LIKE '%" + receivedTitle + "%';");
        try {
            while (rs.next()) {
                foundTitles.add(rs.getString("media_title") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (foundTitles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Titles Found");
        }

        foundTitlesList.setListData(foundTitles.toArray());
        foundTitlesList.repaint();
        scroll_pane_title_list.repaint();

        // for (int i = 0; i < foundTitles.size(); i++) {
        //     System.out.println(foundTitles.get(i));
        // }
    }

    public void backHome() {
        new homeGUI(receivedID);
        setVisible(false);
        dispose();
    }

    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {

                String selectedItem = (String) foundTitlesList.getSelectedValue();
                // System.out.println("found Title: " + selectedItem);

                new watchGUI(selectedItem, receivedID);
            }
        }
    };

}