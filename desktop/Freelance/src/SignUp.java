import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SignUp extends JFrame {

    private JPanel mainPanel;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField uname;
    private JTextField password;
    private JTextField email;
    private JTextField phone;
    private JButton signUpButton;
    private JPasswordField pass;
    private JPasswordField passconf;
    private JCheckBox showpass;

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
    }

    public static void main(String[] args) {
        new SignUp();
    }

}
