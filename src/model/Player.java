package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author maikel
 */
public class Player {
    private String name;
    private Creature[] creatures;
    
    public Player(String name) {
        this.name = name;
        this.creatures = new Creature[6];
    }
    
    public void setCreatures(Creature[] creatures) {
        this.creatures = creatures;
    }
}
