import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;

public class Home extends JFrame {

    private int currJobId;
    private String username;
    private String currJobAuthor;
    private Connection homeConn;
    private final Utility utility;

    private JPanel mainCard;

    private JPanel homePanel;
    private JPanel profileWindow;
    private JPanel homeWindow;
    private JPanel addJobWindow;
    private JPanel jobViewWindow;

    private JButton profileButton;
    private JButton homeButton;
    private JButton myProjectsButton;
    private JButton addJobButton;

    private JTable homeJobTable;

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
    private JPanel jobViewPanel;
    private JPanel applyJobPanel;
    private JPanel userProfilePanel;


    private JLabel userProfileName;
    private JLabel userProfileUsername;
    private JTextPane userprofilebio;
    private JLabel userProfileEmail;
    private JLabel userProfilePhone;
    private JTable userProfileJobsTable;

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
    private JTable jobViewApplicationTable;
    private JButton jobViewApplyButton;
    private JButton jobViewCancelButton;
    private JButton jobViewDeleteJobButton;
    private JButton jobViewDeleteJobApplButton;

    private JTextArea applyapplication;
    private JTextField applycost;
    private JButton logoutButton;
    private JButton jobViewAcceptJobApplButton;
    private JLabel homeLabel;


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
            Class.forName("com.mysql.cj.jdbc.Driver");
            //this.homeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");
            this.homeConn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/6RXBPbWeHI?autoReconnect=true",
                    "6RXBPbWeHI", "7ZoObPuzQ6");

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Error: ", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        homeFunction(false);

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                profilePanelFunction(username);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                homeFunction(false);
            }
        });

        addJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //utility.clearPanel();
                addJobFunction();
            }
        });

        myProjectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                homeFunction(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int confLogout = JOptionPane.showConfirmDialog(null,"Logout ?", "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(confLogout == JOptionPane.YES_OPTION){
                    try {
                        homeConn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    dispose();
                    new Login();
                }
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
                        PreparedStatement insertJobPost = homeConn.prepareStatement(
                                "INSERT INTO jobs (jobtitle, author, jobdesc, jobdue, jobcost) VALUES (?, ?, ?, ?, ?);"
                        );

                        insertJobPost.setString(1, newJobTitle.getText());
                        insertJobPost.setString(2, username);
                        insertJobPost.setString(3, newJobDesc.getText());
                        insertJobPost.setString(4, newJobDue.getText());
                        insertJobPost.setString(5, newJobCost.getText());
                        insertJobPost.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Job Posted!");

                        utility.clearPanel(addJobPanel);

                        //freeConn.close();
                    }
                    catch (SQLException throwables) {
                        JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                                "Try Again", JOptionPane.ERROR_MESSAGE);
                        utility.checkDatabase();
                        throwables.printStackTrace();
                    }
                }
            }
        });

        jobViewApplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(applyJobPanel.isEnabled() && jobViewCancelButton.isEnabled()){
                    System.out.println("Is Enabled !");

                    if(utility.jobApplyValidate(applyapplication,applycost)){
                        utility.checkDatabase();
                        try {

                            /*
                            PreparedStatement applyJob = homeConn.prepareStatement(
                                    "UPDATE jobs SET applied = JSON_ARRAY_APPEND(" +
                                            "COALESCE(applied, '[]'), '$', JSON_OBJECT('uname', ?, 'application', ?, 'cost', ?)) WHERE jobid=?;"
                            );
                            applyJob.setString(1, username);
                            applyJob.setString(2, applyapplication.getText());
                            applyJob.setString(3, applycost.getText());
                            applyJob.setInt(4, currJobId);
                            applyJob.executeUpdate();
                            */

                            PreparedStatement applyJob = homeConn.prepareStatement("INSERT INTO applications(jobid, applicant, application, cost) " +
                                    "VALUES (?, ?, ?, ?)");
                            applyJob.setInt(1, currJobId);
                            applyJob.setString(2, username);
                            applyJob.setString(3, applyapplication.getText());
                            applyJob.setString(4, applycost.getText());
                            applyJob.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Job Applied!");
                            utility.clearPanel(applyJobPanel);

                            applyJobPanel.revalidate();
                            jobViewFunction(currJobId);
                        }
                        catch (SQLException throwables) {
                            JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                                    "Try Again", JOptionPane.ERROR_MESSAGE);
                            utility.checkDatabase();
                            throwables.printStackTrace();
                        }
                        applyJobPanel.setVisible(false);
                        applyJobPanel.setEnabled(false);
                        jobViewCancelButton.setEnabled(false);
                        jobViewWindow.revalidate();
                    }

                }else{
                    utility.clearPanel(applyJobPanel);

                    applyJobPanel.setEnabled(true);
                    applyJobPanel.setVisible(true);

                    applyapplication.setEditable(true);
                    applycost.setEditable(true);

                    jobViewCancelButton.setEnabled(true);
                    jobViewCancelButton.setVisible(true);

                    jobViewWindow.revalidate();
                }
            }
        });

        jobViewDeleteJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    PreparedStatement deleteJob = homeConn.prepareStatement(
                            "DELETE FROM jobs WHERE jobid = ?;"
                    );
                    deleteJob.setInt(1, currJobId);
                    deleteJob.execute();

                    JOptionPane.showMessageDialog(null, "Job Post Deleted Successfully");
                    currJobId = -1;
                    currJobAuthor = null;
                    homeFunction(false);

                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                    throwables.printStackTrace();
                }
            }
        });

        jobViewCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                utility.clearPanel(applyJobPanel);
                applyJobPanel.setVisible(false);

                jobViewAcceptJobApplButton.setEnabled(false);
                jobViewAcceptJobApplButton.setVisible(false);

                jobViewCancelButton.setEnabled(false);

                jobViewWindow.revalidate();
            }
        });

        jobViewDeleteJobApplButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    /*
                    PreparedStatement deleteJobAppl = homeConn.prepareStatement(
                            "UPDATE jobs SET applied=JSON_REMOVE(applied, SUBSTR(JSON_UNQUOTE(" +
                                    "JSON_SEARCH(applied, 'one', ?)), 1, LOCATE('.', JSON_UNQUOTE(JSON_SEARCH(applied, 'one', ?)))-1)) WHERE jobid=?;"
                    );
                    deleteJobAppl.setString(1, username);
                    deleteJobAppl.setString(2, username);
                    deleteJobAppl.setInt(3, currJobId);
                    deleteJobAppl.executeUpdate();
                    */

                    PreparedStatement deleteJobAppl = homeConn.prepareStatement(
                            "DELETE FROM applications WHERE jobid=? AND applicant=?;"
                    );
                    deleteJobAppl.setInt(1, currJobId);
                    deleteJobAppl.setString(2, username);
                    deleteJobAppl.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Job Application Deleted Successfully");

                    jobViewFunction(currJobId);

                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                    throwables.printStackTrace();
                }
            }
        });

        homeJobTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (mouseEvent.getClickCount() == 2){
                    int row = homeJobTable.getSelectedRow();
                    int column = homeJobTable.getSelectedColumn();
                    jobViewFunction((Integer) homeJobTable.getValueAt(row, 0));
                    //JOptionPane.showMessageDialog(null, homeJobTable.getValueAt(row, 0));
                }
            }
        });

        userProfileJobsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (mouseEvent.getClickCount() == 2){
                    int row = userProfileJobsTable.getSelectedRow();
                    int column = userProfileJobsTable.getSelectedColumn();
                    jobViewFunction((Integer) userProfileJobsTable.getValueAt(row, 0));
                    //JOptionPane.showMessageDialog(null, homeJobTable.getValueAt(row, 0));
                }
            }
        });

        jobViewApplicationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (mouseEvent.getClickCount() == 2){
                    int row = jobViewApplicationTable.getSelectedRow();
                    int column = jobViewApplicationTable.getSelectedColumn();
                    profilePanelFunction((String) jobViewApplicationTable.getValueAt(row, 0));
                    //viewApplication(jobViewApplicationTable.getValueAt(row, 0), jobViewApplicationTable.getValueAt(row, 1));
                    //JOptionPane.showMessageDialog(null, homeJobTable.getValueAt(row, 0));
                }

                if (mouseEvent.getClickCount() == 1){
                    int row = jobViewApplicationTable.getSelectedRow();
                    int column = jobViewApplicationTable.getSelectedColumn();
                    //profilePanelFunction((String) jobViewApplicationTable.getValueAt(row, 0));
                    //viewApplication(jobViewApplicationTable.getValueAt(row, 1), jobViewApplicationTable.getValueAt(row, 2));
                    //JOptionPane.showMessageDialog(null, homeJobTable.getValueAt(row, 0));

                    jobViewCancelButton.setEnabled(true);
                    applyJobPanel.setVisible(true);
                    applyJobPanel.setEnabled(false);
                    applyapplication.setEditable(false);
                    applycost.setEditable(false);

                    if(currJobAuthor.equals(username)){
                        jobViewAcceptJobApplButton.setEnabled(true);
                        jobViewAcceptJobApplButton.setVisible(true);
                    }

                    SimpleAttributeSet center = new SimpleAttributeSet();
                    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

                    //applyapplication.setParagraphAttributes(center, false);
                    applyapplication.setText((String) jobViewApplicationTable.getValueAt(row, 1));
                    applycost.setText(String.valueOf(jobViewApplicationTable.getValueAt(row, 2)));

                    jobViewWindow.revalidate();
                }
            }
        });

        jobviewauthor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                profilePanelFunction(currJobAuthor);
            }
        });

        jobViewAcceptJobApplButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    int selectedRow = jobViewApplicationTable.getSelectedRow();
                    String applicant = (String) jobViewApplicationTable.getValueAt(selectedRow, 0);

                    int confLogout = JOptionPane.showConfirmDialog(null,"Do you want to select " +
                                    applicant + " for this job ?", "Select Applicant For This Job",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if(confLogout == JOptionPane.YES_OPTION){
                        PreparedStatement acceptJobAppl = homeConn.prepareStatement("UPDATE jobs SET closedto=? WHERE jobid=?");
                        acceptJobAppl.setString(1, applicant);
                        acceptJobAppl.setInt(2, currJobId);
                        acceptJobAppl.executeUpdate();
                        JOptionPane.showMessageDialog(null, "This Job has been given to " +
                                applicant + ".\nVisit the applicant profile for contact information");
                        jobViewFunction(currJobId);
                    }

                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void updateUserProfile(){
        if (utility.profileValidate(editfirstname, edituname, editemail, editpass, editpassconf)){
            this.utility.checkDatabase();
            try {
                PreparedStatement insertCreds = homeConn.prepareStatement(
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

        homeLabel.setText("Profile");

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

                DefaultTableModel profileViewJobTableModel = new DefaultTableModel(new String[]{"ID" ,"Job Title", "Due", "Cost"}, 0){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                try {
                    PreparedStatement getJobsTable = homeConn.prepareStatement("SELECT jobid, jobtitle, jobdue, jobcost FROM jobs WHERE author=?");
                    getJobsTable.setString(1, username);
                    ResultSet jobTable = getJobsTable.executeQuery();

                    while(jobTable.next())
                    {
                        int id = jobTable.getInt("jobid");
                        String title = jobTable.getString("jobtitle");
                        String due = jobTable.getString("jobdue");
                        double cost = jobTable.getDouble("jobcost");
                        profileViewJobTableModel.addRow(new Object[]{id, title, due, cost});
                    }

                    userProfileJobsTable.setModel(profileViewJobTableModel);
                    userProfileJobsTable.setRowSelectionAllowed(true);
                    userProfileJobsTable.setColumnSelectionAllowed(false);
                    userProfileJobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    utility.setCellsAlignment(userProfileJobsTable, SwingConstants.CENTER);
                    utility.setHeaderAlignment(userProfileJobsTable, SwingConstants.CENTER);
                    //TableColumnModel columnModel = jobViewApplicationTable.getColumnModel();
                    //columnModel.getColumn(0).setMaxWidth(60);
                    //columnModel.getColumn(1).setPreferredWidth((int)(homeWindow.getWidth()/2.5));
                }
                catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                    utility.checkDatabase();
                    throwables.printStackTrace();
                }

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
        homeWindow.setVisible(false);
        jobViewWindow.setVisible(false);
        addJobWindow.setVisible(false);
    }

    private void homeFunction(boolean selectedView){
        profileWindow.setVisible(false);
        homeWindow.setVisible(true);
        jobViewWindow.setVisible(false);
        addJobWindow.setVisible(false);

        if(!selectedView){
            homeLabel.setText("Home");
        }else {
            homeLabel.setText("Current Projects");
        }


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        DefaultTableModel jobsModel = new DefaultTableModel(new String[]{"ID" ,"Job", "Author", "Due", "Cost"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {

            PreparedStatement getJobsTable;

            if(!selectedView){
                System.out.println("Showing Home View");
                getJobsTable = homeConn.prepareStatement("SELECT jobid, jobtitle, author, jobdue, jobcost FROM jobs WHERE closedto IS NULL;");
            }else{
                System.out.println("Showing Close View");
                getJobsTable = homeConn.prepareStatement("SELECT jobid, jobtitle, author, jobdue, jobcost FROM jobs WHERE closedto=?;");
                getJobsTable.setString(1, username);
            }

            ResultSet jobsTable = getJobsTable.executeQuery();
            while(jobsTable.next())
            {
                int id = jobsTable.getInt("jobid");
                String title = jobsTable.getString("jobtitle");
                String author = jobsTable.getString("author");
                String due = jobsTable.getString("jobdue");
                String cost = jobsTable.getString("jobcost");
                jobsModel.addRow(new Object[]{id, title, author, due, cost});
            }
            homeJobTable.setModel(jobsModel);
            homeJobTable.setRowSelectionAllowed(true);
            homeJobTable.setColumnSelectionAllowed(false);
            homeJobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            utility.setCellsAlignment(homeJobTable, SwingConstants.CENTER);
            utility.setHeaderAlignment(homeJobTable, SwingConstants.CENTER);

            TableColumnModel columnModel = homeJobTable.getColumnModel();
            columnModel.getColumn(0).setMaxWidth(60);
            columnModel.getColumn(1).setPreferredWidth((int)(homeWindow.getWidth()/2.5));
        }
        catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(), "Try Again", JOptionPane.ERROR_MESSAGE);
            utility.checkDatabase();
            throwables.printStackTrace();
        }
    }

    private void jobViewFunction(int jobID){

        homeLabel.setText("Job");

        try {
            PreparedStatement jobViewQuery = homeConn.prepareStatement("SELECT jobid, jobtitle, author, jobdesc, " +
                    "jobdue, jobcost, closedto FROM jobs WHERE jobid=?");
            jobViewQuery.setInt(1, jobID);
            ResultSet jobCreds = jobViewQuery.executeQuery();

            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            if (jobCreds.next()){
                currJobId = jobCreds.getInt("jobid");
                currJobAuthor = jobCreds.getString("author");
                jobviewtitle.setText(jobCreds.getString("jobtitle"));
                jobviewauthor.setText("<HTML><a href=''>" + "@" + jobCreds.getString("author") + "</a><HTML>");
                jobviewdesc.setParagraphAttributes(center, false);
                jobviewdesc.setText(jobCreds.getString("jobdesc"));
                jobviewdue.setText("Due Date : " + jobCreds.getString("jobdue"));
                jobviewcost.setText("Est. Cost : " + jobCreds.getString("jobcost"));

                DefaultTableModel applTableModel = new DefaultTableModel(new String[]{"User" ,"Job Application", "Cost"}, 0){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                try {

                    PreparedStatement getApplTable = homeConn.prepareStatement("SELECT * FROM applications WHERE jobid=?");
                    getApplTable.setInt(1, currJobId);
                    ResultSet applTable = getApplTable.executeQuery();

                    while(applTable.next())
                    {
                        String uname = applTable.getString("applicant");
                        String application = applTable.getString("application");
                        double cost = applTable.getDouble("cost");
                        applTableModel.addRow(new Object[]{uname, application, cost});
                    }

                    jobViewApplicationTable.setModel(applTableModel);
                    jobViewApplicationTable.setRowSelectionAllowed(true);
                    jobViewApplicationTable.setColumnSelectionAllowed(false);
                    jobViewApplicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    utility.setCellsAlignment(jobViewApplicationTable, SwingConstants.CENTER);
                    utility.setHeaderAlignment(jobViewApplicationTable, SwingConstants.CENTER);
                    //TableColumnModel columnModel = jobViewApplicationTable.getColumnModel();
                    //columnModel.getColumn(0).setMaxWidth(60);
                    //columnModel.getColumn(1).setPreferredWidth((int)(homeWindow.getWidth()/2.5));
                }
                catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Error : " + throwables.getMessage(),
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                    utility.checkDatabase();
                    throwables.printStackTrace();
                }

                if(this.username.equals(jobCreds.getString("author"))){
                    jobViewApplyButton.setEnabled(false);
                    jobViewDeleteJobButton.setEnabled(true);
                    jobViewDeleteJobApplButton.setEnabled(false);
                    jobViewCancelButton.setEnabled(false);
                }
                else{

                    PreparedStatement checkIfApplied = homeConn.prepareStatement("SELECT * FROM applications WHERE applicant=? and jobid=?");
                    checkIfApplied.setString(1, this.username);
                    checkIfApplied.setString(2, jobCreds.getString("jobid"));
                    ResultSet applied = checkIfApplied.executeQuery();

                    if (!applied.next()){
                        System.out.println("Not applied");
                        jobViewApplyButton.setEnabled(true);
                        jobViewDeleteJobButton.setEnabled(false);
                        jobViewDeleteJobApplButton.setEnabled(false);
                        jobViewCancelButton.setEnabled(false);
                    }
                    else{
                        System.out.println("Already Applied");
                        jobViewApplyButton.setEnabled(false);
                        jobViewDeleteJobButton.setEnabled(false);
                        jobViewDeleteJobApplButton.setEnabled(true);
                        jobViewCancelButton.setEnabled(false);

                        if(this.username.equals(jobCreds.getString("closedto"))){
                            System.out.println("Selected for the Job cannot delete application");
                            jobViewDeleteJobApplButton.setEnabled(false);
                        }
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"Job not found.", "Error", JOptionPane.ERROR_MESSAGE);
                profilePanelFunction(username);
            }
        }
        catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null,throwables, "Error: ", JOptionPane.ERROR_MESSAGE);
            this.utility.checkDatabase();
            throwables.printStackTrace();
        }

        profileWindow.setVisible(false);
        homeWindow.setVisible(false);
        jobViewWindow.setVisible(true);
        addJobWindow.setVisible(false);
        applyJobPanel.setVisible(false);

        jobViewWindow.revalidate();
    }

    private void addJobFunction(){

        homeLabel.setText("Post your own Job");

        profileWindow.setVisible(false);
        homeWindow.setVisible(false);
        addJobWindow.setVisible(true);
        jobViewWindow.setVisible(false);
    }

    private void viewApplication(String application, double cost){
        applyJobPanel.setVisible(true);
        applyJobPanel.setEnabled(false);

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        applyapplication.setText(application);
        applycost.setText(String.valueOf(cost));

        jobViewWindow.revalidate();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
