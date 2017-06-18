import java.awt.*;

/**
 * Klasa przechowujaca balon
 */
public class Balloon {

    Colour colour;
    private Image ballonImage;
    private int xCoordinate;
    private int yCoordinate;

    /**
     * Konstruktor klasy Balloon.
     * Tworzy balon nadajac mu colour i wspolrzedne na mapie
     * @param colour kolor balonu.
     * @param x Wspolrzedna x balonu.
     * @param y Wspolrzedna y balonu.
     */
    Balloon(Colour colour, int x, int y){
        this.colour = colour;
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    /**
     * Zwraca wspolrzedna x balonu.
     * @return wspolrzedna x balonu.
     */
    int getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Ustawia wspolrzedna x balonu na dana w parametrze
     * @param xCoordinate Nowa wspolrzedna x balonu
     */
    void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    /**
     * Zwraca wspolrzedna y balonu.
     * @return wspolrzedna y balonu.
     */
    int getyCoordinate() {
        return yCoordinate;
    }

    /**
     * Ustawia wspolrzedna y balonu na dana w parametrze.
     * @param yCoordinate Nowa wspolrzedna y balonu.
     */
    void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**
     * Zwraca obrazek Image balonu.
     * @return obrazek klasy Image balonu.
     */
    Image getBallonImage() {
        return ballonImage;
    }

    /**
     * Ustawia obrazek balonu na obrazek klasy Image dany w parametrze.
     * @param ballonImage Nowy obrazek balonu.
     */
    void setBallonImage(Image ballonImage) {
        this.ballonImage = ballonImage;
    }

    /**
     * Zwraca kolor balonu.
     * @return Colour balonu.
     */
    Colour getColour() {
        return colour;
    }

     public boolean equals(Object inny) {
        if (inny == null) return false;
        if (inny == this) return true;
        if (!(inny instanceof Location)) return false;
        Location innePolozenie = (Location) inny;
        if (innePolozenie.getWsplY() == this.getyCoordinate() && innePolozenie.getWsplX() == this.getxCoordinate())
            return true;
        else return false;
    }
}