import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Błażej on 2017-06-16.
 */
public class HighScore extends JFrame /*implements ActionListener*/ {
    private final int SCORES_AMOUNT = 5;
    private String[] Wyniki = new String[SCORES_AMOUNT];
    HighScore() throws FileNotFoundException{
        super("Najlepsze wyniki");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        Scanner fileIn = new Scanner(new File("highscore.txt"));
        JTextArea scores = new JTextArea();
        scores.setEditable(false);
        scores.setFocusable(false);
        while (fileIn.hasNext()) {
            scores.append(fileIn.nextLine());
            scores.append("\n");
        }

        setVisible(true);
        add(scores);
    }
}
