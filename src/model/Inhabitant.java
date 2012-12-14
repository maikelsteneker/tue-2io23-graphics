package model;

/**
 *
 * @author maikel
 */
public class Inhabitant {

    Tile tile;
    public int x,y;

    public void setTile(Tile t) throws UseTheOtherMethodException {
        if (!t.getInhabitants().contains(this)) {
            throw new UseTheOtherMethodException();
        }
        this.tile = t;
    }

    class UseTheOtherMethodException extends Exception {

        public UseTheOtherMethodException() {
        }
    }
}
