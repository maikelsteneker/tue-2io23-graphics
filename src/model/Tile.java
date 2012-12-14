package model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Inhabitant.UseTheOtherMethodException;

/**
 *
 * @author maikel
 */
public class Tile {
    private Set<Inhabitant> inhabitants;
    private TileType type;
    public int x,y;
    
    public Tile(TileType t) {
        this.inhabitants = new HashSet<Inhabitant>();
        this.type = t;
    }
    
    public void addInhabitant(Inhabitant e) {
        inhabitants.add(e);
        try {
            e.setTile(this);
        } catch (UseTheOtherMethodException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TileType getType() {
        return type;
    }

    public Set<Inhabitant> getInhabitants() {
        return inhabitants;
    }

    /**
     * Returns the Creature on this Tile, or null if there is no creature on
     * this tile. Note that there cannot be two creatures on a tile at once.
     */
    public Creature getCreature() {
        for (Inhabitant i : inhabitants) {
            if (i instanceof Creature) {
                return (Creature) i;
            }
        }
        return null;
    }

    public boolean removeInhabitant(Creature c) {
        c.tile = null;
        return inhabitants.remove(c);
    }
}
