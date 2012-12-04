package model;

/**
 *
 * @author maikel
 */
public class Creature extends Inhabitant {
    private int life;
    
    public Creature() {
        this.life = 15;
    }

    private boolean canSelect() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void select() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void attack(Creature c, int dmg) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void move(Tile to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void eat(Food f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private void eat(Creature c) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
