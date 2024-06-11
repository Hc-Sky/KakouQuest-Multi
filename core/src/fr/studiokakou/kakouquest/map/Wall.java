package fr.studiokakou.kakouquest.map;

/**
 * Represents a wall object in the game map.
 */
public class Wall {
    Point pos;

    public String assetPath;

    /**
     * Constructs a new wall object.
     *
     * @param pos         The position of the wall on the map.
     * @param assetPath   The file path to the texture asset used for rendering the wall.
     */
    public Wall(Point pos, String assetPath){
        this.pos = pos;
        this.assetPath=assetPath;
    }

    public Wall(OnlineWall wall){
        this.pos = wall.pos;
        this.assetPath = wall.assetPath;
    }
}
