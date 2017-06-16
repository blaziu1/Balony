import static java.lang.System.in;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.io.File;
import javax.swing.*;
import javax.swing.Timer;


/**
 * Klasa okna planszy gry
 */
public class Plansza extends JFrame implements ActionListener {
    private Image img;
    private double proporcjaX;
    private double proporcjaY;
    private double droga;

    private JMenuItem wyjdz;
    private JMenuItem pauza;

    private double PRZESUNIECIE=10;
    private  double PRZESUNIECIEX;
    private  double PRZESUNIECIEY;
    private int przesuniecieWPoziomie;
    private int przesuniecieWPionie;
    private String nick;
    private Random generator = new Random();
    private boolean stoper=false;
    private boolean active;
    private Thread th;
    private Timer tm;
    private OdczytPlanszy odczyt=new OdczytPlanszy();
    private Vector<Balon> pociski = new Vector<>();
    private Vector<Balon> displayedBalloons = new Vector<>();
   // int czas=10;
   private int score=0;
    private int licznik=0;


    private Balon Bullet;
    private Balon SecBullet;

    //   private Balon NowyNaboj;
    //   private Balon zmianaNaboju;
//    BufferedImage dbImage = new BufferedImage(11 * 60, 15 * 60, BufferedImage.TYPE_INT_ARGB);
//    Graphics dbg = dbImage.getGraphics();

    /**
     * Konstruktor wczytuj�cy dane planszy gry z pliku konfiguracyjnego.
     *
     * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
     * @throws IOException je�eli nie b�dzie mo�na nawi�za� po��czenia
     */

