package model;

/**
 *
 * @author maikel
 */
public class GameMap {
    private Tile[][] tiles;
    final static public TileType[][] DEFAULTTILES = {{TileType.ShallowWater,TileType.Land},{TileType.DeepWater,TileType.ShallowWater}};
    final static public GameMap DEFAULTMAP = new GameMap(DEFAULTTILES);
    
    public GameMap(TileType[][] types) {
        tiles = new Tile[types.length][types[0].length];
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < types[0].length; j++) {
                tiles[i][j] = new Tile(types[i][j]);
            }
        }
    }
    
    public int getWidth() {
        return tiles[0].length;
    }
    
    public int getHeight() {
        return tiles.length;
    }
    
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
    
    public static GameMap getDefaultMap() {
        return new GameMap(DEFAULTTILES);
    }
}
