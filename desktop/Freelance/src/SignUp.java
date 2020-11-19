import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends JFrame {

    private JPanel mainPanel;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField uname;
    private JTextField email;
    private JTextField phone;
    private JButton signUpButton;
    private JPasswordField pass;
    private JPasswordField passconf;
    private JCheckBox showpass;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void checkDatabase(){
        try {
            System.out.println("Connecting to Database");

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
            System.out.println("Connected to Database");

            freeConn.close();
        }
        catch (SQLException sqlEx) {
            try {
                System.out.println("Connecting to MySql");
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection mySqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "password");
                System.out.println("Connected to MySql");
                PreparedStatement createDB = mySqlConn.prepareStatement("CREATE DATABASE IF NOT EXISTS freelancer;");
                createDB.executeUpdate();
                mySqlConn.close();

                Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                System.out.println("Connected to database 'freelancer'");
                PreparedStatement createTable = freeConn.prepareStatement("CREATE TABLE users ("
                        + "fname VARCHAR(32) NOT NULL,"
                        + "lname VARCHAR(32),"
                        + "uname VARCHAR(64) NOT NULL PRIMARY KEY,"
                        + "email VARCHAR(32) NOT NULL,"
                        + "phone VARCHAR(32),"
                        + "password VARCHAR(32) NOT NULL);"
                );
                createTable.executeUpdate();
                System.out.println("Table Created");
                freeConn.close();

            } catch (SQLException | ClassNotFoundException mySqlEx) {
                JOptionPane.showMessageDialog(null, mySqlEx.getMessage());
                mySqlEx.printStackTrace();
            }
        }
        catch (ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean signUpValidate(){
        Matcher matcher = pattern.matcher(email.getText());
        if (firstname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter First Name");
            return false;
        }
        else if (uname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Username");
            return false;
        }
        else if(!matcher.matches()){
            JOptionPane.showMessageDialog(null, "Enter valid Email");
            return false;
        }
        else if (!Arrays.equals(pass.getPassword(), passconf.getPassword())) {
            JOptionPane.showMessageDialog(null, "Validate passwords again");
            return false;
        }
        else
            return true;
    }

    public void registerUser(){

        if (signUpValidate()){
            checkDatabase();
            try {
                Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                PreparedStatement insertCreds = freeConn.prepareStatement(
                        "INSERT INTO users (fname, lname, uname, email, phone, password) VALUES (?, ? ,?, ?, ?, MD5(?));"
                );

                insertCreds.setString(1, firstname.getText());
                insertCreds.setString(2, lastname.getText());
                insertCreds.setString(3, uname.getText());
                insertCreds.setString(4, email.getText());
                insertCreds.setString(5, phone.getText());
                insertCreds.setString(6, Arrays.toString(pass.getPassword()));
                insertCreds.executeUpdate();

                JOptionPane.showMessageDialog(null, "Account Created !!!");
                dispose();
                new Login();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public SignUp(){
        setTitle("Freelancer - SignUp");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        showpass.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent i) {
                if (i.getStateChange() == ItemEvent.SELECTED) {
                    pass.setEchoChar((char) 0);
                    passconf.setEchoChar((char) 0);
                } else {
                    pass.setEchoChar('•');
                    passconf.setEchoChar('•');
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                registerUser();
            }
        });
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
