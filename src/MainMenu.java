import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Odpowiada za tworzenie menu glownego wyswietlanego od razu po uruchomieniu programu.
 * Z menu glownego mozna przejsc do wyboru trybu gry, opcji, najlepszych wynikow lub mozna zakonczyc dzialanie programu.
 */
public class MainMenu extends JFrame implements ActionListener{
    private JButton start;
    private JButton wyjdz;
    private JButton opcje;
    private JButton wyniki;
/**
 * Konstruktor, tworzy okno z menu glownym z czterema przyciskami pozwalajacymi na przejscie do menu wyboru trybu gry, opcji, wynikow lub do wyjscia z gry.
 */
    MainMenu() {
        super("Bubble Hit");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        start = new JButton("Start");
        opcje = new JButton("Opcje");
        wyniki = new JButton("Wyniki");
        wyjdz = new JButton("Wyjdz");
        start.addActionListener(this);
        opcje.addActionListener(this);
        wyniki.addActionListener(this);
        wyjdz.addActionListener(this);

        add(start);
        add(opcje);
        add(wyniki);
        add(wyjdz);

        setVisible(true);
    }

    /**
     * Wywolywana po wcisnieciu jednego z wyswietlanych przyciskow.
     * Wcisniecie przycisku "Start" powoduje przejscie do menu wyboru trybu gry.
     * Wcisniecie przycisku "Wyniki" powoduje wyswietlenie najlepszych wynikow.
     * Wcisniecie przycisku "Opcje" powoduje przejscie do okna wyboru poziomu trudnosci.
     * Wcisniecie przycisku "Wyjscie" powoduje zamkniecie okna oraz wylaczenie programu.
     * @param e
     */
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==start)
        {
            dispose();
            ModeMenu modeMenu = new ModeMenu();
        }
        if(e.getSource()==wyniki){
            try{
                dispose();

                HighScore highscore = new HighScore();
            }
            catch (IOException error)
            {
                System.out.println("ERROR: IOException");
            }
        }
        if(e.getSource()==opcje){
            dispose();
            Options options = new Options();
        }
        if(e.getSource()==wyjdz){
            System.exit(0);
        }
    }
}
