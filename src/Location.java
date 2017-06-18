/**
 *
 */
public class Location {
    private int wsplX;
    private int wsplY;

    Location(int wsplX, int wsplY) {
        this.wsplX = wsplX;
        this.wsplY = wsplY;
    }

    int getWsplX() {
        return wsplX;
    }

    int getWsplY() {
        return wsplY;
    }

    @Override
    public boolean equals(Object inny) {
        if (inny == null) return false;
        if (inny == this) return true;
        if (!(inny instanceof Location)) return false;
        Location innePolozenie = (Location) inny;
        if (innePolozenie.getWsplY() == this.getWsplY() && innePolozenie.getWsplX() == this.getWsplX())
            return true;
        else return false;
    }
}