    Plansza(File plikStartowy) throws IOException {
        odczyt.Wczytaj(plikStartowy);
        displayedBalloons =odczyt.balony;
        setSize(odczyt.SZEROKOSC * 60, odczyt.WYSOKOSC * 60);
    //    StworzPustaPlansze(odczyt.WYSOKOSC, odczyt.SZEROKOSC);
        setTitle("Bubble Hit");
        setLocationRelativeTo(null);
        Bullet = new Balon(getKolor(99), (odczyt.SZEROKOSC)*30 - 30, (odczyt.WYSOKOSC-2)*60);
        SecBullet = new Balon(getKolor(99), (odczyt.SZEROKOSC)*60 - 60, (odczyt.WYSOKOSC-1)*60);
        pociski.add(SecBullet);
        pociski.add(Bullet);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        class TimeListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                if(!stoper){
                    modyfikacjaPolozenia();
                    checkStatus();
                    repaint();
                }
                if(!checkStatus()){
                    ending();
                }
                if(licznik==3)
                {
                    descendBallons();
                    System.out.println(addBallons().size());
                    for(int i=0;i<addBallons().size();i++)
                    displayedBalloons.add(addBallons().get(i));
                }
              //  if(!checkStatus()){
              //      System.out.println("1");
              //  }
            }
        }
        ActionListener listener = new TimeListener();

        int czas = 5;
        tm = new Timer(czas, listener);
        tm.start();
        MouseListenerPlansza();

        // NowyNaboj = new Balon(getWidth() - 80, getHeight() - 70 );
        //    int czas = 5;
        //   Timer tm = new Timer(czas, this);
        //   tm.start();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                MainMenu okienko = new MainMenu();
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

    private void MouseListenerPlansza() {
        this.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * słuchacz myszki
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                System.out.println(e.getPoint());

                /*Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
                przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - ((pociski.lastElement().getWsplX()+30)*(getWidth()/600));
                przesuniecieWPionie = gdzieKliknieto.getWsplY() - pociski.lastElement().getWsplY()-30;
                droga = Math.sqrt(Math.pow(przesuniecieWPionie,2) + Math.pow(przesuniecieWPoziomie,2)); //zmiana
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE * proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE * proporcjaY);
                active = true;
                stoper = false;*/
                Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
                //Polozenie polozenieWyrzutni = new Polozenie(Bullet.getWsplX(), Bullet.getWsplY());
                double s1 = getWidth(); //pobierana szerokosc
                double s2 = (odczyt.SZEROKOSC)*60; // szerokosc ustalona
                double wsps = s1/s2; //wspolczynnik szerokosci
               // double s3 = (Bullet.getWsplX())*wsps; //potrzebna nam zmiana typu double
                 double s3 = (pociski.lastElement().getWsplX())*wsps; //potrzebna nam zmiana typu double
                int su3 = (int)s3; //potrzebna nam zmiana typu int
                double s4 = (gdzieKliknieto.getWsplX());
                int su4 = (int) s4;
                przesuniecieWPoziomie = su4 - su3/*((Bullet.getWsplX()+30)*(getWidth()/((SZEROKOSC-1)*60)))*/;
                double w1 = getHeight(); //pobierana wysokosc
                double w2 = (odczyt.WYSOKOSC*60); // wysokosc ustalona
                double wspw = w1/w2; //wspolczynnik wysokosci
              //  double w3 = (Bullet.getWsplY())*wspw; //potrzebna nam zmiana typu double
                double w3 = (pociski.lastElement().getWsplY())*wspw; //potrzebna nam zmiana typu double
                int wu3 = (int)w3; //potrzebna nam zmiana typu int
                double w4 = (gdzieKliknieto.getWsplY());
                int wu4 = (int) w4;
                przesuniecieWPionie = wu4 - wu3/*(Bullet.getWsplY()+30)*//*((Bullet.getWsplY()+30))*//*(getHeight()/(WYSOKOSC*60)))*/;
                droga = Math.sqrt(Math.pow(przesuniecieWPionie,2) + Math.pow(przesuniecieWPoziomie,2)); //zmiana
                // System.out.println("droga przesuniecieX przesuniecieY " + droga + przesuniecieWPoziomie + przesuniecieWPionie);
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE* (1/wsps)* proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE* (1/wspw)* proporcjaY);
                active = true;
                stoper = false;
                if(gdzieKliknieto.getWsplY()>pociski.lastElement().getWsplY()){
                    PRZESUNIECIEX=0;
                    PRZESUNIECIEY=0;
                }

            }

        });
    }
    /**
     *usypia watek na n ms
     */

    private void Sleeeep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e1) {
            System.out.println("InterruptedException");
        }}
    /**
     * modyfikuje połozenie balonu-pocisku
     */

    private void modyfikacjaPolozenia() {


        int a = (odczyt.SZEROKOSC - 1) * 60;
        if (pociski.lastElement().getWsplX() >= 0 && pociski.lastElement().getWsplX() <= a) {
            if (przesuniecieWPoziomie < 0) {
                pociski.lastElement().setWsplX((int) (pociski.lastElement().getWsplX() - PRZESUNIECIEX));
            }
            if (przesuniecieWPoziomie > 0) {
                pociski.lastElement().setWsplX((int) (pociski.lastElement().getWsplX() + PRZESUNIECIEX));
            }


        }
        if (pociski.lastElement().getWsplX() <= 0) {
            pociski.lastElement().setWsplX(0);
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getWsplX() >= a) {
            pociski.lastElement().setWsplX(a);
            //System.out.println(getWidth());
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getWsplY() >= 60 && pociski.lastElement().getWsplY() <= (odczyt.WYSOKOSC-1)*60)/*getHeight() - 60)*/ {
            if (przesuniecieWPionie < 0) {
                pociski.lastElement().setWsplY((int) (pociski.lastElement().getWsplY() - PRZESUNIECIEY));
                if (przesuniecieWPionie > 0) {
                    pociski.lastElement().setWsplY((int) (pociski.lastElement().getWsplY() + PRZESUNIECIEY));
                }

            }
            if (pociski.lastElement().getWsplY() <= 60) {
                pociski.lastElement().setWsplY(60);
                int n=0;
                while(pociski.lastElement().getWsplX()>n*60+30) {
                    n++;
                }
                int dx = n*60;
                pociski.lastElement().setWsplX(dx);
                /*PRZESUNIECIEY = 0;
                PRZESUNIECIEX = 0;*/
                stoper=true;
                pociski.lastElement().setWsplX((pociski.lastElement().getWsplX()/60));
                pociski.lastElement().setWsplY((pociski.lastElement().getWsplY()/60));
                displayedBalloons.add(pociski.lastElement());
                pociski.clear();
                Bullet =new Balon(getKolor(99), (odczyt.SZEROKOSC)*30 - 30, (odczyt.WYSOKOSC-2)*60);
                pociski.add(Bullet);

            }


            if (pociski.lastElement().getWsplY() >= (odczyt.WYSOKOSC-1) * 60) {
                pociski.lastElement().setWsplY((odczyt.WYSOKOSC-1) * 60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }
        }

        boolean czyMoznadalej = CzyDrogaWolna(displayedBalloons);
        if (!czyMoznadalej) {

            int n=0;
            while(pociski.lastElement().getWsplX()>n*60+30) {
                n++;
            }
            int dx = n*60;
            pociski.lastElement().setWsplX(dx);
            int m=0;
            while(pociski.lastElement().getWsplY()>m*60+30) {
                m++;
            }
            int dy = m*60;
            pociski.lastElement().setWsplY(dy);
            // PRZESUNIECIEX=0;
            //   PRZESUNIECIEY=0;

            stoper=true;
            pociski.lastElement().setWsplX((pociski.lastElement().getWsplX()/60));
            pociski.lastElement().setWsplY((pociski.lastElement().getWsplY()/60));
            displayedBalloons.add(pociski.lastElement());
            Bullet =new Balon(getKolor(99), (odczyt.SZEROKOSC)*60 - 60, (odczyt.WYSOKOSC-1)*60);
             SecBullet = pociski.firstElement();
             SecBullet.setWsplX((odczyt.SZEROKOSC)*30 - 30);
             SecBullet.setWsplY((odczyt.WYSOKOSC-2)*60);
             pociski.clear();
             pociski.add(Bullet);
             pociski.add(SecBullet);
             licznik++;
        }


    }

    private boolean CzyDrogaWolna(Vector<Balon> Balloons){
        for (Balon b: Balloons){
            if (Math.sqrt(Math.pow(b.getWsplX() * 60 - (pociski.lastElement().getWsplX()),2)+Math.pow(Math.abs(b.getWsplY() * 60  - (pociski.lastElement().getWsplY())),2))<=50) {
                return false;
            }
        }
        return true;
    }

    private void descendBallons(){
        for (Balon db : displayedBalloons) {
            db.setWsplY(db.getWsplY() + 2);
        }
        licznik=0;
    }

    private Vector<Balon> addBallons(){
        OdczytPlanszy load=new OdczytPlanszy();
        try{
            load.Wczytaj(new File("fabularnysredni.txt"));
        }
        catch(IOException error){
            System.out.println("ERROR: IOException");
        }
        return load.balony;
    }

    private boolean checkStatus() {
        for (Balon b : displayedBalloons) {
            if (b.getWsplY() == 13) {
                return false;
            }
        }
        return true;
    }



    /**
     * maluje komponent planszy gry
     *
     * @param g kontekst graficzny
     */



    private void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, odczyt.SZEROKOSC * 60, odczyt.WYSOKOSC * 60);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, odczyt.SZEROKOSC * 60, 60);
        g.fillRect(0, odczyt.WYSOKOSC * 60 - 60, odczyt.SZEROKOSC * 60, 60);
        g.setColor(Color.BLACK);
        for(int kr = 0; kr<odczyt.WYSOKOSC*60; kr+=20) {
            g.fillRect(kr, (odczyt.WYSOKOSC - 2) * 60 - 2, 10, 1);
        }
            for (Balon b : displayedBalloons) {
            switch (b.getKolor()) {
                case ZOLTY:
                    g.setColor(Color.YELLOW);
                    b.setObrazekBalonu(img = new ImageIcon("zolty.png").getImage());
                    break;
                case CZERWONY:
                    g.setColor(Color.RED);
                    b.setObrazekBalonu(img = new ImageIcon("czerwony.png").getImage());
                    break;
                case ZIELONY:
                    g.setColor(Color.GREEN);
                    b.setObrazekBalonu(img = new ImageIcon("zielony.png").getImage());
                    break;
                case NIEBIESKI:
                    g.setColor(Color.BLUE);
                    b.setObrazekBalonu(img = new ImageIcon("niebieski.png").getImage());
                    break;
                default:
                    g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE)
            {
                //g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
                g.drawImage(b.getObrazekBalonu(), b.getWsplX() * 60, b.getWsplY() * 60, null);
            }
        }
        for(Balon b : pociski){
            switch (b.getKolor()) {
                case ZOLTY:
                    g.setColor(Color.YELLOW);
                    b.setObrazekBalonu(img = new ImageIcon("zolty.png").getImage());
                    break;
                case CZERWONY:
                    g.setColor(Color.RED);
                    b.setObrazekBalonu(img = new ImageIcon("czerwony.png").getImage());
                    break;
                case ZIELONY:
                    g.setColor(Color.GREEN);
                    b.setObrazekBalonu(img = new ImageIcon("zielony.png").getImage());
                    break;
                case NIEBIESKI:
                    g.setColor(Color.BLUE);
                    b.setObrazekBalonu(img = new ImageIcon("niebieski.png").getImage());
                    break;
                default:
                    g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE) {
                g.drawImage(b.getObrazekBalonu(), b.getWsplX(), b.getWsplY(), null);
            }
        }

        g.dispose();
        setFocusable(true);

    }

    public void paint(Graphics g) {
        BufferedImage dbImage = new BufferedImage(odczyt.SZEROKOSC * 60, odczyt.WYSOKOSC * 60, BufferedImage.TYPE_INT_ARGB);
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);

        BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = scaled.createGraphics();
        gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(scaled, 0, 0, this);
    }


    /**
     * Metoda tworzy pusta plansze o podanych wymiarach.
     *
     * @param WYSOKOSC  planszy w ilosci rzed�w balon�w
     * @param SZEROKOSC planszy w ilosci rzed�w balon�w
     */

 /*   private void StworzPustaPlansze(int WYSOKOSC, int SZEROKOSC) {
        for (int i = 0; i < WYSOKOSC; i++) {
            for (int j = 0; j < SZEROKOSC; j++) {

                Polozenie wspolrzedne = new Polozenie(j, i);
                odczyt.polozenia.add(wspolrzedne);
                odczyt.pola.put(wspolrzedne, new Balon());
            }
        }
    }*/

    /**
     * Metoda zwraca kolor na podstawie dostarczonego kodu numerycznego.
     *
     * @param kolorInt kod koloru
     * @return zwracany obiekt Kolor
     */

    private Kolor getKolor(int kolorInt) {
        Kolor kolor;
        if (kolorInt == 99) {
            Random rand = new Random();
            kolorInt = rand.nextInt(4) + 1;

        }
        switch (kolorInt) {
            case 1:
                kolor = Kolor.ZIELONY;
                break;
            case 2:
                kolor = Kolor.CZERWONY;
                break;
            case 3:
                kolor = Kolor.NIEBIESKI;
                break;
            case 4:
                kolor = Kolor.ZOLTY;
                break;
            default:
                kolor = Kolor.brak;
        }
        return kolor;
    }

    private void updateHighScore(String line, int scr) throws FileNotFoundException{
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

    private void ending() {
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
            try{
                updateHighScore(nick, score);
            }
            catch (IOException error){
                System.out.println("ERROR: IOException");
            }
            tm.stop();
            dispose();
            MainMenu menu = new MainMenu();
        }
    }

    /**
     * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
     *
     * @param e przycisniecie przycisku
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.wyjdz) {
            int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_OPTION);
            if (odp == JOptionPane.YES_OPTION) {
                dispose();
                Menu okienko = new Menu();
            } else if (odp == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Dobra decyzja!", "Brawo!", JOptionPane.INFORMATION_MESSAGE);
            } else if (odp == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!", "Nie�adnie!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    /**
     * Glowna petla animacji balonow
     *
     *
     *
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    //
/*@Override
public void run() {

while (true) {
if(stoper){
break;
}
//if(active){
modyfikacjaPolozenia();
repaint();
Sleeeep(25);
}
}
//}*/
}