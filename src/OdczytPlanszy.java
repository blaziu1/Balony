import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;


class OdczytPlanszy {
    int WYSOKOSC, SZEROKOSC, DESCEND;
    Vector<Polozenie> polozenia = new Vector<>();
    Vector<Balon> balony = new Vector<>();
    Properties pola = new Properties();
    
    private void WczytajPole(String line) {

        String[] balonString = line.split("\\s+");
        try {
            int wsplX = Integer.parseInt(balonString[0]);
            int wsplY = Integer.parseInt(balonString[1]);
            int kolorInt = Integer.parseInt(balonString[2]);
            Polozenie wspolrzedneBalona = new Polozenie(wsplX, wsplY);
            Kolor kolor = getKolor(kolorInt);
            Balon balon = new Balon(kolor, wsplX, wsplY);
            balony.add(balon);
            for (Polozenie p : polozenia) {
                if (p.equals(wspolrzedneBalona))
                    wspolrzedneBalona = p;
                pola.replace(wspolrzedneBalona, balon);
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR = ArrayIndexOutOfBoundsException in Wczytaj Pole");
        }
    }

    void Wczytaj(File plikStartowy) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(plikStartowy))) {
            String line = br.readLine();
            while (line != null) {
                if (line.contains("WYMIARY")) {
                    String[] wymiaryString = line.split("\\s+");
                    SZEROKOSC = Integer.parseInt(wymiaryString[1]);
                    WYSOKOSC = Integer.parseInt(wymiaryString[2]);
                    DESCEND = Integer.parseInt(wymiaryString[3]);

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

    private String WczytajBalony(BufferedReader br, String line) throws IOException {

        if (line.contains("#"))
            line = br.readLine();
        else {
            WczytajPole(line);
            line = br.readLine();
        }

        return line;
    }
    public Kolor getKolor(int kolorInt) {
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
}
