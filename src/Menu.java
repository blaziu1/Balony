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

    private JMenuBar menuBar;
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
    }
    @Override
    public void actionPerformed(ActionEvent e){
        Object z = e.getSource();
        if(z==mRozpocznijGre)
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

    }
    public static void main(String[] args)
    {
        Menu okienko = new Menu();
    }

}
