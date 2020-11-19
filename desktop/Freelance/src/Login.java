import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Arrays;

public class Login extends JFrame {

    private JPanel loginPanel;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JCheckBox showPass;
    private JButton signUpButton;
    private JLabel title;

    public Login(){
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
                    Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");

                    PreparedStatement loginStatement = mysqlConn.prepareStatement("SELECT * FROM users WHERE uname=? AND password=MD5(?)");

                    loginStatement.setString(1, username.getText());
                    loginStatement.setString(2, Arrays.toString(password.getPassword()));

                    ResultSet loginResult = loginStatement.executeQuery();

                    if (loginResult.next()){
                        JOptionPane.showMessageDialog(null, "Welcome " + loginResult.getString("fname") + " " + loginResult.getString("lname") + " !");
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Waapas se kar chutiye");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new Login();
    }
}
