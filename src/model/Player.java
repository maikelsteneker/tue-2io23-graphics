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
    private Set<Creature> creatures;
    private Creature currentCreature;
    
    public Player(String name) {
        this.name = name;
        this.creatures = new HashSet<Creature>();
        this.currentCreature = null;
    }

    public Collection<Creature> getCreatures() {
        return creatures;
    }
    
    public void setCreatures(Set<Creature> creatures) {
        this.creatures = creatures;
        currentCreature = (Creature) creatures.toArray()[0];
    }

    public Creature getCurrentCreature() {
        return currentCreature;
    }
}
