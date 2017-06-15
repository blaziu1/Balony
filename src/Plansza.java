import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;


/**
 * Klasa okna planszy gry
 */
public class Plansza extends JFrame implements ActionListener, Runnable {
    private Image img;
    private Image balonik;
    private double proporcjaX;
    private double proporcjaY;
    private double droga;

  //  private JMenuBar menuBar;
    private JMenuItem wyjdz;
    private JMenuItem pauza;

   /* public JPanel getPlansza() {
        return plansza;
    }*/

    private double PRZESUNIECIE=10;
    private  double PRZESUNIECIEX;
    private  double PRZESUNIECIEY;
    private int przesuniecieWPoziomie;
    private int przesuniecieWPionie;
    private Random generator = new Random();
    private int losowa=generator.nextInt(4);
    private int losowa2=generator.nextInt(4);
    private boolean stoper, active;
    private Thread th;
    private OdczytPlanszy odczyt=new OdczytPlanszy();

    private Balon Naboj;
    private Balon NowyNaboj;
//    BufferedImage dbImage = new BufferedImage(11 * 60, 15 * 60, BufferedImage.TYPE_INT_ARGB);
//    Graphics dbg = dbImage.getGraphics();

    /**
     * Konstruktor wczytuj�cy dane planszy gry z pliku konfiguracyjnego.
     *
     * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
     * @throws IOException je�eli nie b�dzie mo�na nawi�za� po��czenia
     */

