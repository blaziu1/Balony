import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Klasa wyswietlajaca okno z wyborem poziomu trudnosci gry.
 */
public class Options extends JFrame implements ActionListener {
    private JButton latwy;
    private JButton sredni;
    private JButton trudny;

    /**
     * Konstruktor, tworzy okno z trzema przyciskami umozliwiajacymi wybor poziomu trudnosci.
     * Poziom trudnosci (ilosc kolorow kulek na mapie) jest wczytywany z pliku tekstowego.
     */
    Options(){

        super("Wybór poziomu trudności");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,3));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                MainMenu mainMenu = new MainMenu();
            }
        });
        latwy = new JButton("Łatwy");
        sredni = new JButton("Średni");
        trudny = new JButton("Trudny");
        latwy.addActionListener(this);
        sredni.addActionListener(this);
        trudny.addActionListener(this);

        add(latwy);
        add(sredni);
        add(trudny);

        setVisible(true);
    }

    /**
     * Wywolywana po wcisnieciu jednego z wyswietlanych przyciskow.
     * Zmienia ilosc kolorow kulek wyswietlanych na ekranie w zaleznosci od wcisnietego przycisku.
     * Po wcisnieciu przycisku nastepuje powrot do menu glownego.
     * @param e
     */
    public void actionPerformed(ActionEvent e){
        int difficultyCode;
        if (e.getSource() == latwy) {
            difficultyCode = 2;
            }
        else if (e.getSource() == sredni) {
            difficultyCode = 3;
        } else {
            difficultyCode = 4;
        }
        dispose();

        try {
            PrintWriter save = new PrintWriter("difficulty.txt");
            save.println(difficultyCode);
            save.close();
        } catch (IOException error) {
            System.out.println("IOException");
        }

        MainMenu mainMenu = new MainMenu();
    }
}
