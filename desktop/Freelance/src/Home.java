import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    private JPanel homePanel;
    private JButton profile;
    private JPanel mainCard;
    private JPanel jobListWindow;
    private JPanel profileWindow;
    private JPanel addJobWindow;
    private JPanel jobViewWindow;
    private JButton profileButton;
    private JButton jobsButton;

    public Home(){
        setTitle("Home");
        setContentPane(homePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,640));
        setSize(800, 640);
        setLocationRelativeTo(null);
        setVisible(true);
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                profileWindow.setVisible(true);
                jobListWindow.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/

        new Home();
    }
}
