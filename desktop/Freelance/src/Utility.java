import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public Utility(){
    }

    public void clearPanel(JComponent panel){
        for(Component control : panel.getComponents()) {
            if(control instanceof JTextField) {
                JTextField textField = (JTextField) control;
                textField.setText(null);
            }
            else if (control instanceof JTextArea) {
                JTextArea textArea = (JTextArea) control;
                textArea.setText(null);
            }
            else if (control instanceof JPanel) {
                JPanel jPanel = (JPanel) control;
                clearPanel(jPanel);
            }
            else if (control instanceof JScrollPane) {
                System.out.println("Yeah baby !");
                JViewport vport = ((JScrollPane) control).getViewport();

                try {
                    JTextArea textArea = (JTextArea) vport.getView();
                    textArea.setText(null);
                }
                catch (Exception e){
                    JTextField textField = (JTextField) vport.getView();
                    textField.setText(null);
                }

                try {
                    JTextField textField = (JTextField) vport.getView();
                    textField.setText(null);
                }
                catch (Exception e){
                    JTextArea textArea = (JTextArea) vport.getView();
                    textArea.setText(null);
                }
            }
        }
    }

    public boolean profileValidate(JTextField firstname, JTextField uname, JTextField email, JPasswordField pass, JPasswordField passconf){
        Matcher matcher = pattern.matcher(email.getText());
        if (firstname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter First Name");
            return false;
        }
        if (uname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Username");
            return false;
        }
        if(!matcher.matches()){
            JOptionPane.showMessageDialog(null, "Enter valid Email");
            return false;
        }
        if (!Arrays.equals(pass.getPassword(), passconf.getPassword())) {
            JOptionPane.showMessageDialog(null, "Validate passwords again");
            return false;
        }
        return true;
    }

    public boolean jobPostValidate(JTextField jobTitle, JTextField jobDue, JTextField jobCost){
        if (jobTitle.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Job Title");
            return false;
        }
        if (jobDue.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Due Date");
            return false;
        }
        if (jobCost.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Preferred Cost");
            return false;
        }
        return true;
    }

    public boolean jobApplyValidate(JTextArea jobAppl, JTextField jobApplCost){
        if (jobAppl.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Job Application");
            return false;
        }

        if (jobApplCost.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Preferred Cost");
            return false;
        }
        return true;
    }

    public void setCellsAlignment(JTable table, int alignment)
    {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(alignment);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(cellRenderer);
        }
    }

    public void setHeaderAlignment(JTable table, int alignment)
    {
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(alignment);
    }



    public void initDatabase(){
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
            PreparedStatement createTableUsers = freeConn.prepareStatement("CREATE TABLE IF NOT EXISTS users ("
                    + "fname VARCHAR(32) NOT NULL,"
                    + "lname VARCHAR(32),"
                    + "uname VARCHAR(64) NOT NULL PRIMARY KEY,"
                    + "bio TEXT,"
                    + "email VARCHAR(32) NOT NULL,"
                    + "phone VARCHAR(32),"
                    + "password VARCHAR(32) NOT NULL);"
            );
            createTableUsers.executeUpdate();
            System.out.println("Table Created : users");

            PreparedStatement createTableJobs = freeConn.prepareStatement("CREATE TABLE IF NOT EXISTS jobs ("
                    + "jobid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "jobtitle TEXT NOT NULL,"
                    + "author VARCHAR(64) NOT NULL,"
                    + "jobdesc TEXT,"
                    + "jobdue DATE NOT NULL,"
                    + "jobcost DECIMAL(8,2) NOT NULL,"
                    + "applied JSON,"
                    + "FOREIGN KEY (author) REFERENCES users(uname) ON DELETE CASCADE ON UPDATE CASCADE);"
            );
            createTableJobs.executeUpdate();
            System.out.println("Table Created : jobs");

            System.out.println("Tables Created");
            freeConn.close();

            JOptionPane.showMessageDialog(null, "Database Initialized Please Try Again");

        } catch (SQLException | ClassNotFoundException mySqlEx) {
            JOptionPane.showMessageDialog(null, mySqlEx.getMessage());
            mySqlEx.printStackTrace();
        }
    }

    public void checkDatabase(){
        try {
            System.out.println("Connecting to Database");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection freeConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancer", "root", "password");

            DatabaseMetaData dbm = freeConn.getMetaData();
            ResultSet userTable = dbm.getTables(null, null, "users", null);
            if (userTable.next()) {
                System.out.println("Table users Exists");
            }
            else {
                initDatabase();
            }

            ResultSet jobTable = dbm.getTables(null, null, "jobs", null);
            if (jobTable.next()) {
                System.out.println("Table jobs Exists");
            }
            else {
                initDatabase();
            }

            System.out.println("Connected to Database");
            freeConn.close();
        }
        catch (SQLException sqlEx) {
            initDatabase();
        }
        catch (ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
