import sun.awt.ConstrainableGraphics;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends JFrame {

    private String username;
    private Connection homeConn;

    private JPanel mainCard;

    private JPanel homePanel;
    private JPanel profileWindow;
    private JPanel jobListWindow;
    private JPanel addJobWindow;
    private JPanel jobViewWindow;

    private JButton profileButton;
    private JButton homeButton;
    private JButton refreshButton;
    private JButton addJobButton;

    private JTable table1;

    private JPanel editProfilePanel;
    private JTextField editfirstname;
    private JTextField editlastname;
    private JTextField edituname;
    private JTextField editemail;
    private JTextField editphone;
    private JPasswordField editpass;
    private JPasswordField editpassconf;
    private JCheckBox editshowpass;
    private JButton editSaveButton;
    private JTextArea editbio;

    private JPanel userProfilePanel;
    private JLabel userProfileName;
    private JLabel userProfileUsername;
    private JTextPane userprofilebio;
    private JLabel userProfileEmail;
    private JLabel userProfilePhone;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public Home(String user){
        this.username = user;
        setTitle("Freelancer");
        setContentPane(homePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024,640));
        setSize(1024, 640);
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            this.homeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error: ", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                profilePanelFunction(username);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                homeFunction();
            }
        });

        editSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateUserProfile();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                profilePanelFunction("ponkaj");
            }
        });

        editshowpass.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent i) {
                if (i.getStateChange() == ItemEvent.SELECTED) {
                    editpass.setEchoChar((char) 0);
                    editpassconf.setEchoChar((char) 0);
                } else {
                    editpass.setEchoChar('•');
                    editpassconf.setEchoChar('•');
                }
            }
        });
    }







    //Profile
    private boolean validateEditProfile(){
        Matcher matcher = pattern.matcher(editemail.getText());
        if (editfirstname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter First Name");
            return false;
        }
        if (edituname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Username");
            return false;
        }
        if(!matcher.matches()){
            JOptionPane.showMessageDialog(null, "Enter valid Email");
            return false;
        }
        if (!Arrays.equals(editpass.getPassword(), editpassconf.getPassword())) {
            JOptionPane.showMessageDialog(null, "Validate passwords again");
            return false;
        }
        return true;
    }

    public void updateUserProfile(){
        if (validateEditProfile()){
            try {
                Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                PreparedStatement insertCreds = freeConn.prepareStatement(
                        "UPDATE users SET fname=?, lname=?, uname=?, bio=?, email=?, phone=?, password=MD5(?) WHERE uname=?;"
                );

                insertCreds.setString(1, editfirstname.getText());
                insertCreds.setString(2, editlastname.getText());
                insertCreds.setString(3, edituname.getText());
                insertCreds.setString(4, editbio.getText());
                insertCreds.setString(5, editemail.getText());
                insertCreds.setString(6, editphone.getText());
                insertCreds.setString(7, Arrays.toString(editpass.getPassword()));
                insertCreds.setString(8, this.username);
                insertCreds.executeUpdate();
                JOptionPane.showMessageDialog(null, "Profile Updated");
                this.username = edituname.getText();
            }
            catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Error : " +throwables.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
                throwables.printStackTrace();
            }
        }
    }

    private void profilePanelFunction(String username){
        try {
            PreparedStatement profileCredQuery = homeConn.prepareStatement("SELECT fname, lname, uname, bio, email, phone FROM users WHERE uname=?");
            profileCredQuery.setString(1, username);
            ResultSet profileCredentials = profileCredQuery.executeQuery();

            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

            if (profileCredentials.next()){
                userProfileName.setText(profileCredentials.getString("fname") + " " + profileCredentials.getString("lname"));
                userProfileUsername.setText("@" + profileCredentials.getString("uname"));
                userprofilebio.setParagraphAttributes(center, false);
                userprofilebio.setText(profileCredentials.getString("bio"));
                userProfileEmail.setText(profileCredentials.getString("email"));
                userProfilePhone.setText(profileCredentials.getString("phone"));

                editfirstname.setText(profileCredentials.getString("fname"));
                editlastname.setText(profileCredentials.getString("lname"));
                edituname.setText(profileCredentials.getString("uname"));
                editbio.setText(profileCredentials.getString("bio"));
                editemail.setText(profileCredentials.getString("email"));
                editphone.setText(profileCredentials.getString("phone"));
            }
            else {
                JOptionPane.showMessageDialog(null,"Invalid User : "+username, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null,throwables, "Error: ", JOptionPane.ERROR_MESSAGE);
            throwables.printStackTrace();
        }

        if(username.equals(this.username)){
            editProfilePanel.setEnabled(true);
            editProfilePanel.setVisible(true);
        }
        else {
            System.out.println(this.username + username);
            editProfilePanel.setEnabled(false);
            editProfilePanel.setVisible(false);
        }
        profileWindow.setVisible(true);
        jobListWindow.setVisible(false);
        jobViewWindow.setVisible(false);
        addJobWindow.setVisible(false);
    }









    private void homeFunction(){
        profileWindow.setVisible(false);
        jobListWindow.setVisible(true);
        jobViewWindow.setVisible(false);
        addJobWindow.setVisible(false);
    }

    private void jobViewFunction(){
        profileWindow.setVisible(false);
        jobListWindow.setVisible(false);
        jobViewWindow.setVisible(true);
        addJobWindow.setVisible(false);
    }

    private void addJobFunction(){
        profileWindow.setVisible(false);
        jobListWindow.setVisible(false);
        jobViewWindow.setVisible(false);
        addJobWindow.setVisible(true);
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
