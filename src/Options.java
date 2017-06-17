import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Błażej on 2017-06-17.
 */
public class Options extends JFrame implements ActionListener {
    private JButton latwy;
    private JButton sredni;
    private JButton trudny;
    Options(){

        super("Wybór poziomu trudności");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,3));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                MainMenu okienko = new MainMenu();
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

    /*public void actionPerformed(ActionEvent e){
        if(e.getSource()==latwy)
        {

            dispose();
            try{
                PrintWriter save = new PrintWriter("difficulty.txt");
                save.println("2");
                save.close();
            } catch(IOException error)
            {
                System.out.println("IOException");
            }
            MainMenu mainMenu = new MainMenu();

        }
        if(e.getSource()==sredni){
            dispose();
            try{
                PrintWriter save = new PrintWriter("difficulty.txt");
                save.println("3");
                save.close();
            } catch(IOException error)
            {
                System.out.println("IOException");
            }
            MainMenu mainMenu = new MainMenu();
        }
        if(e.getSource()==trudny){
            dispose();
            try{
                PrintWriter save = new PrintWriter("difficulty.txt");
                save.println("4");
                save.close();
            } catch(IOException error)
            {
                System.out.println("IOException");
            }
            MainMenu mainMenu = new MainMenu();
        }


    }*/
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
