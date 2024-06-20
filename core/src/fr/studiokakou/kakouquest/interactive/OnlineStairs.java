package fr.studiokakou.kakouquest.interactive;

import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;

import java.util.ArrayList;

/**
 * The OnlineStairs class represents stairs in the online game, used for moving between levels or areas.
 */
public class OnlineStairs {
    public Point pos;
    public ArrayList<OnlinePlayer> players = new ArrayList<>();

    /**
     * Constructs an OnlineStairs object based on an existing Stairs object.
     *
     * @param stairs the existing Stairs object to base this OnlineStairs on
     */
    public OnlineStairs(Stairs stairs) {
        this.pos = stairs.pos;
    }

    /**
     * Constructs an OnlineStairs object at a given position.
     *
     * @param pos the position of the stairs
     */
    public OnlineStairs(Point pos) {
        this.pos = pos;
    }

    /**
     * Default constructor for OnlineStairs.
     */
    public OnlineStairs() {}

    /**
     * Converts this OnlineStairs object to a Stairs object.
     *
     * @return a new Stairs object with the same position
     */
    public Stairs toStairs() {
        Stairs stairs = new Stairs(this.pos);
        return stairs;
    }
}
