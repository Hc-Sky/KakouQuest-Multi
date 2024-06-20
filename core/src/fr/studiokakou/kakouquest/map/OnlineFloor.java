package fr.studiokakou.kakouquest.map;

import fr.studiokakou.kakouquest.utils.Utils;

import java.util.ArrayList;

/**
 * The OnlineFloor class represents a floor tile in an online game map.
 */
public class OnlineFloor {
    public Point pos;

    public int textureIndex;

    /**
     * Constructs an OnlineFloor object at the specified position.
     *
     * @param pos the position of the floor tile
     */
    public OnlineFloor(Point pos) {
        this.pos = pos;
        this.textureIndex = (Utils.randint(0, 4) == 0) ? Utils.randint(1, 7) : 0;
    }

    /**
     * Constructs an OnlineFloor object at the specified coordinates.
     *
     * @param x the x-coordinate of the floor tile
     * @param y the y-coordinate of the floor tile
     */
    public OnlineFloor(float x, float y) {
        this.pos = new Point(x, y);
        this.textureIndex = (Utils.randint(0, 4) == 0) ? Utils.randint(1, 7) : 0;
    }

    /**
     * Default constructor for OnlineFloor.
     */
    public OnlineFloor() {}

    /**
     * Gets the surrounding walls of this floor tile based on the positions of other floor tiles.
     *
     * @param floors the list of floor tiles in the map
     * @return the list of surrounding walls
     */
    public ArrayList<OnlineWall> getSurrounding(ArrayList<OnlineFloor> floors) {
        ArrayList<Point> result = new ArrayList<>();
        result.add(this.pos.add(1, 0));
        result.add(this.pos.add(-1, 0));
        result.add(this.pos.add(0, 1));
        result.add(this.pos.add(0, -1));

        for (OnlineFloor f : floors) {
            if (f.pos.equals(this.pos.add(1, 0))) {
                result.set(0, null);
            }
            if (f.pos.equals(this.pos.add(-1, 0))) {
                result.set(1, null);
            }
            if (f.pos.equals(this.pos.add(0, 1))) {
                result.set(2, null);
            }
            if (f.pos.equals(this.pos.add(0, -1))) {
                result.set(3, null);
            }
        }

        ArrayList<OnlineWall> walls = new ArrayList<>();
        Point orientation = new Point(0, 0);
        if (result.get(0) != null) {
            orientation = orientation.add(1, 0);
        }
        if (result.get(1) != null) {
            orientation = orientation.add(-1, 0);
        }
        if (result.get(2) != null) {
            orientation = orientation.add(0, 1);
        }
        if (result.get(3) != null) {
            orientation = orientation.add(0, -1);
        }

        if (orientation.equals(new Point(-1, -1))) {
            walls.add(new OnlineWall(this.pos.add(-1, -1), "assets/map/wall_outer_front_left.png"));
            walls.add(new OnlineWall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos, "assets/map/wall_top_mid.png"));
            walls.add(new OnlineWall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
        }
        if (orientation.equals(new Point(1, -1))) {
            walls.add(new OnlineWall(this.pos.add(1, -1), "assets/map/wall_outer_front_right.png"));
            walls.add(new OnlineWall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos, "assets/map/wall_top_mid.png"));
            walls.add(new OnlineWall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
        }
        if (orientation.equals(new Point(-1, 1))) {
            walls.add(new OnlineWall(this.pos.add(-1, 2), "assets/map/wall_outer_top_left.png"));
            walls.add(new OnlineWall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos.add(-1, 1), "assets/map/wall_edge_mid_right.png"));
            walls.add(new OnlineWall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
            walls.add(new OnlineWall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }
        if (orientation.equals(new Point(1, 1))) {
            walls.add(new OnlineWall(this.pos.add(1, 2), "assets/map/wall_outer_top_right.png"));
            walls.add(new OnlineWall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos.add(1, 1), "assets/map/wall_edge_mid_left.png"));
            walls.add(new OnlineWall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
            walls.add(new OnlineWall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }
        if (orientation.equals(new Point(-1, 0))) {
            walls.add(new OnlineWall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
        }
        if (orientation.equals(new Point(1, 0))) {
            walls.add(new OnlineWall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
        }
        if (orientation.equals(new Point(0, -1))) {
            walls.add(new OnlineWall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos, "assets/map/wall_top_mid.png"));
        }
        if (orientation.equals(new Point(0, 1))) {
            walls.add(new OnlineWall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new OnlineWall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }

        return walls;
    }
}
