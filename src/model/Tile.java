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
}
