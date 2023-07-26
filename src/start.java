import my_project.table;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static my_project.table.my_menubar;

public class start {
    private JPanel main_panel;
    private JPanel top_panel;
    private JPanel bottom_panel;
    private JTextField usernameTextField1;
    private JPasswordField passwordField1;
    private JButton loginBtn;
    private JTextField rfirst_nameTxtF;
    private JTextField rlastnameTextField;
    private JTextField rUsernameTextField;
    private JPasswordField passwordField2;
    private JButton registerButton;
    private JButton loginButton;
    private JButton registerButton1;
    private JPanel welcome_panel;

    static JFrame frame = new JFrame("Java Ambassadors Programming Club");
    static String url = "jdbc:mysql://localhost:3306/java_ambassadors_club";
    static String userName = "root";
    static String adminpassword = "";

    public start() {
        top_panel.setVisible(false);
        bottom_panel.setVisible(false);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome_panel.setVisible(false);
                top_panel.setVisible(true);
            }
        });
        registerButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome_panel.setVisible(false);
                bottom_panel.setVisible(true);
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = usernameTextField1.getText();
                String password = String.valueOf(passwordField1.getPassword());
                String sql = "SELECT * FROM members WHERE username = ? AND password = ?;";

                PreparedStatement statement = null;
                ResultSet resultSet = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, userName, adminpassword);

                    statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, password);

                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Welcome " + name, "Login Successful", JOptionPane.PLAIN_MESSAGE);
                        new table().showTable();
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username & Password", "", JOptionPane.ERROR_MESSAGE);
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String member = "SELECT * FROM members WHERE username = ? AND last_name = ?";
                String insertSql = "INSERT INTO members (id, first_name, last_name, username, password) VALUES (NULL, ?, ?, ?, ?)";

                PreparedStatement selectStatement = null;
                PreparedStatement insertStatement = null;
                ResultSet resultSet = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(url, userName, adminpassword);

                    selectStatement = conn.prepareStatement(member);
                    selectStatement.setString(1, rUsernameTextField.getText());
                    selectStatement.setString(2, rlastnameTextField.getText());
                    resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Member already exists.", "", JOptionPane.ERROR_MESSAGE);
                    } else {
                        insertStatement = conn.prepareStatement(insertSql);
                        insertStatement.setString(1, rfirst_nameTxtF.getText());
                        insertStatement.setString(2, rlastnameTextField.getText());
                        insertStatement.setString(3, rUsernameTextField.getText());
                        insertStatement.setString(4, String.valueOf(passwordField2.getPassword()));
                        insertStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Registered successfully.", "", JOptionPane.PLAIN_MESSAGE);
                        new table().showTable();

                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (selectStatement != null) {
                        selectStatement.close();
                    }
                    if (insertStatement != null) {
                        insertStatement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        frame.setBounds(430, 200, 599, 400);
        frame.setJMenuBar(my_menubar());
        frame.setContentPane(new start().main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
