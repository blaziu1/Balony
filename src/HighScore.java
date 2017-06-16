import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Błażej on 2017-06-16.
 */
public class HighScore extends JFrame /*implements ActionListener*/ {
    private String[] Wyniki = new String[5];
    HighScore(){
        super("Najlepsze wyniki");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
