import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class MainMenu extends JFrame implements ActionListener{
    private JButton start;
    private JButton wyjdz;
    private JButton opcje;
    private JButton wyniki;

    MainMenu() {
        super("Bubble Hit");
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
        wyniki.addActionListener(this);

        add(start);
        add(opcje);
        add(wyniki);
        add(wyjdz);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==start)
        {
            dispose();
            ModeMenu modeMenu = new ModeMenu();
        }
        if(e.getSource()==wyniki){
            dispose();

            HighScore highscore = new HighScore();
        }
        if(e.getSource()==wyjdz){
            System.exit(0);
        }


    }
}
