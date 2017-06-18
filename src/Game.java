import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;


class Game {
    private static int map_num =0;
    private int NUM_OF_FILES = 3;

    /**
     * Odpowiada za obnizanie sie balonow na mapie.
     * @param Balloons Kontener zawierajacy wszystkie wyswietlane ballons.
     * @param descend Mowi o ile ballons maja sie obnizyc.
     */
    void descendBallons(Vector<Balloon> Balloons, int descend) {
        for (Balloon b : Balloons) {
            b.setyCoordinate(b.getyCoordinate() + descend);
        }
    }

    /**
     * Dodaje na plansze ballons wczytywane z pliku.
     * Wywolywane w trybie arcade po obnizeniu sie balonow.
     * @return Kontener zawierajacy dodawane ballons
     */
    Vector<Balloon> addBallons(){
        MapLoad load=new MapLoad();
        try{
            load.loadFile(new File("fabularnysredni.txt"));
        }
        catch(IOException error){
            System.out.println("ERROR: IOException");
        }
        return load.ballons;
    }

    /**
     * Sprawdza czy jakis balon znajduje sie na wysokosci pocisku na mapie.
     * @param Balloons Kontener wszystkich wyswietlanych balonow.
     * @return true jesli ballons znajduja sie powyzej pocisku, false jesli na tej samej wysokosci.
     */
    boolean checkStatus(Vector<Balloon> Balloons) {
        for (Balloon b : Balloons) {
            if (b.getyCoordinate() == 13) {
                return false;
            }
        }
        return true;
    }

    /**
     * Odpowiada za zachowanie gry po uusnieciu z niej wszystkich balonow.
     * Po usunieciu wszystkich balonow tworzona jest nowa mapa z pliku tekstowego.
     * Jesli gracz wyczyscil ballons na wszystkich dostepnych mapach, wyswietlany jest komunikat informujacy o zwyciestwie.
     */
    void nextLevel(){

        File[] files = new File[NUM_OF_FILES];
        files[0] = new File("drugipoziom.txt");
        files[1] = new File("trzecipoziom.txt");
        files[2] = new File("czwartypoziom.txt");
        try {
            Map map = new Map(files[map_num]);
            map_num++;
            //    EventQueue.invokeLater(() -> plansza.setVisible(true));
            map.setVisible(true);
        }
        catch (IOException error)
        {
            System.out.println("ERROR: IOException");
        }
        catch (ArrayIndexOutOfBoundsException err){
            String[] options = {"Hurra"};
            JPanel panel = new JPanel();
            JLabel lbl = new JLabel("Ukończyłeś tryb fabularny ");
            panel.add(lbl);
            int selectedOption = JOptionPane.showOptionDialog(null, panel,"Koniec gry", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options , options[0]);
            if(selectedOption == 0){
                MainMenu menu = new MainMenu();
                map_num =0;
            }

        }
    }

    /**
     * Odpowiada za zachowanie gry po porazce.
     * Wyswietlany jest komunikat informujacy o porazce, w ktorym gracz wpisuje swoje imie.
     * Jesli imie zostalo wpisane, a wynik gracza jest lepszy od wynikow w pliku highscore.txt to jest on tam wpisywany.
     * @param score Wynik jaki uzyskal gracz podczas gry.
     */
    void ending(int score) {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Twój wynik: "+score+". Wpisz Twoje imie: ");
        JTextField txt = new JTextField(10);
        panel.add(lbl);
        panel.add(txt);
        int selectedOption = JOptionPane.showOptionDialog(null, panel,"Porażka", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options , options[0]);
        if(selectedOption == 0)
        {
            String nick = txt.getText();
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

    /**
     * Odpowiada za dopisanie imienia gracza wraz z wynikiem do pliku highscore.txt.
     * @param line Imie gracza.
     * @param scr Wynik gracza.
     * @throws FileNotFoundException Wyjatek jesli nie zostal odnaleziony plik highscore.txt.
     */
    private void updateHighScore(String line, int scr) throws FileNotFoundException {
        int[] scores = new int[5];
        int i=0;
        String[] lines = new String[5];
        String[] names = new String[5];
        Scanner in = new Scanner(new File("highscore.txt"));
        while (in.hasNext()) {
            lines[i]=in.nextLine();
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
