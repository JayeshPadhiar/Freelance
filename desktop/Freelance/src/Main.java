import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Main extends JFrame {

    private JPanel mainPanel;
    private JPanel loginPanel;
    private JTextField username;
    private JPasswordField password;
    private JLabel unameLabel;
    private JLabel passLabel;
    private JButton loginButton;
    private JCheckBox showPass;
    private JButton signUpButton;

    public Main(){
        setTitle("Freelancer");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900,420));
        setSize(900, 420);
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
                new SignUp();
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
