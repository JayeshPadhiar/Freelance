import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Arrays;

public class Home extends JFrame {

    private String username;
    private Connection homeConn;

    private final Utility utility;

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

    private JPanel addJobPanel;
    private JPanel applyJobPanel;
    private JPanel userProfilePanel;


    private JLabel userProfileName;
    private JLabel userProfileUsername;
    private JTextPane userprofilebio;
    private JLabel userProfileEmail;
    private JLabel userProfilePhone;

    private JTextField newJobTitle;
    private JTextArea newJobDesc;
    private JTextField newJobDue;
    private JTextField newJobCost;
    private JButton newJobPostButton;

    private JLabel jobviewtitle;
    private JLabel jobviewauthor;
    private JTextPane jobviewdesc;
    private JLabel jobviewdue;
    private JLabel jobviewcost;
    private JButton jobViewApplyButton;
    private JButton jobViewCancelButton;

    private JTextArea applyapplication;
    private JTextField applycost;

    public Home(String user){
        this.username = user;
        this.utility = new Utility();

        setTitle("Freelancer");
        setContentPane(homePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024,768));
        setSize(1024, 768);
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

        addJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //utility.clearPanel();
                addJobFunction();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jobViewFunction(18);
            }
        });

        editSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateUserProfile();
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

        newJobPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (utility.jobPostValidate(newJobTitle, newJobDue, newJobCost)){
                    utility.checkDatabase();
                    try {
                        Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
                        PreparedStatement insertJobPost = freeConn.prepareStatement(
                                "INSERT INTO jobs (jobtitle, author, jobdesc, jobdue, jobcost) VALUES (?, ? ,?, ?, ?);"
                        );

                        insertJobPost.setString(1, newJobTitle.getText());
                        insertJobPost.setString(2, username);
                        insertJobPost.setString(3, newJobDesc.getText());
                        insertJobPost.setString(4, newJobDue.getText());
                        insertJobPost.setString(5, newJobCost.getText());
                        insertJobPost.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Job Posted!");

                        utility.clearPanel(addJobPanel);

                        /*newJobTitle.setText(null);
                        newJobDesc.setText(null);
                        newJobDue.setText(null);
                        newJobCost.setText(null);*/

                        freeConn.close();
                    }
                    catch (SQLException throwables) {
                        JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(), "Try Again", JOptionPane.ERROR_MESSAGE);
                        utility.checkDatabase();
                        throwables.printStackTrace();
                    }
                }
            }
        });

        jobViewApplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(applyJobPanel.isEnabled() && jobViewCancelButton.isEnabled() && applyJobPanel.isVisible() && jobViewCancelButton.isVisible()){

                    //utility.clearPanel(applyJobPanel);

                    System.out.println("Is Enabled !");

                    //applyJobPanel.setVisible(false);
                    //jobViewCancelButton.setVisible(false);

                    applyJobPanel.setEnabled(false);
                    jobViewCancelButton.setEnabled(false);

                    jobViewWindow.revalidate();

                }else{
                    applyJobPanel.setEnabled(true);
                    applyJobPanel.setVisible(true);

                    jobViewCancelButton.setEnabled(true);
                    jobViewCancelButton.setVisible(true);

                    jobViewWindow.revalidate();
                }
            }
        });
    }



    public void updateUserProfile(){
        if (utility.profileValidate(editfirstname, edituname, editemail, editpass, editpassconf)){
            this.utility.checkDatabase();
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

                profilePanelFunction(this.username);

            }
            catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Error : " +throwables.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
                this.utility.checkDatabase();
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
            this.utility.checkDatabase();
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

        userProfilePanel.revalidate();
        profileWindow.revalidate();

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

    private void jobViewFunction(int jobID){

        try {
            PreparedStatement jobViewQuery = homeConn.prepareStatement("SELECT jobtitle, author, jobdesc, jobdue, jobcost FROM jobs WHERE jobid=?");
            jobViewQuery.setInt(1, jobID);
            ResultSet jobCreds = jobViewQuery.executeQuery();

            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

            if (jobCreds.next()){
                jobviewtitle.setText(jobCreds.getString("jobtitle"));
                jobviewauthor.setText("By : @" + jobCreds.getString("author"));
                jobviewdesc.setParagraphAttributes(center, false);
                jobviewdesc.setText(jobCreds.getString("jobdesc"));
                jobviewdue.setText("Due Date : " + jobCreds.getString("jobdue"));
                jobviewcost.setText("Est. Cost : " + jobCreds.getString("jobcost"));
            }
            else {
                JOptionPane.showMessageDialog(null,"Job not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null,throwables, "Error: ", JOptionPane.ERROR_MESSAGE);
            this.utility.checkDatabase();
            throwables.printStackTrace();
        }

        /*if(username.equals(this.username)){
            editProfilePanel.setEnabled(true);
            editProfilePanel.setVisible(true);
        }
        else {
            System.out.println(this.username + username);
            editProfilePanel.setEnabled(false);
            editProfilePanel.setVisible(false);
        }*/

        jobViewWindow.revalidate();

        profileWindow.setVisible(false);
        jobListWindow.setVisible(false);
        jobViewWindow.setVisible(true);
        addJobWindow.setVisible(false);
    }

    private void addJobFunction(){
        profileWindow.setVisible(false);
        jobListWindow.setVisible(false);
        addJobWindow.setVisible(true);
        jobViewWindow.setVisible(false);
    }


    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/

        new Home("jay");
    }
}
