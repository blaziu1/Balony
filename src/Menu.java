import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Błażej on 2017-05-26.
 */
public class Menu extends JFrame implements ActionListener{
    JMenuBar menuBar;
    private JMenu menuGra;
    private JMenuItem mRozpocznijGre;
    private JLabel lNazwa;
    private JButton start;
    private JButton wyjdz;
    private JButton opcje;
    private JButton wyniki;

    public Menu() {
        super("Bubble hit");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        /**
         *Tworzenie przycisków menu głównego
         */
        start = new JButton("Start");
        opcje = new JButton("Opcje");
        wyniki = new JButton("Wyniki");
        wyjdz = new JButton("Wyjdz");
        start.addActionListener(this);
        wyjdz.addActionListener(this);

        add(start);
        add(opcje);
        add(wyniki);
        add(wyjdz);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==start)
        {

            File plikKofiguracyjny = new File("plikTekstowy.txt");


            try {
                dispose();
                Plansza plansza = new Plansza(plikKofiguracyjny);
                EventQueue.invokeLater(() -> plansza.setVisible(true));

            }
            catch (IOException error)
            {
                System.out.println("ERROR: IOException");
            }

        }
        if(e.getSource()==wyjdz){
            System.exit(0);
        }


    }
}
