package fr.studiokakou.kakouquest.map;

import com.badlogic.gdx.graphics.Texture;
import fr.studiokakou.kakouquest.utils.Utils;

import java.util.ArrayList;

/**
 * The Floor class represents a floor tile in the game.
 */
public class Floor {
    Point pos;

    // texture vars
    public int textureIndex;
    public static float TEXTURE_WIDTH = 16;
    public static float TEXTURE_HEIGHT = 16;
    public Texture texture;

    /**
     * Constructs a Floor object at the specified position.
     *
     * @param x the x-coordinate of the floor tile
     * @param y the y-coordinate of the floor tile
     */
    public Floor(float x, float y) {
        this.pos = new Point(x, y);

        // selects a random texture from the possible ones
        if (Utils.randint(0, 4) == 0) {
            this.textureIndex = Utils.randint(1, 7);
        } else {
            this.textureIndex = 0;
        }
    }

    /**
     * Constructs a Floor object from an OnlineFloor object.
     *
     * @param onlineFloor the OnlineFloor object to base this Floor on
     */
    public Floor(OnlineFloor onlineFloor) {
        this.pos = new Point(onlineFloor.pos.x, onlineFloor.pos.y);
        this.textureIndex = onlineFloor.textureIndex;
    }

    /**
     * Checks the surrounding of the floor and returns a list of walls surrounding it.
     *
     * @param floors a list of all floor tiles
     * @return a list of walls surrounding this floor tile
     */
    public ArrayList<Wall> getSurrounding(ArrayList<Floor> floors) {
        ArrayList<Point> result = new ArrayList<>();
        result.add(this.pos.add(1, 0));
        result.add(this.pos.add(-1, 0));
        result.add(this.pos.add(0, 1));
        result.add(this.pos.add(0, -1));

        for (Floor f : floors) {
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

        ArrayList<Wall> walls = new ArrayList<>();

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

        // based on the surrounding, add the appropriate walls
        if (orientation.equals(new Point(-1, -1))) {
            walls.add(new Wall(this.pos.add(-1, -1), "assets/map/wall_outer_front_left.png"));
            walls.add(new Wall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos, "assets/map/wall_top_mid.png"));
            walls.add(new Wall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
        }
        if (orientation.equals(new Point(1, -1))) {
            walls.add(new Wall(this.pos.add(1, -1), "assets/map/wall_outer_front_right.png"));
            walls.add(new Wall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos, "assets/map/wall_top_mid.png"));
            walls.add(new Wall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
        }

        if (orientation.equals(new Point(-1, 1))) {
            walls.add(new Wall(this.pos.add(-1, 2), "assets/map/wall_outer_top_left.png"));
            walls.add(new Wall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos.add(-1, 1), "assets/map/wall_edge_mid_right.png"));
            walls.add(new Wall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
            walls.add(new Wall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }
        if (orientation.equals(new Point(1, 1))) {
            walls.add(new Wall(this.pos.add(1, 2), "assets/map/wall_outer_top_right.png"));
            walls.add(new Wall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos.add(1, 1), "assets/map/wall_edge_mid_left.png"));
            walls.add(new Wall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
            walls.add(new Wall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }

        if (orientation.equals(new Point(-1, 0))) {
            walls.add(new Wall(this.pos.add(-1, 0), "assets/map/wall_edge_mid_right.png"));
        }
        if (orientation.equals(new Point(1, 0))) {
            walls.add(new Wall(this.pos.add(1, 0), "assets/map/wall_edge_mid_left.png"));
        }
        if (orientation.equals(new Point(0, -1))) {
            walls.add(new Wall(this.pos.add(0, -1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos, "assets/map/wall_top_mid.png"));
        }
        if (orientation.equals(new Point(0, 1))) {
            walls.add(new Wall(this.pos.add(0, 1), "assets/map/wall_mid.png"));
            walls.add(new Wall(this.pos.add(0, 2), "assets/map/wall_top_mid.png"));
        }

        return walls;
    }
}
