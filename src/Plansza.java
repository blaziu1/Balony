import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;


/**
 * Klasa okna planszy gry
 */
public class Plansza extends JFrame implements ActionListener, Runnable {
    private ImageIcon img;
    private Image balonik;
    private int WYSOKOSC, SZEROKOSC;
    int czas = 5;
    private Timer tm = new Timer(czas, this);
    double proporcjaX;
    double proporcjaY;
    double droga;
    Polozenie polozenieNaboju;

    private JMenuBar menuBar;
    private JMenuItem wyjdz;
    private JMenuItem pauza;

    public JPanel getPlansza() {
        return plansza;
    }

    private JPanel plansza;
    private Properties pola = new Properties();
    double PRZESUNIECIE=10;
    double PRZESUNIECIEX;
    double PRZESUNIECIEY;
    private Vector<Polozenie> polozenia = new Vector<>();
    private Vector<Polozenie> polozenia2 = new Vector<>();
    int przesuniecieWPoziomie;
    int przesuniecieWPionie;
    Random generator = new Random();
    int losowa=generator.nextInt(4);

    /**
     * Konstruktor wczytuj�cy dane planszy gry z pliku konfiguracyjnego.
     *
     * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
     * @throws IOException je�eli nie b�dzie mo�na nawi�za� po��czenia
     */

