import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Odpowiada za utworzenie okna z wyborem trybu gry.
 * Do wyboru sa dwa tryby - arcade i fabularny.
 */
public class ModeMenu  extends JFrame implements ActionListener {
    private JButton fabularny;
    private JButton arcade;
    private JButton powrot;

    /**
     * Konstruktor, tworzy okno z trzema przyciskami.
     * Przyciskami mozna przejsc do trybu arcade, trybu fabularnego lub wrocic do menu glownego.
     */
    ModeMenu() {
        super("Bubble Hit");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        fabularny = new JButton("Tryb fabularny");
        arcade = new JButton("Tryb arcade");
        powrot = new JButton("Powrót");
        fabularny.addActionListener(this);
        arcade.addActionListener(this);
        powrot.addActionListener(this);

        add(fabularny);
        add(arcade);
        add(powrot);

        setVisible(true);
    }

    /**
     * Wywolywana po wcisnieciu jednego z wyswietlanych przyciskow.
     * Po wybraniu jednego z trybow wyswietlana jest plansza wyswietlana z pliku tekstowego.
     * Wcisniecie przycisku "Powrot" powoduje zamkniecie okna i powrot do menu glownego.
     * @param e
     */

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==fabularny)
        {
            try {
                dispose();
                Map map = new Map(new File("fabularny.txt"));
                //    EventQueue.invokeLater(() -> plansza.setVisible(true));
                map.setVisible(true);

            }
            catch (IOException error)
            {
                System.out.println("ERROR: IOException");
            }
        }
        if(e.getSource()==arcade)
        {
            try {
                dispose();
                Map map = new Map(new File("arcade.txt"));
                //    EventQueue.invokeLater(() -> plansza.setVisible(true));
                map.setVisible(true);

            }
            catch (IOException error)
            {
                System.out.println("ERROR: IOException");
            }
        }
        if(e.getSource()==powrot)
        {
                dispose();
                MainMenu mainmenu = new MainMenu();
        }
    }



}
