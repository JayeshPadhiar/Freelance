import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    private JPanel homePanel;
    private JButton profileButton;
    private JPanel mainCard;
    private JPanel jobListWindow;
    private JPanel profileWindow;
    private JPanel addJobWindow;
    private JPanel jobViewWindow;
    private JButton addJobButton;
    private JTable table1;
    private JButton refreshButton;
    private JButton homeButton;
    private JTextField editfirstname;
    private JTextField editlastname;
    private JTextField edituname;
    private JCheckBox editshowpass;
    private JTextField editemail;
    private JTextField editphone;
    private JPasswordField editpass;
    private JPasswordField editpassconf;
    private JButton editSaveButton;
    private JTextArea editbio;
    private JPanel editProfilePanel;
    private JPanel userProfilePanel;
    private JLabel userProfileName;
    private JLabel userProfileUsername;
    private JTextPane userProfilebio;
    private JLabel userProfileEmail;
    private JLabel userProfilePhone;

    public Home(){
        setTitle("Freelancer");
        setContentPane(homePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024,640));
        setSize(1024, 640);
        setLocationRelativeTo(null);
        setVisible(true);
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                profileWindow.setVisible(true);
                jobListWindow.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new Home();
    }
}
