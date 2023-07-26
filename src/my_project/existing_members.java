package my_project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class existing_members extends JFrame {
    public existing_members() {
        try {
            // Connecting to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_ambassadors_club", "root", "");

            // Executing the query
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT first_name, last_name FROM members");

            // Creating a table model and populating it with the data from the result set
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("First Name");
            model.addColumn("Last Name");
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("first_name");
                row[1] = rs.getString("last_name");
                model.addRow(row);
            }

            // Creating a JTable and setting it model
            JTable table = new JTable(model);

            // Adding the table to a scroll pane
            JScrollPane scrollPane = new JScrollPane(table);

            // Adding the scroll pane to the frame
            add(scrollPane);

            // Setting the frame properties
            setTitle("List of registered members");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setVisible(true);
            setResizable(false);

            // Closing the database connection
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
