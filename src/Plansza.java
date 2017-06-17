import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.io.File;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;


/**
 * Klasa okna planszy gry
 */
public class Plansza extends JFrame implements ActionListener {
    private Image img;
    private double proporcjaX;
    private double proporcjaY;
    private double droga;
    private JPanel window;
    private JPanel upcomponent;
    private JPanel contentPane;


    private JMenuItem wyjdz;
    private JMenuItem pauza;

    private double PRZESUNIECIE = 10;
    private double PRZESUNIECIEX;
    private double PRZESUNIECIEY;
    private int przesuniecieWPoziomie;
    private int przesuniecieWPionie;
    private String nick;
    private boolean stoper = false;
    private boolean active;
    private Thread th;
    private Timer tm;
    public OdczytPlanszy odczyt = new OdczytPlanszy();
    private Vector<Balon> pociski = new Vector<>();
    private Vector<Balon> displayedBalloons = new Vector<>();
    private int score = 9;
    private int licznik = 0;
    private int nextMap = 1;

    private Balon Bullet;
    private Balon SecBullet;

    /**
     * Konstruktor wczytuj�cy dane planszy gry z pliku konfiguracyjnego.
     *
     * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
     * @throws IOException je�eli nie b�dzie mo�na nawi�za� po��czenia
     */

