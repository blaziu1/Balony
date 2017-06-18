import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.io.File;
import javax.swing.*;
import javax.swing.Timer;


/**
 * Klasa odpowiedzialna za wszystko co sie dzieje na mapie podczas gry.
 */
public class Map extends JFrame /*implements ActionListener*/ {
    private double proporcjaX;
    private double proporcjaY;
    private double droga;

    private double PRZESUNIECIE = 10;
    private double PRZESUNIECIEX;
    private double PRZESUNIECIEY;
    private int przesuniecieWPoziomie;
    private int przesuniecieWPionie;
    private boolean stop = false;
    private boolean active;
    private Thread th;
    private Timer tm;
    private MapLoad odczyt = new MapLoad();
    private Vector<Balloon> pociski = new Vector<>();
    private Vector<Balloon> displayedBalloons = new Vector<>();
    private int score = 0;
    private int counter = 0;
    private int release = 0;

    private Balloon bullet;
    private Balloon secondBullet;

    /**
     * Konstruktor, tworzy okno gry na podstawie pliku tekstowego wczytanego w klasie MapLoad.
     * Tworzy naboj klasy Balloon po srodku mapy na dole oraz drugi naboj w prawym dolnym rogu.
     * Pierwszym nabojem gracz strzela, drugi wejdzie wtedy na miejsce pierwszego.
     * Balony znajduja sie w jednym kontenerze, a naboje w drugim.
     * @param plikStartowy Wczytany plik tekstowy na podstawie ktorego wczytana jest mapa.
     * @throws IOException Gdy nie odnaleziony zostanie plik tekstowy.
     */

