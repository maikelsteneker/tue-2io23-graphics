package model;

import java.util.Set;

/**
 *
 * @author maikel
 */
public class Game {
    private GameMap map;
    public Set<Player> players;
    
    public Game(Set<Player> players) throws InvalidNumberOfPlayersException {
        if (players.size() < 2 && players.size() > 6) {
            throw new InvalidNumberOfPlayersException();
        }
        this.players = players;
        this.map = GameMap.getDefaultMap();
    }
    
    public Game(Set<Player> players, GameMap map) throws InvalidNumberOfPlayersException {
        if (players.size() < 2 && players.size() > 6) {
            throw new InvalidNumberOfPlayersException();
        }
        this.players = players;
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }
    
    class InvalidNumberOfPlayersException extends Exception {

        public InvalidNumberOfPlayersException() {
        }
    }
}
