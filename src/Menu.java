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
        setLocation(100, 200);
        setLayout(new GridLayout(4, 1));

        /**
         *Tworzenie przycisków menu głównego
         */
        mRozpocznijGre = new JMenuItem("Rozpocznij Grę");
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

      //  Container parent = start.getParent();
        setVisible(true);
    }
/* JMenuBar menuBar;
    private JMenu menuGra;
    private JMenuItem mRozpocznijGre;
    private JLabel lNazwa;

    public Menu() {
        setTitle("Balony");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        menuGra = new JMenu("Gra");

        mRozpocznijGre = new JMenuItem("Rozpocznij Grę");
        mRozpocznijGre.addActionListener(this);
        menuGra.add(mRozpocznijGre);

        setJMenuBar(menuBar);
        menuBar.add(menuGra);

        lNazwa = new JLabel("Witaj w grze Balony!", SwingConstants.CENTER);
        lNazwa.setBounds(20,20,300,200);
        lNazwa.setFont(new Font("SanSerif",Font.BOLD,25));
        add(lNazwa);
        setVisible(true);

    }*/
    public void actionPerformed(ActionEvent e){
        //Object z = e.getSource();
        if(e.getSource()==start)
        {

            File plikKofiguracyjny = new File("plikTekstowy.txt");


            try {
                dispose();
                Plansza plansza = new Plansza(plikKofiguracyjny);
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        plansza.setVisible(true);
                    }
                });

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


    public static void main(String[] args)
    {
        Menu okienko = new Menu();
    }

}
