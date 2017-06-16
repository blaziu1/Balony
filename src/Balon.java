import java.awt.*;

/**
 * Created by Błażej on 2017-05-26.
 */
public class Balon {
    Kolor kolor;
    private Image obrazekBalonu;
    boolean czyIstnieje;
    private int wsplX;
    private int wsplY;

    /**
     * Konstruktor tworzacy balon w okreslonym kolorze .
     *
     * @param kolor kolor balonu
     *
     */
    public Balon(Kolor kolor) {
        this.kolor = kolor;
        czyIstnieje=true;

    }
    public Balon(Kolor kolor, int wsplX, int wsplY){
        this.kolor = kolor;
        this.wsplX = wsplX;
        this.wsplY = wsplY;
        this.czyIstnieje = true;
    }
    public Balon(int wsplX, int wsplY){
        this.wsplX = wsplX;
        this.wsplY = wsplY;
        this.czyIstnieje = true;
    }

    public int getWsplX() {
        return wsplX;
    }

    public void setWsplX(int wsplX) {
        this.wsplX = wsplX;
    }

    public int getWsplY() {
        return wsplY;
    }

    public void setWsplY(int wsplY) {
        this.wsplY = wsplY;
    }
    /**
     * Konstruktor domyslny tworzacy "puste" miejsce na planszy.
     *
     *
     */

    public Balon() {
        kolor=Kolor.brak;
        czyIstnieje=false;
    }
    public Image getObrazekBalonu() {
        return obrazekBalonu;
    }

    public void setObrazekBalonu(Image obrazekBalonu) {
        this.obrazekBalonu = obrazekBalonu;
    }




    public Kolor getKolor() {
        return kolor;
    }

     public boolean equals(Object inny) {
        if (inny == null) return false;
        if (inny == this) return true;
        if (!(inny instanceof Polozenie)) return false;
        Polozenie innePolozenie = (Polozenie) inny;
        if (innePolozenie.getWsplY() == this.getWsplY() && innePolozenie.getWsplX() == this.getWsplX())
            return true;
        else return false;
    }

    public void setKolor(Kolor kolor) {
        this.kolor = kolor;
    }

   /* public boolean isCzyIstnieje() {
        return czyIstnieje;
    }
       public void setCzyIstnieje(boolean czyIstnieje) {
        this.czyIstnieje = czyIstnieje;
    }*/
}