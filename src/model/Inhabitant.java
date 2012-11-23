package model;

/**
 *
 * @author maikel
 */
class Inhabitant {

    Tile tile;

    public void setTile(Tile t) throws UseTheOtherMethodException {
        if (!tile.getInhabitants().contains(this)) {
            throw new UseTheOtherMethodException();
        }
        this.tile = t;
    }

    class UseTheOtherMethodException extends Exception {

        public UseTheOtherMethodException() {
        }
    }
}
