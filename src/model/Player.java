package model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author maikel
 */
public class Player {
    private String name;
    private Set<Creature> creatures;
    
    public Player(String name) {
        this.name = name;
        this.creatures = new HashSet<Creature>();
    }
}
