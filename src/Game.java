import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Błażej on 2017-06-17.
 */
public class Game {
    String nick;

    public void descendBallons(Vector<Balon> Balloons) {
        for (Balon b : Balloons) {
            b.setWsplY(b.getWsplY() + 2);
        }
    }

    public Vector<Balon> addBallons(){
        OdczytPlanszy load=new OdczytPlanszy();
        try{
            load.Wczytaj(new File("fabularnysredni.txt"));
        }
        catch(IOException error){
            System.out.println("ERROR: IOException");
        }
        return load.balony;
    }

    public boolean checkStatus(Vector<Balon> Balloons) {
        for (Balon b : Balloons) {
            if (b.getWsplY() == 13) {
                return false;
            }
        }
        return true;
    }

    public void ending(int score) {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Twój wynik: "+score+". Wpisz Twoje imie: ");
        JTextField txt = new JTextField(10);
        panel.add(lbl);
        panel.add(txt);
        int selectedOption = JOptionPane.showOptionDialog(null, panel,"Porażka", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options , options[0]);
        if(selectedOption == 0)
        {
            nick = txt.getText();
            if(nick.contains(" ")){
                String[] divided2 = nick.split("\\s+");
                nick = String.join("", divided2);
            }
            if(nick.contains("-")){
                nick = nick.replace('-', '_');
            }
            if(!nick.isEmpty())
            {
                try{
                    updateHighScore(nick, score);
                }
                catch (IOException error){
                    System.out.println("ERROR: IOException");
                }
            }
            MainMenu menu = new MainMenu();
        }
    }

    private void updateHighScore(String line, int scr) throws FileNotFoundException {
        int[] scores = new int[5];
        int i=0;
        String[] lines = new String[5];
        String[] names = new String[5];
        Scanner odczyt = new Scanner(new File("highscore.txt"));
        while (odczyt.hasNext()) {
            lines[i]=odczyt.nextLine();
            String[] divided = lines[i].split("-");
            String[] divided2 = lines[i].split("\\s+");
            names[i] = divided2[1];
            scores[i]=Integer.parseInt(divided[1]);
            i++;
        }
        for (int j=0; j<5; j++){
            if(scores[j]<scr){
                switch(j){
                    case 4:
                        names[4]=line;
                        scores[4]=scr;
                        break;
                    case 3:
                        names[4]=names[3];
                        scores[4]=scores[3];
                        names[3]=line;
                        scores[3]=scr;
                        break;
                    case 2:
                        names[4]=names[3];
                        scores[4]=scores[3];
                        names[3]=names[2];
                        scores[3]=scores[2];
                        names[2]=line;
                        scores[2]=scr;
                        break;
                    case 1:
                        names[4]=names[3];
                        scores[4]=scores[3];
                        names[3]=names[2];
                        scores[3]=scores[2];
                        names[2]=names[1];
                        scores[2]=scores[1];
                        names[1]=line;
                        scores[1]=scr;
                        break;
                    case 0:
                        names[4]=names[3];
                        scores[4]=scores[3];
                        names[3]=names[2];
                        scores[3]=scores[2];
                        names[2]=names[1];
                        scores[2]=scores[1];
                        names[1]=names[0];
                        scores[1]=scores[0];
                        names[0]=line;
                        scores[0]=scr;
                        break;
                }
                break;
            }
        }
        PrintWriter save = new PrintWriter("highscore.txt");
        for(int k=0; k<5; k++){
            save.println((k+1)+". "+names[k]+" -"+scores[k]);
        }
        save.close();
    }

}
