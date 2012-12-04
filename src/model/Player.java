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
    private int currentCreatureIndex;
    
    public Player(String name) {
        this.name = name;
        this.creatures = new Creature[6];
        this.currentCreatureIndex = 0;
    }
    
    public void setCreatures(Creature[] creatures) {
        this.creatures = creatures;
        currentCreatureIndex = 0;
    }

    public Creature getCurrentCreature() {
        return creatures[currentCreatureIndex];
    }
    
    public int SetCurrentCreature(int index) {
        return this.currentCreatureIndex = index;
    }
}
