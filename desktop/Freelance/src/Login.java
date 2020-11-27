import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Arrays;

public class Login extends JFrame {

    private Utility utility;

    private JPanel loginPanel;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JCheckBox showPass;
    private JButton signUpButton;
    private JLabel title;

    public Login(){

        this.utility = new Utility();

        setTitle("Freelancer");
        setContentPane(loginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900,440));
        setSize(900, 440);
        setLocationRelativeTo(null);
        setVisible(true);

        showPass.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent i) {
                if (i.getStateChange() == ItemEvent.SELECTED) {
                    password.setEchoChar((char) 0);
                } else {
                    password.setEchoChar('•');
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new SignUp();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                    //Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/6RXBPbWeHI?autoReconnect=true", "6RXBPbWeHI", "7ZoObPuzQ6");

                    String uname = username.getText();
                    String pass = Arrays.toString(password.getPassword());

                    PreparedStatement loginStatement = mysqlConn.prepareStatement("SELECT * FROM users WHERE uname=? AND password=MD5(?)");
                    loginStatement.setString(1, uname);
                    loginStatement.setString(2, pass);
                    ResultSet loginResult = loginStatement.executeQuery();

                    if (loginResult.next()){
                        new Home(uname);
                        JOptionPane.showMessageDialog(null, "Welcome " + loginResult.getString("fname") + " " + loginResult.getString("lname") + " !");
                        dispose();
                        mysqlConn.close();
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Invalid Username of Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    mysqlConn.close();

                } catch (SQLException | ClassNotFoundException throwables) {
                    JOptionPane.showMessageDialog(null,throwables, "Error", JOptionPane.ERROR_MESSAGE);
                    utility.checkDatabase();
                    throwables.printStackTrace();
                }
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
        new Login();
    }
}
