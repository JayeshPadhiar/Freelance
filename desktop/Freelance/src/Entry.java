import javax.swing.*;
import java.awt.*;

public class Entry extends JFrame {
    private JPanel entryPanel;

    public Entry(){
        setTitle("Freelancer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(entryPanel);
        setSize(500,400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args){
        new Entry();
    }
}
