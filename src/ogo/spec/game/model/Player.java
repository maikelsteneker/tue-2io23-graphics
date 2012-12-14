package ogo.spec.game.model;

import java.util.Arrays;
import java.util.Iterator;

public class Player implements Iterable<Creature>
{
    private String name;
       
    private Creature[] creatures;

    public Player(String name) {
        this.name = name;
    }

    public void setCreatures(Creature[] creatures) {
        this.creatures = creatures;
    }

    @Override
    public Iterator<Creature> iterator() {
        return Arrays.asList(creatures).iterator();
    }
}