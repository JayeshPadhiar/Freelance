import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");

                    if (signUpValidate()){
                        PreparedStatement insertCreds = (PreparedStatement) connection.prepareStatement(
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
                    }

                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException throwables) {
                    JOptionPane.showMessageDialog(null, throwables.getMessage());
                    throwables.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new SignUp();
    }

}