    public Plansza(File plikStartowy) throws IOException {
        Wczytaj(plikStartowy);
        setTitle("Balony");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        polozenieNaboju = new Polozenie((getWidth()/2)-30, getHeight() - 120);
        //System.out.println("w konstruktorze x->" + polozenieNaboju.getWsplX() + "y->" + polozenieNaboju.getWsplY());
        tm.start();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                Menu okienko = new Menu();

            }


        });
        this.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *słuchacz myszki
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);
                System.out.println(e.getPoint());
                Thread th = new Thread(Plansza.this::run);
                th.start();

                Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
                Polozenie polozenieWyrzutni = new Polozenie(polozenieNaboju.getWsplX(), polozenieNaboju.getWsplY());
                przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - polozenieWyrzutni.getWsplX();
                przesuniecieWPionie = gdzieKliknieto.getWsplY() - polozenieWyrzutni.getWsplY();
                droga= Math.sqrt(przesuniecieWPionie*przesuniecieWPionie + przesuniecieWPoziomie*przesuniecieWPoziomie);
                // System.out.println("droga przesuniecieX przesuniecieY " + droga + przesuniecieWPoziomie + przesuniecieWPionie);
                proporcjaX = przesuniecieWPoziomie/droga;
                proporcjaY = przesuniecieWPionie/droga;
                PRZESUNIECIEX=Math.abs(PRZESUNIECIE*proporcjaX);
                PRZESUNIECIEY=Math.abs(PRZESUNIECIE*proporcjaY);
            }


        });
        setVisible(true);
        setResizable(true);


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

       // System.out.println(" W modyf x->" + polozenieNaboju.getWsplX() + "y->" + polozenieNaboju.getWsplY());
        int a = getWidth()-(getWidth()/SZEROKOSC);
        System.out.println(a);
        System.out.println(getWidth());
        System.out.println(SZEROKOSC);

        if (polozenieNaboju.getWsplX() >= 0 && polozenieNaboju.getWsplX() <= a) {
            if (przesuniecieWPoziomie < 0) {
                polozenieNaboju.setWsplX((int) (polozenieNaboju.getWsplX() - PRZESUNIECIEX));
            }
            if (przesuniecieWPoziomie > 0) {
                polozenieNaboju.setWsplX((int) (polozenieNaboju.getWsplX() + PRZESUNIECIEX));
            }


        }
        if(polozenieNaboju.getWsplX() <= 0)
        {
            polozenieNaboju.setWsplX(0);
            PRZESUNIECIEX=-1*PRZESUNIECIEX;
        }


        if(polozenieNaboju.getWsplX() >= a) {
            polozenieNaboju.setWsplX(a);
            //System.out.println(getWidth());
            PRZESUNIECIEX=-1*PRZESUNIECIEX;
        }

        //tutaj pr�bowa�em zrobi� odbijanie dla X
        /*else if (polozenieNaboju.getWsplX() < 60 || polozenieNaboju.getWsplX() > (getWidth() - 120))
        {
        	PRZESUNIECIEX=-PRZESUNIECIEX;
        	if (przesuniecieWPoziomie < 0) {
                polozenieNaboju.setWsplX((int) (polozenieNaboju.getWsplX() - PRZESUNIECIEX));
            }
            if (przesuniecieWPoziomie > 0) {
                polozenieNaboju.setWsplX((int) (polozenieNaboju.getWsplX() + PRZESUNIECIEX));
            }
        }*/

        if (polozenieNaboju.getWsplY() >= 60 && polozenieNaboju.getWsplY() <= getHeight() - 60) {
            if (przesuniecieWPionie < 0)
            {
                polozenieNaboju.setWsplY((int) (polozenieNaboju.getWsplY() - PRZESUNIECIEY));
                //if(polozenieNaboju.getWsplY() < 60)
                //System.out.println("chuj " + (PRZESUNIECIE) + " " + (proporcjaX) + " " + (proporcjaY) + " " +  PRZESUNIECIEY);
                //}
                if (przesuniecieWPionie > 0)
                {
                    polozenieNaboju.setWsplY((int) (polozenieNaboju.getWsplY() + PRZESUNIECIEY));
                }

            }
            if(polozenieNaboju.getWsplY() <= 60)
            {
                polozenieNaboju.setWsplY(60);
               // PRZESUNIECIEY=-1*PRZESUNIECIEY;
                PRZESUNIECIEY=0;
                PRZESUNIECIEX=0;
            }




            if(polozenieNaboju.getWsplY() >= getHeight() - 60)
            {
                polozenieNaboju.setWsplY(getHeight() - 60);
                PRZESUNIECIEY=-1*PRZESUNIECIEY;
            }

            //tym chcia�em zamkn�� w�tek ale nie dzia�a
        /*if (polozenieNaboju.getWsplX() < 60 || polozenieNaboju.getWsplX() > (getWidth() - 120))
        	tm.stop();

        if (polozenieNaboju.getWsplY() < 60 || polozenieNaboju.getWsplY() > getHeight() - 60)
        	tm.stop();*/

            //tutaj pr�bowa�em zrobi� odbijanie dla Y
        /*else if (polozenieNaboju.getWsplY() < 60 || polozenieNaboju.getWsplY() > getHeight() - 60)
        {
        	PRZESUNIECIEY=-PRZESUNIECIEY;
        	if (przesuniecieWPionie < 0)
            {
                polozenieNaboju.setWsplY((int) (polozenieNaboju.getWsplY() - PRZESUNIECIEY));
                System.out.println("chuj " + (PRZESUNIECIE) + " " + (proporcjaX) + " " + (proporcjaY) + " " +  PRZESUNIECIEY);
                }
            if (przesuniecieWPionie > 0)
            {
                polozenieNaboju.setWsplY((int) (polozenieNaboju.getWsplY() + PRZESUNIECIEY));
            }
        }*/

        }
    }
    /**
     * maluje komponent planszy gry
     *
     * @param g kontekst graficzny
     */


    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, SZEROKOSC * 60, WYSOKOSC * 60);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, SZEROKOSC * 60, 60);
        //g.fillRect(0, 0, 60, WYSOKOSC * 60);
        //g.fillRect(SZEROKOSC * 60 - 60, 0, 60, WYSOKOSC * 60);
        g.fillRect(0, WYSOKOSC * 60 - 60, SZEROKOSC * 60, 60);
        for (Polozenie p : polozenia) {
            Balon balon = (Balon) pola.get(p);
            switch (balon.getKolor()) {
                case ZOLTY:
                    g.setColor(Color.YELLOW);
                    img = new ImageIcon("zolty.png");
                    break;
                case CZERWONY:
                    g.setColor(Color.RED);
                    img = new ImageIcon("czerwony.png");
                    break;
                case ZIELONY:
                    g.setColor(Color.GREEN);
                    img = new ImageIcon("zielony.png");
                    break;
                case NIEBIESKI:
                    g.setColor(Color.BLUE);
                    img = new ImageIcon("niebieski.png");
                    break;
                default:
                    g.setColor(Color.WHITE);
            }
            if (g.getColor() != Color.WHITE)
            {
                //g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
                balonik = img.getImage();
                g.drawImage(balonik, p.getWsplX() * 60, p.getWsplY() * 60, null);
            }
        }

        //g.setColor(Color.black);

        //g.fillOval(polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, 60, 60);
        switch(losowa){
            case 0:
                img = new ImageIcon("czerwony.png");
                balonik = img.getImage();
                g.drawImage(balonik, polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, null);
                break;
            case 1:
                img = new ImageIcon("zolty.png");
                balonik = img.getImage();
                g.drawImage(balonik, polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, null);
                break;
            case 2:
                img = new ImageIcon("zielony.png");
                balonik = img.getImage();
                g.drawImage(balonik, polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, null);
                break;
            case 3:
                img = new ImageIcon("niebieski.png");
                balonik = img.getImage();
                g.drawImage(balonik, polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, null);
                break;
        }

        g.dispose();
        setFocusable(true);

    }

    public void paint(Graphics g) {
        BufferedImage dbImage = new BufferedImage(SZEROKOSC * 60, WYSOKOSC * 60, BufferedImage.TYPE_INT_ARGB);
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);

        BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = scaled.createGraphics();
        gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(scaled, 0, 0, this);
    }

    /**
     * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku konfiguracyjnego
     *
     * @param plikStartowy bie��ca linijka
     * @throws IOException je�eli nie uda si� otworzyc pliku
     */
    private void Wczytaj(File plikStartowy) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(plikStartowy))) {
            String line = br.readLine();
            while (line != null) {
                if (line.contains("WYMIARY")) {
                    String[] wymiaryString = line.split("\\s+");
                    SZEROKOSC = Integer.parseInt(wymiaryString[1]);
                    WYSOKOSC = Integer.parseInt(wymiaryString[2]);

                    setSize(SZEROKOSC * 60, WYSOKOSC * 60);
                    StworzPustaPlansze(WYSOKOSC, SZEROKOSC);

                    line = br.readLine();
                } else {
                    try {
                        line = WczytajBalony(br, line);
                    } catch (NullPointerException e) {
                        break;
                    }
                }

            }

        }

    }

    /**
     * Metoda wczytuje po�ozenie balon�w  z pliku kofiguracyjnego.
     *
     * @param line bie��ca linijka
     * @param br   bufor czytnika
     * @return bierzaca linie
     * @throws IOException je�eli nie uda si� odczyta� kolejnej linijki
     */
    private String WczytajBalony(BufferedReader br, String line) throws IOException {

        if (line.contains("#"))
            line = br.readLine();
        else {
            WczytajPole(line);
            line = br.readLine();
        }

        return line;
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
                polozenia.add(wspolrzedne);
                pola.put(wspolrzedne, new Balon());
            }
        }
    }

    /**
     * Metoda wczytuje z odczytanej lini pliku po�ozenie i kolor balona a obiektu klasy Plansza.
     *
     * @param line bie��ca linijka pliku nad ktora pracuje metoda
     */


    private void WczytajPole(String line) {

        String[] balonString = line.split("\\s+");
        try {
            int wsplX = Integer.parseInt(balonString[0]);
            int wsplY = Integer.parseInt(balonString[1]);
            int kolorInt = Integer.parseInt(balonString[2]);
            Kolor kolor;
            Polozenie wspolrzedneBalona = new Polozenie(wsplX, wsplY);
            kolor = getKolor(kolorInt);
            Balon balon = new Balon(kolor);
            for (Polozenie p : polozenia) {
                if (p.equals(wspolrzedneBalona))
                    wspolrzedneBalona = p;
                pola.replace(wspolrzedneBalona, balon);
            }


        } catch (ArrayIndexOutOfBoundsException e) {
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
            case 88:
                kolor = Kolor.CZARNY;
                break;
            case 69:
                kolor = Kolor.TECZOWY;
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
            Polozenie temp = new Polozenie(polozenieNaboju.getWsplX(), polozenieNaboju.getWsplY());
            modyfikacjaPolozenia();
            if (polozenieNaboju != temp) {
                repaint();
                Sleeeep(25);
            } else
                break;


        }
    }
}