    Plansza(File plikStartowy) throws IOException {
        odczyt.Wczytaj(plikStartowy);
        displayedBalloons = odczyt.balony;
        setSize(odczyt.SZEROKOSC * 60, odczyt.WYSOKOSC * 60);
        //    StworzPustaPlansze(odczyt.WYSOKOSC, odczyt.SZEROKOSC);
        setTitle("Bubble Hit");
        setLocationRelativeTo(null);
        window = new JPanel(new GridLayout(2, 1));
        upcomponent = new JPanel(new BorderLayout());
        Game game = new Game();
        //       addComponents();
        Bullet = new Balon(odczyt.getKolor(6), (odczyt.SZEROKOSC) * 30 - 30, (odczyt.WYSOKOSC - 2) * 60);
        SecBullet = new Balon(odczyt.getKolor(6), (odczyt.SZEROKOSC) * 60 - 60, (odczyt.WYSOKOSC - 1) * 60);
        pociski.add(SecBullet);
        pociski.add(Bullet);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        class TimeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stoper) {
                    modyfikacjaPolozenia();
                    game.checkStatus(displayedBalloons);
                    repaint();
                }
                if (!game.checkStatus(displayedBalloons)) {
                    game.ending(score);
                    tm.stop();
                    dispose();
                }
                if (licznik == 3) {
                    game.descendBallons(displayedBalloons, odczyt.DESCEND);
                    licznik = 0;
                    if (odczyt.DESCEND != 0) {
                        for (int i = 0; i < game.addBallons().size(); i++)
                            displayedBalloons.add(game.addBallons().get(i));
                    }
                }
                if (odczyt.DESCEND == 0 && displayedBalloons.isEmpty()) {
                    tm.stop();
                    game.nextLevel();
                    dispose();
                    nextMap++;
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

    private void MouseListenerPlansza() {

        addMouseListener(new MouseAdapter() {

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
                double s2 = (odczyt.SZEROKOSC) * 60; // szerokosc ustalona
                double wsps = s1 / s2; //wspolczynnik szerokosci
                // double s3 = (Bullet.getWsplX())*wsps; //potrzebna nam zmiana typu double
                double s3 = (pociski.lastElement().getWsplX()) * wsps; //potrzebna nam zmiana typu double
                int su3 = (int) s3; //potrzebna nam zmiana typu int
                double s4 = (gdzieKliknieto.getWsplX());
                int su4 = (int) s4;
                przesuniecieWPoziomie = su4 - su3/*((Bullet.getWsplX()+30)*(getWidth()/((SZEROKOSC-1)*60)))*/;
                double w1 = getHeight(); //pobierana wysokosc
                double w2 = (odczyt.WYSOKOSC * 60); // wysokosc ustalona
                double wspw = w1 / w2; //wspolczynnik wysokosci
                //  double w3 = (Bullet.getWsplY())*wspw; //potrzebna nam zmiana typu double
                double w3 = (pociski.lastElement().getWsplY()) * wspw; //potrzebna nam zmiana typu double
                int wu3 = (int) w3; //potrzebna nam zmiana typu int
                double w4 = (gdzieKliknieto.getWsplY());
                int wu4 = (int) w4;
                przesuniecieWPionie = wu4 - wu3/*(Bullet.getWsplY()+30)*//*((Bullet.getWsplY()+30))*//*(getHeight()/(WYSOKOSC*60)))*/;
                droga = Math.sqrt(Math.pow(przesuniecieWPionie, 2) + Math.pow(przesuniecieWPoziomie, 2)); //zmiana
                // System.out.println("droga przesuniecieX przesuniecieY " + droga + przesuniecieWPoziomie + przesuniecieWPionie);
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE * (1 / wsps) * proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE * (1 / wspw) * proporcjaY);
                active = true;
                stoper = false;
                if (gdzieKliknieto.getWsplY() > pociski.lastElement().getWsplY()) {
                    PRZESUNIECIEX = 0;
                    PRZESUNIECIEY = 0;
                }

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


        if (pociski.lastElement().getWsplY() >= 60 && pociski.lastElement().getWsplY() <= (odczyt.WYSOKOSC - 1) * 60)/*getHeight() - 60)*/ {
            if (przesuniecieWPionie < 0) {
                pociski.lastElement().setWsplY((int) (pociski.lastElement().getWsplY() - PRZESUNIECIEY));
                if (przesuniecieWPionie > 0) {
                    pociski.lastElement().setWsplY((int) (pociski.lastElement().getWsplY() + PRZESUNIECIEY));
                }

            }
            if (pociski.lastElement().getWsplY() <= 60) {
                pociski.lastElement().setWsplY(60);
                int n = 0;
                while (pociski.lastElement().getWsplX() > n * 60 + 30) {
                    n++;
                }
                int dx = n * 60;
                pociski.lastElement().setWsplX(dx);
                /*PRZESUNIECIEY = 0;
                PRZESUNIECIEX = 0;*/
                stoper = true;
                pociski.lastElement().setWsplX((pociski.lastElement().getWsplX() / 60));
                pociski.lastElement().setWsplY((pociski.lastElement().getWsplY() / 60));
                displayedBalloons.add(pociski.lastElement());
                pociski.clear();
                Bullet = new Balon(odczyt.getKolor(6), (odczyt.SZEROKOSC) * 30 - 30, (odczyt.WYSOKOSC - 2) * 60);
                pociski.add(Bullet);
                disappearing();
            }


            if (pociski.lastElement().getWsplY() >= (odczyt.WYSOKOSC - 1) * 60) {
                pociski.lastElement().setWsplY((odczyt.WYSOKOSC - 1) * 60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }
        }

        boolean czyMoznadalej = CzyDrogaWolna(displayedBalloons);
        if (!czyMoznadalej) {

            int n = 0;
            while (pociski.lastElement().getWsplX() > n * 60 + 30) {
                n++;
            }
            int dx = n * 60;
            pociski.lastElement().setWsplX(dx);
            int m = 0;
            while (pociski.lastElement().getWsplY() > m * 60 + 30) {
                m++;
            }
            int dy = m * 60;
            pociski.lastElement().setWsplY(dy);

            stoper = true;
            pociski.lastElement().setWsplX((pociski.lastElement().getWsplX() / 60));
            pociski.lastElement().setWsplY((pociski.lastElement().getWsplY() / 60));
            displayedBalloons.add(pociski.lastElement());
            Bullet = new Balon(odczyt.getKolor(6), (odczyt.SZEROKOSC) * 60 - 60, (odczyt.WYSOKOSC - 1) * 60);
            SecBullet = pociski.firstElement();
            SecBullet.setWsplX((odczyt.SZEROKOSC) * 30 - 30);
            SecBullet.setWsplY((odczyt.WYSOKOSC - 2) * 60);
            pociski.clear();
            pociski.add(Bullet);
            pociski.add(SecBullet);
            licznik++;
            disappearing();
        }


    }

    private boolean CzyDrogaWolna(Vector<Balon> Balloons) {
        for (Balon b : Balloons) {
            if (Math.sqrt(Math.pow(b.getWsplX() * 60 - (pociski.lastElement().getWsplX()), 2) + Math.pow(Math.abs(b.getWsplY() * 60 - (pociski.lastElement().getWsplY())), 2)) <= 50) {
                return false;
            }
        }
        return true;
    }

    private void disappearing(){
        int indextable[]=new int[150];
        for(int p =0;p<150;p++){
            indextable[p]=-1;
        }
        int licznik = 0;

        for(int i1 =0; i1<displayedBalloons.size();i1++){
            int polX1 =displayedBalloons.get(i1).getWsplX();
            int polY1 = displayedBalloons.get(i1).getWsplY();
            int polX2 = displayedBalloons.lastElement().getWsplX();
            int polY2 = displayedBalloons.lastElement().getWsplY();
            int odlX = polX1 - polX2;
            int odlY = polY1 - polY2;
            double odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
            if(odlXY == 1.0 && displayedBalloons.lastElement().kolor==displayedBalloons.get(i1).kolor ){
                //licznik++;
                indextable[i1]=i1;
                polX1 = displayedBalloons.get(i1).getWsplX();
                polY1 = displayedBalloons.get(i1).getWsplY();
                for (int i2=0;i2<displayedBalloons.size();i2++){
                    polX2 = displayedBalloons.get(i2).getWsplX();
                    polY2 = displayedBalloons.get(i2).getWsplY();
                    odlX = polX1 - polX2;
                    odlY = polY1 - polY2;
                    odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                    if(odlXY == 1.0 && displayedBalloons.get(i2).kolor==displayedBalloons.get(i1).kolor ){
                        //licznik++;
                        indextable[i2]=i2;
                        polX1 = displayedBalloons.get(i2).getWsplX();
                        polY1 = displayedBalloons.get(i2).getWsplY();
                        for (int i3 =0;i3<displayedBalloons.size();i3++){
                            polX2 = displayedBalloons.get(i3).getWsplX();
                            polY2 = displayedBalloons.get(i3).getWsplY();
                            odlX = polX1 - polX2;
                            odlY = polY1 - polY2;
                            odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                            if(odlXY == 1.0 && displayedBalloons.get(i3).kolor==displayedBalloons.get(i2).kolor ){
                                //licznik++;
                                indextable[i3]=i3;
                                polX1 = displayedBalloons.get(i3).getWsplX();
                                polY1 = displayedBalloons.get(i3).getWsplY();
                                for (int i4 =0;i4<displayedBalloons.size();i4++){
                                    polX2 = displayedBalloons.get(i4).getWsplX();
                                    polY2 = displayedBalloons.get(i4).getWsplY();
                                    odlX = polX1 - polX2;
                                    odlY = polY1 - polY2;
                                    odlXY = Math.sqrt(Math.pow(odlX,2)+Math.pow(odlY,2));
                                    if(odlXY == 1.0 && displayedBalloons.get(i4).kolor==displayedBalloons.get(i3).kolor ){
                                        //licznik++;
                                        indextable[i4]=i4;
                                        polX1 = displayedBalloons.get(i4).getWsplX();
                                        polY1 = displayedBalloons.get(i4).getWsplY();
                                        for (int i5 =0;i5<displayedBalloons.size();i5++) {
                                            polX2 = displayedBalloons.get(i5).getWsplX();
                                            polY2 = displayedBalloons.get(i5).getWsplY();
                                            odlX = polX1 - polX2;
                                            odlY = polY1 - polY2;
                                            odlXY = Math.sqrt(Math.pow(odlX, 2) + Math.pow(odlY, 2));
                                            if (odlXY == 1.0 && displayedBalloons.get(i5).kolor == displayedBalloons.get(i4).kolor) {
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
            if(odlXY == 0.0 && displayedBalloons.lastElement().kolor==displayedBalloons.get(i1).kolor ){
                indextable[i1]=i1;
            }
        }
        for(int h =displayedBalloons.size()-1;h>=0;h--){
            if(indextable[h]!=-1){
                licznik++;
            }
        }
        if(licznik>2){
            for(int h =displayedBalloons.size()-1;h>=0;h--){
                if(indextable[h]!=-1){
                    displayedBalloons.remove(indextable[h]);
                }
            }

        }
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
        for (int kr = 0; kr < odczyt.WYSOKOSC * 60; kr += 20) {
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
            if (g.getColor() != Color.WHITE) {
                g.drawImage(b.getObrazekBalonu(), b.getWsplX() * 60, b.getWsplY() * 60, null);
            }
        }
        for (Balon b : pociski) {
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