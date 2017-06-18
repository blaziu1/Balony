import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * Opowiedzialna za wczytanie wygladu planszy z pliku tekstowego oraz za wczytanie poziomu trudnosci.
 */
class MapLoad {
    int HEIGHT, WIDTH, DESCEND, NUM_OF_COLOURS;
    private Vector<Location> coordinates = new Vector<>();
    Vector<Balloon> ballons = new Vector<>();
    Properties pola = new Properties();

    /**
     * Wczytuje i parsuje informacje o poszczegolnych polach na mapie z pliku tekstowego.
     * Liczby w pliku tekstowym oddzielane sa spacja.
     * Pierwsza oznacza wspolrzedna x, druga wspolrzedna y a trzecia to colour balonu.
     * @param line
     */
    private void loadLine(String line) {

        String[] balonString = line.split("\\s+");
        try {
            int x = Integer.parseInt(balonString[0]);
            int y = Integer.parseInt(balonString[1]);
            int ColourCode = Integer.parseInt(balonString[2]);
            try{
                readDifficulty();
               }
            catch(IOException e)
                {
                    System.out.println("ERROR: IOException");
                }
            Location ballonsCoordinates = new Location(x, y);
            Colour colour = getColour(ColourCode);
            Balloon balloon = new Balloon(colour, x, y);
            ballons.add(balloon);
            for (Location p : coordinates) {
                if (p.equals(ballonsCoordinates))
                    ballonsCoordinates = p;
                pola.replace(ballonsCoordinates, balloon);
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR = ArrayIndexOutOfBoundsException in loadFile Pole");
        }
    }

    /**
     * Wczytuje wysokosc i szerokosc okna a takze tryb gry z pliku tekstowego.
     * Wywoluje metode loadBalloons.
     * @param plikStartowy Odczytywany plik tekstowy zawierajacy dane dotyczace wygladu mapy.
     * @throws IOException Rzuca wyjatek jesli plik tekstowy nie zostal odnaleziony.
     */
    void loadFile(File plikStartowy) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(plikStartowy))) {
            String line = br.readLine();
            while (line != null) {
                if (line.contains("WYMIARY")) {
                    String[] parameters = line.split("\\s+");
                    WIDTH = Integer.parseInt(parameters[1]);
                    HEIGHT = Integer.parseInt(parameters[2]);
                    DESCEND = Integer.parseInt(parameters[3]);

                    line = br.readLine();
                } else {
                    try {
                        line = loadBalloons(br, line);
                    } catch (NullPointerException e) {
                        break;
                    }
                }

            }

        }

    }

    /**
     * Wczytuje poszczegolne linie z pliku tekstowego.
     * Jesli linia zawiera znak "#" jest pomijana.
     * W pozostalych przypadkach wywolywana jest metoda wczytajPole().
     * @param br klasy BufferedReader przekazywany z metody wczytaj().
     * @param line klasy String, zapisywana w niej jest wczytana linia.
     * @return linie tekstu typu String
     * @throws IOException Wyjatek jesli nie zostal odnaleziony plik tekstowy.
     */
    private String loadBalloons(BufferedReader br, String line) throws IOException {

        if (line.contains("#"))
            line = br.readLine();
        else {
            loadLine(line);
            line = br.readLine();
        }

        return line;
    }

    /**
     * Wczytuje oraz parsuje plik tekstowy zawierajacy informacje o poziomie trudnosci.
     * @throws FileNotFoundException Jesli nie zostal odnaleziony plik tekstowy.
     */
    private void readDifficulty() throws FileNotFoundException{
        Scanner in = new Scanner(new File("difficulty.txt"));
        String zdanie = in.nextLine();
        NUM_OF_COLOURS =Integer.parseInt(zdanie);
    }

    /**
     * Sluzy do nadania balonom koloru zaleznie od informacji zawartej w pliku tekstowym.
     * Liczbie 6 w pliku tekstowym odpowiada kolor losowy.
     * Ilosc kolorow zalezy od poziomu trudnosci.
     * @param ColourCode wczytana z pliku i sparsowana cyfra ktora zostaje w tej funkcji zamieniona w kolor balonu.
     * @return Colour balonu.
     */
    Colour getColour(int ColourCode) {
        Colour colour;
        if (ColourCode == 6) {
            Random rand = new Random();
            ColourCode = rand.nextInt(NUM_OF_COLOURS) + 1;

        }
        switch (ColourCode) {
            case 1:
                colour = Colour.ZIELONY;
                break;
            case 2:
                colour = Colour.CZERWONY;
                break;
            case 3:
                colour = Colour.NIEBIESKI;
                break;
            case 4:
                colour = Colour.ZOLTY;
                break;
            default:
                colour = Colour.brak;

        }
        return colour;
    }
}
