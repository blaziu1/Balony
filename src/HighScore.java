import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Odpowiada za wyswietlenie okna z graczami ktorzy uzyskali najlepszy wynik.
 */
class HighScore extends JFrame {
   // private final int SCORES_AMOUNT = 5;
   // private String[] Wyniki = new String[SCORES_AMOUNT];

    /**
     * Konstruktor, tworzy okno, w ktorym wyswietla wyniki graczy z najlepszymi wynikami.
     * Imiona graczy wraz z najlepszymi wynikami wczytywane sa z pliku tekstowego.
     * @throws FileNotFoundException Rzuca wyjatek jesli plik highscore.txt nie zostal odnaleziony.
     */
    HighScore() throws FileNotFoundException{
        super("Najlepsze wyniki");
        this.addWindowListener(new WindowAdapter() {
                                   public void windowClosing(WindowEvent e) {
                                       dispose();
                                       MainMenu mainmenu = new MainMenu();
                                   }
                               });
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