    Map(File plikStartowy) throws IOException {
        odczyt.loadFile(plikStartowy);
        displayedBalloons = odczyt.ballons;
        setSize(odczyt.WIDTH * 60, odczyt.HEIGHT * 60);
        setTitle("Bubble Hit");
        setLocationRelativeTo(null);
        Game game = new Game();
        bullet = new Balloon(odczyt.getColour(6), (odczyt.WIDTH) * 30 - 30, (odczyt.HEIGHT - 2) * 60);
        secondBullet = new Balloon(odczyt.getColour(6), (odczyt.WIDTH) * 60 - 60, (odczyt.HEIGHT - 1) * 60);
        pociski.add(secondBullet);
        pociski.add(bullet);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        /**
         * Odpowiada za animacje balonow na mapie.
         * Sprawdza czy nie nastapil koniec gry lub czy nie nalezy obnizyc coordinates balonow.
         */
        class TimeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stop) {
                    movement();
                    game.checkStatus(displayedBalloons);
                    repaint();
                }
                if (!game.checkStatus(displayedBalloons)) {
                    game.ending(score);
                    tm.stop();
                    dispose();
                }
                if (counter == 5) {
                    if (odczyt.MODE == 0) {
                        game.descendBallons(displayedBalloons, odczyt.DESCENDARC);
                        counter = 0;
                        for (int i = 0; i < game.addBallons().size(); i++)
                            displayedBalloons.add(game.addBallons().get(i));
                    }
                    else{
                        game.descendBallons(displayedBalloons, odczyt.DESCENDFAB);
                        counter = 0;
                    }
                }
                if (odczyt.MODE == 1 && displayedBalloons.isEmpty()) {
                    tm.stop();
                    game.nextLevel();
                    dispose();
                }
            }
        }
        ActionListener listener = new TimeListener();

        int czas = 5;
        tm = new Timer(czas, listener);
        tm.start();
        MouseListenerPlansza();


        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                MainMenu mainMenu = new MainMenu();
            }


        });


 /*       this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char h = e.getKeyChar();
                System.out.println(h);
                if (h == 'p') {
                    if(PRZESUNIECIEX!=0&&PRZESUNIECIEY!=0){
                        PRZESUNIECIEX=0;
                        PRZESUNIECIEY=0;
                    }
                    else{

                    }
                }
            }


        });*/
    }

    /**
     * Odpowiada za pobranie wspolrzednych punktu na mapie w ktore kliknal gracz.
     */
    private void MouseListenerPlansza() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(release==0){
                    Location gdzieKliknieto = new Location(e.getX(), e.getY());
                    double s1 = getWidth(); //pobierana szerokosc
                    double s2 = (odczyt.WIDTH) * 60; // szerokosc ustalona
                    double wsps = s1 / s2; //wspolczynnik szerokosci
                    double s3 = (pociski.lastElement().getxCoordinate()) * wsps; //potrzebna nam zmiana typu double
                    int su3 = (int) s3; //potrzebna nam zmiana typu int
                    double s4 = (gdzieKliknieto.getWsplX());
                    int su4 = (int) s4;
                    przesuniecieWPoziomie = su4 - su3/*((bullet.getxCoordinate()+30)*(getWidth()/((WIDTH-1)*60)))*/;
                    double w1 = getHeight(); //pobierana wysokosc
                    double w2 = (odczyt.HEIGHT * 60); // wysokosc ustalona
                    double wspw = w1 / w2; //wspolczynnik wysokosci
                    double w3 = (pociski.lastElement().getyCoordinate()) * wspw; //potrzebna nam zmiana typu double
                    int wu3 = (int) w3; //potrzebna nam zmiana typu int
                    double w4 = (gdzieKliknieto.getWsplY());
                    int wu4 = (int) w4;
                    przesuniecieWPionie = wu4 - wu3/*(bullet.getyCoordinate()+30)*//*((bullet.getyCoordinate()+30))*//*(getHeight()/(HEIGHT*60)))*/;
                    droga = Math.sqrt(Math.pow(przesuniecieWPionie, 2) + Math.pow(przesuniecieWPoziomie, 2)); //zmiana
                    proporcjaX = przesuniecieWPoziomie / droga;
                    proporcjaY = przesuniecieWPionie / droga;
                    PRZESUNIECIEX = Math.abs(PRZESUNIECIE * (1 / wsps) * proporcjaX);
                    PRZESUNIECIEY = Math.abs(PRZESUNIECIE * (1 / wspw) * proporcjaY);
                    release=1;
                    active = true;
                    stop = false;
                    if (gdzieKliknieto.getWsplY() > pociski.lastElement().getyCoordinate()) {
                        PRZESUNIECIEX = 0;
                        PRZESUNIECIEY = 0;
                    }
                }

             /*   Location gdzieKliknieto = new Location(e.getX(), e.getY());
                double s1 = getWidth(); //pobierana szerokosc
                double s2 = (odczyt.WIDTH) * 60; // szerokosc ustalona
                double wsps = s1 / s2; //wspolczynnik szerokosci
                double s3 = (pociski.lastElement().getxCoordinate()) * wsps; //potrzebna nam zmiana typu double
                int su3 = (int) s3; //potrzebna nam zmiana typu int
                double s4 = (gdzieKliknieto.getWsplX());
                int su4 = (int) s4;
                przesuniecieWPoziomie = su4 - su3//((bullet.getxCoordinate()+30)*(getWidth()/((WIDTH-1)*60)))
                double w1 = getHeight(); //pobierana wysokosc
                double w2 = (odczyt.HEIGHT * 60); // wysokosc ustalona
                double wspw = w1 / w2; //wspolczynnik wysokosci
                double w3 = (pociski.lastElement().getyCoordinate()) * wspw; //potrzebna nam zmiana typu double
                int wu3 = (int) w3; //potrzebna nam zmiana typu int
                double w4 = (gdzieKliknieto.getWsplY());
                int wu4 = (int) w4;
                przesuniecieWPionie = wu4 - wu3/*(bullet.getyCoordinate()+30)*//*((bullet.getyCoordinate()+30))*//*(getHeight()/(HEIGHT*60)))*/
              /*  droga = Math.sqrt(Math.pow(przesuniecieWPionie, 2) + Math.pow(przesuniecieWPoziomie, 2)); //zmiana
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE * (1 / wsps) * proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE * (1 / wspw) * proporcjaY);
                active = true;
                stop = false;
                if (gdzieKliknieto.getWsplY() > pociski.lastElement().getyCoordinate()) {
                    PRZESUNIECIEX = 0;
                    PRZESUNIECIEY = 0;
                }*/

            }

        });
    }
    /**
     *usypia watek na n ms
     */

 /*   private void Sleeeep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e1) {
            System.out.println("InterruptedException");
        }}*/

    /**
     * Odpowiada za zmiane coordinates pocisku na mapie od jego wystrzelenia do zatrzymania.
     * Jesli pocisk trafi w balon lub w gorna sciane, jest on zatrzymywany, wyrownywany, wyjmowany z kontenera pociskow i wkladany do kontenera balonow.
     * Drugi pocisk staje sie wtedy pierwszym, a w miejsce drugiego losowany jest nowy pocisk.
     * Jesli pocisk trafi w grupe dwoch lub wiecej balonow o tym samym kolorze znikaja one z mapy.
     */
    private void movement() {


        int a = (odczyt.WIDTH - 1) * 60;
        if (pociski.lastElement().getxCoordinate() >= 0 && pociski.lastElement().getxCoordinate() <= a) {
            if (przesuniecieWPoziomie < 0) {
                pociski.lastElement().setxCoordinate((int) (pociski.lastElement().getxCoordinate() - PRZESUNIECIEX));
            }
            if (przesuniecieWPoziomie > 0) {
                pociski.lastElement().setxCoordinate((int) (pociski.lastElement().getxCoordinate() + PRZESUNIECIEX));
            }


        }
        if (pociski.lastElement().getxCoordinate() <= 0) {
            pociski.lastElement().setxCoordinate(0);
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getxCoordinate() >= a) {
            pociski.lastElement().setxCoordinate(a);
            //System.out.println(getWidth());
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getyCoordinate() >= 60 && pociski.lastElement().getyCoordinate() <= (odczyt.HEIGHT - 1) * 60)/*getHeight() - 60)*/ {
            if (przesuniecieWPionie < 0) {
                pociski.lastElement().setyCoordinate((int) (pociski.lastElement().getyCoordinate() - PRZESUNIECIEY));
                if (przesuniecieWPionie > 0) {
                    pociski.lastElement().setyCoordinate((int) (pociski.lastElement().getyCoordinate() + PRZESUNIECIEY));
                }

            }
            if (pociski.lastElement().getyCoordinate() <= 60) {
                pociski.lastElement().setyCoordinate(60);
                int n = 0;
                while (pociski.lastElement().getxCoordinate() > n * 60 + 30) {
                    n++;
                }
                int dx = n * 60;
                pociski.lastElement().setxCoordinate(dx);
                stop = true;
                pociski.lastElement().setxCoordinate((pociski.lastElement().getxCoordinate() / 60));
                pociski.lastElement().setyCoordinate((pociski.lastElement().getyCoordinate() / 60));
                displayedBalloons.add(pociski.lastElement());
                int variableq = 0;
                int variablez = 0;
                int variablen = 0;
                int variablec = 0;
                int variablezo = 0;
                for(int k = 0;k<displayedBalloons.size();k++){
                    if(displayedBalloons.get(k).colour==Colour.ZIELONY){
                        variablez = 10;
                    }
                    if(displayedBalloons.get(k).colour==Colour.CZERWONY){

                        variablen = 100;
                    }
                    if(displayedBalloons.get(k).colour==Colour.NIEBIESKI){

                        variablec = 1000;
                    }
                    if(displayedBalloons.get(k).colour==Colour.ZOLTY){

                        variablezo = 10000;
                    }

                }
                variableq = variablez +variablen +variablec + variablezo;
                bullet = new Balloon(odczyt.getColour(variableq), (odczyt.WIDTH) * 60 - 60, (odczyt.HEIGHT - 1) * 60);                secondBullet = pociski.firstElement();
                secondBullet.setxCoordinate((odczyt.WIDTH) * 30 - 30);
                secondBullet.setyCoordinate((odczyt.HEIGHT - 2) * 60);
                pociski.clear();
                pociski.add(bullet);
                pociski.add(secondBullet);
                counter++;
                disappearing();
                release=0;
                System.out.println(score);
            }


            if (pociski.lastElement().getyCoordinate() >= (odczyt.HEIGHT - 1) * 60) {
                pociski.lastElement().setyCoordinate((odczyt.HEIGHT - 1) * 60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }
        }

        if (!isClear(displayedBalloons)) {

            int n = 0;
            while (pociski.lastElement().getxCoordinate() > n * 60 + 30) {
                n++;
            }
            int dx = n * 60;
            pociski.lastElement().setxCoordinate(dx);
            int m = 0;
            while (pociski.lastElement().getyCoordinate() > m * 60 + 30) {
                m++;
            }
            int dy = m * 60;
            pociski.lastElement().setyCoordinate(dy);

            stop = true;
            pociski.lastElement().setxCoordinate((pociski.lastElement().getxCoordinate() / 60));
            pociski.lastElement().setyCoordinate((pociski.lastElement().getyCoordinate() / 60));
            displayedBalloons.add(pociski.lastElement());
            int variableq = 0;
            int variablez = 0;
            int variablen = 0;
            int variablec = 0;
            int variablezo = 0;
            for(int k = 0;k<displayedBalloons.size();k++){
                if(displayedBalloons.get(k).colour==Colour.ZIELONY){
                    variablez = 10;
                }
                if(displayedBalloons.get(k).colour==Colour.CZERWONY){

                    variablen = 100;
                }
                if(displayedBalloons.get(k).colour==Colour.NIEBIESKI){

                    variablec = 1000;
                }
                if(displayedBalloons.get(k).colour==Colour.ZOLTY){

                    variablezo = 10000;
                }

            }
            variableq = variablez +variablen +variablec + variablezo;
            bullet = new Balloon(odczyt.getColour(variableq), (odczyt.WIDTH) * 60 - 60, (odczyt.HEIGHT - 1) * 60);            secondBullet = pociski.firstElement();
            secondBullet.setxCoordinate((odczyt.WIDTH) * 30 - 30);
            secondBullet.setyCoordinate((odczyt.HEIGHT - 2) * 60);
            pociski.clear();
            pociski.add(bullet);
            pociski.add(secondBullet);
            counter++;
            disappearing();
            release=0;
            System.out.println(score);
        }


    }

    /**
     * Sprawdza czy naboj trafil w jakiegos balona umieszczonego na mapie.
     * @param Balloons Kontener balonow wyswietlanych na mapie.
     * @return false jesli naboj napotkal na swojej drodze, true w innym wypadku.
     */
    private boolean isClear(Vector<Balloon> Balloons) {
        for (Balloon b : Balloons) {
            if (Math.sqrt(Math.pow(b.getxCoordinate() * 60 - (pociski.lastElement().getxCoordinate()), 2) + Math.pow(Math.abs(b.getyCoordinate() * 60 - (pociski.lastElement().getyCoordinate())), 2)) <= 50) {
                return false;
            }
        }
        return true;
    }

    /**
     * Odpowiada za znikanie balonow jesli pocisk trafi w grupe dwoch lub wiecej balonow o takim samym kolorze co on.
     */
    private void disappearing(){
        int indextable[]=new int[150];
        for(int p =0;p<150;p++){
            indextable[p]=-1;
        }
        int licznik = 0;

        for(int i1 =0; i1<displayedBalloons.size();i1++){
            int polX1 =displayedBalloons.get(i1).getxCoordinate();
            int polY1 = displayedBalloons.get(i1).getyCoordinate();
            int polX2 = displayedBalloons.lastElement().getxCoordinate();
            int polY2 = displayedBalloons.lastElement().getyCoordinate();
            int odlX = polX1 - polX2;
            int odlY = polY1 - polY2;
            double odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
            if(odlXY == 1.0 && displayedBalloons.lastElement().colour ==displayedBalloons.get(i1).colour){
                //counter++;
                indextable[i1]=i1;
                polX1 = displayedBalloons.get(i1).getxCoordinate();
                polY1 = displayedBalloons.get(i1).getyCoordinate();
                for (int i2=0;i2<displayedBalloons.size();i2++){
                    polX2 = displayedBalloons.get(i2).getxCoordinate();
                    polY2 = displayedBalloons.get(i2).getyCoordinate();
                    odlX = polX1 - polX2;
                    odlY = polY1 - polY2;
                    odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                    if(odlXY == 1.0 && displayedBalloons.get(i2).colour ==displayedBalloons.get(i1).colour){
                        //counter++;
                        indextable[i2]=i2;
                        polX1 = displayedBalloons.get(i2).getxCoordinate();
                        polY1 = displayedBalloons.get(i2).getyCoordinate();
                        for (int i3 =0;i3<displayedBalloons.size();i3++){
                            polX2 = displayedBalloons.get(i3).getxCoordinate();
                            polY2 = displayedBalloons.get(i3).getyCoordinate();
                            odlX = polX1 - polX2;
                            odlY = polY1 - polY2;
                            odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                            if(odlXY == 1.0 && displayedBalloons.get(i3).colour ==displayedBalloons.get(i2).colour){
                                //counter++;
                                indextable[i3]=i3;
                                polX1 = displayedBalloons.get(i3).getxCoordinate();
                                polY1 = displayedBalloons.get(i3).getyCoordinate();
                                for (int i4 =0;i4<displayedBalloons.size();i4++){
                                    polX2 = displayedBalloons.get(i4).getxCoordinate();
                                    polY2 = displayedBalloons.get(i4).getyCoordinate();
                                    odlX = polX1 - polX2;
                                    odlY = polY1 - polY2;
                                    odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                                    if(odlXY == 1.0 && displayedBalloons.get(i4).colour ==displayedBalloons.get(i3).colour){
                                        //counter++;
                                        indextable[i4]=i4;
                                        polX1 = displayedBalloons.get(i4).getxCoordinate();
                                        polY1 = displayedBalloons.get(i4).getyCoordinate();
                                        for (int i5 =0;i5<displayedBalloons.size();i5++) {
                                            polX2 = displayedBalloons.get(i5).getxCoordinate();
                                            polY2 = displayedBalloons.get(i5).getyCoordinate();
                                            odlX = polX1 - polX2;
                                            odlY = polY1 - polY2;
                                            odlXY = Math.sqrt(Math.pow(odlX, 2) + Math.pow(odlY, 2));
                                            if (odlXY == 1.0 && displayedBalloons.get(i5).colour == displayedBalloons.get(i4).colour) {
                                                indextable[i5]=i5;

                                            }
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
            if(odlXY == 0.0 && displayedBalloons.lastElement().colour ==displayedBalloons.get(i1).colour){
                indextable[i1]=i1;
            }
        }
        for(int h =displayedBalloons.size()-1;h>=0;h--){
            if(indextable[h]!=-1){
                licznik++;
            }
        }
        int licznikpktow = 0;
        if(licznik>2){
            for(int h =displayedBalloons.size()-1;h>=0;h--){
                if(indextable[h]!=-1){
                    licznikpktow++;
                }
            }

        }
        if(licznik>2){
            for(int h =displayedBalloons.size()-1;h>=0;h--){
                if(indextable[h]!=-1){
                    displayedBalloons.remove(indextable[h]);
                }
            }

        }
        score+=licznikpktow;
    }


    /**
     * Rysuje na mapie wszystkie ballons oraz naboje znajdujace sie w kontenerach, w odpowiednich miejscach na mapie.
     * @param g
     */
    private void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, odczyt.WIDTH * 60, odczyt.HEIGHT * 60);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, odczyt.WIDTH * 60, 60);
        g.fillRect(0, odczyt.HEIGHT * 60 - 60, odczyt.WIDTH * 60, 60);
        g.setColor(Color.BLACK);
        for (int kr = 0; kr < odczyt.HEIGHT * 60; kr += 20) {
            g.fillRect(kr, (odczyt.HEIGHT - 2) * 60 - 2, 10, 1);
        }
        Image img;
        for (Balloon db : displayedBalloons) {
            switch (db.getColour()) {
                case ZOLTY:
                    g.setColor(Color.YELLOW);
                    db.setBallonImage(img = new ImageIcon("zolty.png").getImage());
                    break;
                case CZERWONY:
                    g.setColor(Color.RED);
                    db.setBallonImage(img = new ImageIcon("czerwony.png").getImage());
                    break;
                case ZIELONY:
                    g.setColor(Color.GREEN);
                    db.setBallonImage(img = new ImageIcon("zielony.png").getImage());
                    break;
                case NIEBIESKI:
                    g.setColor(Color.BLUE);
                    db.setBallonImage(img = new ImageIcon("niebieski.png").getImage());
                    break;
                default:
                    g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE) {
                g.drawImage(db.getBallonImage(), db.getxCoordinate() * 60, db.getyCoordinate() * 60, null);
            }
        }
        for (Balloon p : pociski) {
            switch (p.getColour()) {
                case ZOLTY:
                    g.setColor(Color.YELLOW);
                    p.setBallonImage(img = new ImageIcon("zolty.png").getImage());
                    break;
                case CZERWONY:
                    g.setColor(Color.RED);
                    p.setBallonImage(img = new ImageIcon("czerwony.png").getImage());
                    break;
                case ZIELONY:
                    g.setColor(Color.GREEN);
                    p.setBallonImage(img = new ImageIcon("zielony.png").getImage());
                    break;
                case NIEBIESKI:
                    g.setColor(Color.BLUE);
                    p.setBallonImage(img = new ImageIcon("niebieski.png").getImage());
                    break;
                default:
                    g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE) {
                g.drawImage(p.getBallonImage(), p.getxCoordinate(), p.getyCoordinate(), null);
            }
        }

        g.dispose();
        setFocusable(true);

    }

    /**
     * Odpowiada za podwojne buforowanie oraz skalowanie komponentow graficznych.
     * @param g
     */
    public void paint(Graphics g) {
        BufferedImage dbImage = new BufferedImage(odczyt.WIDTH * 60, odczyt.HEIGHT * 60, BufferedImage.TYPE_INT_ARGB);
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);

        BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = scaled.createGraphics();
        gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(scaled, 0, 0, this);
    }

    /**
     * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
     *
     * @param e przycisniecie przycisku
     */
   // @Override
   // public void actionPerformed(ActionEvent e) {

      /*  if (e.getSource() == this.wyjdz) {
            int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_OPTION);
            if (odp == JOptionPane.YES_OPTION) {
                dispose();
                Menu okienko = new Menu();
            } else if (odp == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Dobra decyzja!", "Brawo!", JOptionPane.INFORMATION_MESSAGE);
            } else if (odp == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!", "Nie�adnie!", JOptionPane.WARNING_MESSAGE);
            }
        }*/
   // }
}