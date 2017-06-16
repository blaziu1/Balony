import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Błażej on 2017-06-16.
 */
public class ModeMenu  extends JFrame implements ActionListener {
    private JButton fabularny;
    private JButton arcade;
    private JButton powrot;
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
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==fabularny)
        {

            File plikKofiguracyjny = new File("fabularny.txt");


            try {
                dispose();
                Plansza plansza = new Plansza(plikKofiguracyjny);
                //    EventQueue.invokeLater(() -> plansza.setVisible(true));
                plansza.setVisible(true);

            }
            catch (IOException error)
            {
                System.out.println("ERROR: IOException");
            }
        }
        if(e.getSource()==arcade)
        {

            File plikKofiguracyjny = new File("arcade.txt");


            try {
                dispose();
                Plansza plansza = new Plansza(plikKofiguracyjny);
                //    EventQueue.invokeLater(() -> plansza.setVisible(true));
                plansza.setVisible(true);

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
