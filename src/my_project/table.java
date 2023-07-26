package my_project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;


public class table {
    public static JMenuBar my_menubar() {

        //creating a menuBar
        JMenuBar jMenuBar = new JMenuBar();

        //creating menus
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");

        //creating menuItems for the menus above
        JMenuItem open = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem about = new JMenuItem("About");

        //setting Mnemonics for the menus
        file.setMnemonic(KeyEvent.VK_F);
        help.setMnemonic(KeyEvent.VK_H);

        //setting Accelerator for some menuItems
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

        //adding an actionListener to 'exit' menu item
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //adding an actionListener to 'result' menu item
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new existing_members();
                }catch (Exception s){
                    System.out.println("Error: " + s.getMessage());
                }
            }
        });

        //adding an actionListener to 'about' menu item
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Copyright information");
                frame.setVisible(true);
                frame.setSize(600, 300);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);

                JPanel panel = new JPanel();
                frame.add(panel);
                panel.setVisible(true);
                JTextArea textArea = new JTextArea("Group members:" +
                        "\n\n" + " 1\n" +
                        " 2\n" +
                        " 3\n" +
                        " 4\n" +
                        " 5\n" +
                        " 6\t\tD\n\n\n" +
                        " Course Title:\t\tOBJECT ORIENTED SOFTWARE DEVELOPMENT WITH JAVA II\n" +
                        " 2022/2023 Academic Year");
                textArea.setEditable(false);
                panel.add(textArea);
                panel.setBackground(Color.white);
            }
        });

        /*adding 'add' and 'exit' menu item to file
        menu with a separator - separating them*/
        file.add(open);
        file.addSeparator();
        file.add(exit);

        //adding 'about' menu item to help menu
        help.add(about);

        //adding all menu to the menuBar
        jMenuBar.add(file);
        jMenuBar.add(help);

        //returning the menuBar as it is required as the method's datatype
        return jMenuBar;
    }

    public void showTable(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/java_ambassadors_club";
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM members";
            ResultSet rs = stmt.executeQuery(sql);
            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame();
            frame.setTitle("Java Ambassadors Programming Club");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(430, 200, 599, 400);
            frame.setJMenuBar(my_menubar());

            // add search panel
            JPanel searchPanel = new JPanel();
            JTextField searchTextField = new JTextField(20);
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // getting the search query
                        String query = searchTextField.getText();


                        for (int i = 0; i < table.getRowCount(); i++) {
                            for (int j = 0; j < table.getColumnCount(); j++) {
                                String value = table.getValueAt(i, j).toString();
                                if (value.contains(query)) {
                                    // select the row and scroll to it
                                    table.setRowSelectionInterval(i, i);
                                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                                    return;
                                }
                            }
                        }

                        // if no match was found, display a message
                        JOptionPane.showMessageDialog(null, "No matching rows found");
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
            });

            // adding delete panel
            JPanel deletePanel = new JPanel();
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int row = table.getSelectedRow();
                        String id = table.getModel().getValueAt(row, 0).toString();
                        String deleteSql = "DELETE FROM members WHERE id=?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                        deleteStmt.setString(1, id);
                        int result = deleteStmt.executeUpdate();
                        if (result == 1) {
                            JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.removeRow(row);
                        }
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
            });
            deletePanel.add(searchTextField);
            deletePanel.add(searchButton);
            deletePanel.add(deleteButton);
            frame.add(deletePanel, BorderLayout.SOUTH);


            scrollPane.setViewportView(table);
            frame.add(scrollPane);
            frame.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        Object[][] data = new Object[0][columnCount];
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            data = addRow(data, row);
        }
        return new DefaultTableModel(data, columnNames);
    }

    public static Object[][] addRow(Object[][] data, Object[] row) {
        int length = data.length;
        Object[][] newData = new Object[length + 1][row.length];
        for (int i = 0; i < length; i++) {
            newData[i] = data[i];
        }
        newData[length] = row;
        return newData;
    }
}
