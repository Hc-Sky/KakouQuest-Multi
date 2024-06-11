package fr.studiokakou.kakouquest.map;

/**
 * The OnlineWall class represents a wall tile in an online game map.
 */
public class OnlineWall {
    Point pos;

    String assetPath;

    /**
     * Constructs an OnlineWall object with the specified position and asset path.
     *
     * @param pos        the position of the wall tile
     * @param assetPath  the asset path of the wall texture
     */
    public OnlineWall(Point pos, String assetPath) {
        this.pos = pos;
        this.assetPath = assetPath;
    }

    /**
     * Constructs an OnlineWall object based on an existing Wall object.
     *
     * @param wall  the Wall object from which to construct the OnlineWall
     */
    public OnlineWall(Wall wall) {
        this.pos = wall.pos;
        this.assetPath = wall.assetPath;
    }

    /**
     * Default constructor for OnlineWall.
     */
    public OnlineWall() {}
}
