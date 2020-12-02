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

    private final Utility utility;

    private JPanel mainPanel;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField uname;
    private JTextField email;
    private JTextField phone;
    private JTextArea bio;
    private JButton signUpButton;
    private JPasswordField pass;
    private JPasswordField passconf;
    private JCheckBox showpass;
    private JButton loginButton;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void registerUser(){

        if (utility.profileValidate(firstname, uname, email, pass, passconf)){
            this.utility.checkDatabase();
            try {
                //Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                Connection freeConn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/6RXBPbWeHI?autoReconnect=true", "6RXBPbWeHI", "7ZoObPuzQ6");

                PreparedStatement insertCreds = freeConn.prepareStatement(
                        "INSERT INTO users (fname, lname, uname, bio, email, phone, password) VALUES (?, ? ,?, ?, ?, ?, MD5(?));"
                );

                insertCreds.setString(1, firstname.getText());
                insertCreds.setString(2, lastname.getText());
                insertCreds.setString(3, uname.getText());
                insertCreds.setString(4, bio.getText());
                insertCreds.setString(5, email.getText());
                insertCreds.setString(6, phone.getText());
                insertCreds.setString(7, Arrays.toString(pass.getPassword()));
                insertCreds.executeUpdate();

                JOptionPane.showMessageDialog(null, "Account Created !!!\nLogin to continue");

                freeConn.close();
                dispose();
                new Login();
            }
            catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(), "Try Again", JOptionPane.ERROR_MESSAGE);
                utility.checkDatabase();
                throwables.printStackTrace();
            }
        }
    }

    public SignUp(){

        this.utility = new Utility();

        setTitle("Freelancer - SignUp");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 640));
        setSize(900, 640);
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new Login();
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //new SignUp();
    }
}