    Plansza(File plikStartowy) throws IOException {
       // OdczytPlanszy odczyt=new OdczytPlanszy();
        odczyt.Wczytaj(plikStartowy);
        setSize(odczyt.SZEROKOSC * 60, odczyt.WYSOKOSC * 60);
        StworzPustaPlansze(odczyt.WYSOKOSC, odczyt.SZEROKOSC);
        setTitle("Bubble Hit");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        MouseListenerPlansza();
        Naboj = new Balon((getWidth() / 2) - 30, getHeight() - 120);
        NowyNaboj = new Balon(getWidth() - 80, getHeight() - 70 );

     //   wczytajNowyNaboj();
        int czas = 5;
        Timer tm = new Timer(czas, this);
        tm.start();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                Menu okienko = new Menu();
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
//                    if(PRZESUNIECIEX==0&&PRZESUNIECIEY==0) {
                super.mouseClicked(e);

                    System.out.println(e.getPoint());
                    System.out.println("2");
                    th = new Thread(Plansza.this);
                    active = true;
                    th.start();
                  //  Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
//                }
                Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
                //Polozenie polozenieWyrzutni = new Polozenie(Naboj.getWsplX(), Naboj.getWsplY());
                przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - ((Naboj.getWsplX()+30)*(getWidth()/600));
                przesuniecieWPionie = gdzieKliknieto.getWsplY() - Naboj.getWsplY()-30;
                droga = Math.sqrt(Math.pow(przesuniecieWPionie,2) + Math.pow(przesuniecieWPoziomie,2)); //zmiana
                // System.out.println("droga przesuniecieX przesuniecieY " + droga + przesuniecieWPoziomie + przesuniecieWPionie);
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE * proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE * proporcjaY);


            }


        });
        }
        //setVisible(true);
       // setResizable(true);
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

        // System.out.println(" W modyf x->" + polozenieNaboju.getWsplX() + "y->" + polozenieNaboju.getWsplY());
        int a = (odczyt.SZEROKOSC - 1) * 60;

        if (Naboj.getWsplX() >= 0 && Naboj.getWsplX() <= a) {
            if (przesuniecieWPoziomie < 0) {
                Naboj.setWsplX((int) (Naboj.getWsplX() - PRZESUNIECIEX));
            }
            if (przesuniecieWPoziomie > 0) {
                Naboj.setWsplX((int) (Naboj.getWsplX() + PRZESUNIECIEX));
            }


        }
        if (Naboj.getWsplX() <= 0) {
            Naboj.setWsplX(0);
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (Naboj.getWsplX() >= a) {
            Naboj.setWsplX(a);
            //System.out.println(getWidth());
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (Naboj.getWsplY() >= 60 && Naboj.getWsplY() <= getHeight() - 60) {
            if (przesuniecieWPionie < 0) {
                Naboj.setWsplY((int) (Naboj.getWsplY() - PRZESUNIECIEY));
                if (przesuniecieWPionie > 0) {
                    Naboj.setWsplY((int) (Naboj.getWsplY() + PRZESUNIECIEY));
                }

            }
            if (Naboj.getWsplY() <= 60) {
                Naboj.setWsplY(60);
                int n=0;
                while(Naboj.getWsplX()>n*60+30) {
                    n++;
                }
                int dx = n*60;
                Naboj.setWsplX(dx);
                PRZESUNIECIEY = 0;
                PRZESUNIECIEX = 0;
            }


            if (Naboj.getWsplY() >= getHeight() - 60) {
                Naboj.setWsplY(getHeight() - 60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }
        }

        boolean czyMoznadalej = CzyDrogaWolna(odczyt.balony);
        if (!czyMoznadalej) {

            int n=0;
            while(Naboj.getWsplX()>n*60+30) {
                n++;
            }
            int dx = n*60;
            Naboj.setWsplX(dx);
            int m=0;
            while(Naboj.getWsplY()>m*60) {
                m++;
            }
            int dy = m*60;
            Naboj.setWsplY(dy);

            //stoper = true;
            PRZESUNIECIEX=0;
            PRZESUNIECIEY=0;
    //        balony.add(Naboj);
            NowyNaboj.setWsplX((getWidth() / 2) - 30);
            NowyNaboj.setWsplY(getHeight() - 120);
     //       wczytajNowyNaboj();
        }
    }

    private boolean CzyDrogaWolna(Vector<Balon> balony){
        for (Balon b: odczyt.balony){
            if (Math.sqrt(Math.pow(b.getWsplX() * 60 - (Naboj.getWsplX()),2)+Math.pow(Math.abs(b.getWsplY() * 60  - (Naboj.getWsplY())),2))<=45) {
                return false;
            }
                   /* if (Math.abs(b.getWsplX() * 60 + - (Naboj.getWsplX())) <= 45) {
                        if (Math.abs(b.getWsplY() * 60  - (Naboj.getWsplY())) <= 45){
                                return false;
                        }
                    }*/
        }
        return true;
    }

        public void wczytajNowyNaboj(Graphics g){
                Balon nowyNaboj = new Balon((getWidth()/2) - 80, getHeight() - 120 );
            switch(losowa) {
                case 0:
                    img = new ImageIcon("zielony.png").getImage();
                    g.drawImage(img, nowyNaboj.getWsplX(), nowyNaboj.getWsplY(), null);
                    nowyNaboj.setObrazekBalonu(img);
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
        //g.fillRect(0, 0, 60, WYSOKOSC * 60);
        //g.fillRect(SZEROKOSC * 60 - 60, 0, 60, WYSOKOSC * 60);
        g.fillRect(0, odczyt.WYSOKOSC * 60 - 60, odczyt.SZEROKOSC * 60, 60);
        for (Balon b : odczyt.balony) {
            switch (b.getKolor()) {
                case ZOLTY:
     //               g.setColor(Color.YELLOW);
                    b.setObrazekBalonu(img = new ImageIcon("zolty.png").getImage());
                    break;
                case CZERWONY:
//                    g.setColor(Color.RED);
                    b.setObrazekBalonu(img = new ImageIcon("czerwony.png").getImage());
                    break;
                case ZIELONY:
  //                  g.setColor(Color.GREEN);
                    b.setObrazekBalonu(img = new ImageIcon("zielony.png").getImage());
                    break;
                case NIEBIESKI:
 //                   g.setColor(Color.BLUE);
                    b.setObrazekBalonu(img = new ImageIcon("niebieski.png").getImage());
                    break;
                default:
          //          g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE)
            {
                //g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
                g.drawImage(b.getObrazekBalonu(), b.getWsplX() * 60, b.getWsplY() * 60, null);
            }
        }
        switch(losowa){
            case 0:
                img = new ImageIcon("zielony.png").getImage();
                g.drawImage(img, Naboj.getWsplX(), Naboj.getWsplY(), null);
                Naboj.setObrazekBalonu(img);
                break;
            case 1:
                img = new ImageIcon("czerwony.png").getImage();
                g.drawImage(img, Naboj.getWsplX(), Naboj.getWsplY(), null);
                Naboj.setObrazekBalonu(img);
                break;
            case 2:
                img = new ImageIcon("niebieski.png").getImage();
                g.drawImage(img, Naboj.getWsplX(), Naboj.getWsplY(), null);
                Naboj.setObrazekBalonu(img);
                break;
            case 3:
                img = new ImageIcon("zolty.png").getImage();
                g.drawImage(img, Naboj.getWsplX(), Naboj.getWsplY(), null);
                Naboj.setObrazekBalonu(img);
                break;
        }
        switch(losowa2){
            case 0:
                img = new ImageIcon("zielony.png").getImage();
                g.drawImage(img, NowyNaboj.getWsplX(), NowyNaboj.getWsplY(), null);
                NowyNaboj.setObrazekBalonu(img);
                break;
            case 1:
                img = new ImageIcon("czerwony.png").getImage();
                g.drawImage(img, NowyNaboj.getWsplX(), NowyNaboj.getWsplY(), null);
                NowyNaboj.setObrazekBalonu(img);
                break;
            case 2:
                img = new ImageIcon("niebieski.png").getImage();
                g.drawImage(img, NowyNaboj.getWsplX(), NowyNaboj.getWsplY(), null);
                NowyNaboj.setObrazekBalonu(img);
                break;
            case 3:
                img = new ImageIcon("zolty.png").getImage();
                g.drawImage(img, NowyNaboj.getWsplX(), NowyNaboj.getWsplY(), null);
                NowyNaboj.setObrazekBalonu(img);
                break;
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

    private void StworzPustaPlansze(int WYSOKOSC, int SZEROKOSC) {
        for (int i = 0; i < WYSOKOSC; i++) {
            for (int j = 0; j < SZEROKOSC; j++) {

                Polozenie wspolrzedne = new Polozenie(j, i);
                odczyt.polozenia.add(wspolrzedne);
                odczyt.pola.put(wspolrzedne, new Balon());
            }
        }
    }

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

    /**
     * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
     *
     * @param e przycisniecie przycisku
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Object z = e.getSource();
        if (z == this.wyjdz) {
            int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_NO_OPTION);
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
    @Override
    public void run() {

        while (true) {
            if(stoper){
                break;
            }
//            if(active){
                modyfikacjaPolozenia();
                repaint();
                Sleeeep(25);
//            }
        }
    }
}