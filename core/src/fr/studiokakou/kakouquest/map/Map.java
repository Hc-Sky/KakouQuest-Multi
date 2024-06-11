package fr.studiokakou.kakouquest.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.entity.Monster;
import fr.studiokakou.kakouquest.interactive.Chest;
import fr.studiokakou.kakouquest.interactive.Interactive;
import fr.studiokakou.kakouquest.interactive.Stairs;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.network.ServerMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * The Map class represents a game map and provides methods for managing and drawing the map.
 */
public class Map {
    public ArrayList<Floor> floors = new ArrayList<>();

    // map dimensions
    public int map_height;
    public int map_width;
    public static int ROOM_MIN_HEIGHT = 7;
    public static int ROOM_MIN_WIDTH = 7;
    public static int ROOM_MAX_HEIGHT = 21;
    public static int ROOM_MAX_WIDTH = 21;

    public Stairs stairs;

    public boolean drawingMonsters = false;

    public ArrayList<Interactive> interactives = new ArrayList<>();

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Bridge> bridges = new ArrayList<>();
    ArrayList<Wall> walls = new ArrayList<>();

    // map textures
    public static Texture[] FLOOR_TEXTURES;
    public static HashMap<String, Texture> WALL_TEXTURES;
    public static Texture STAIRS_TEXTURE;

    public static Animation<TextureRegion> interactKeyAnimation;

    public static ArrayList<Chest> chests = new ArrayList<>();

    public static ArrayList<Monster> monsters = new ArrayList<>();

    /**
     * Constructs a Map object with the specified dimensions.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public Map(int width, int height) {
        this.map_height = height;
        this.map_width = width;

        // load floors textures
        FLOOR_TEXTURES = new Texture[]{
                new Texture("assets/map/floor_1.png"),
                new Texture("assets/map/floor_2.png"),
                new Texture("assets/map/floor_3.png"),
                new Texture("assets/map/floor_4.png"),
                new Texture("assets/map/floor_5.png"),
                new Texture("assets/map/floor_6.png"),
                new Texture("assets/map/floor_7.png"),
                new Texture("assets/map/floor_8.png")
        };

        // load walls textures
        WALL_TEXTURES = new HashMap<>();
        WALL_TEXTURES.put("assets/map/wall_outer_front_left.png", new Texture("assets/map/wall_outer_front_left.png"));
        WALL_TEXTURES.put("assets/map/wall_mid.png", new Texture("assets/map/wall_mid.png"));
        WALL_TEXTURES.put("assets/map/wall_top_mid.png", new Texture("assets/map/wall_top_mid.png"));
        WALL_TEXTURES.put("assets/map/wall_edge_mid_right.png", new Texture("assets/map/wall_edge_mid_right.png"));
        WALL_TEXTURES.put("assets/map/wall_outer_front_right.png", new Texture("assets/map/wall_outer_front_right.png"));
        WALL_TEXTURES.put("assets/map/wall_edge_mid_left.png", new Texture("assets/map/wall_edge_mid_left.png"));
        WALL_TEXTURES.put("assets/map/wall_outer_top_left.png", new Texture("assets/map/wall_outer_top_left.png"));
        WALL_TEXTURES.put("assets/map/wall_outer_top_right.png", new Texture("assets/map/wall_outer_top_right.png"));
        WALL_TEXTURES.put("assets/map/wall_outer_mid_right.png", new Texture("assets/map/wall_outer_mid_right.png"));
        WALL_TEXTURES.put("assets/map/wall_outer_mid_left.png", new Texture("assets/map/wall_outer_mid_left.png"));

        //load stairs texture
        STAIRS_TEXTURE = new Texture("assets/map/floor_ladder.png");
    }

    /**
     * Constructs a Map object from a ServerMap object.
     *
     * @param onlineMap the ServerMap object
     */
    public Map(ServerMap onlineMap) {
        this.floors.clear();
        this.walls.clear();
        chests.clear();

        this.map_height = onlineMap.map_height;
        this.map_width = onlineMap.map_width;

        this.rooms = onlineMap.rooms;
        this.bridges = onlineMap.bridges;

        for (int i = 0; i < onlineMap.floors.size(); i++) {
            this.floors.add(new Floor(onlineMap.floors.get(i)));
        }

        for (int i = 0; i < onlineMap.walls.size(); i++) {
            this.walls.add(new Wall(onlineMap.walls.get(i)));
        }

        for (int i = 0; i < onlineMap.chests.size(); i++) {
            Chest chest = new Chest(onlineMap.chests.get(i));
            chests.add(chest);
            this.interactives.add(chest);
        }
    }

    /**
     * Refreshes the interactables in the map.
     */
    public void refreshInteract() {
        // gets the closest object from the player to show the interact key
        Interactive closest = interactives.get(0);
        float minDistance = closest.getDistance(OnlineGameScreen.player);
        for (Interactive interactive : this.interactives) {
            float distance = interactive.getDistance(OnlineGameScreen.player);
            if (distance < minDistance) {
                minDistance = distance;
                closest = interactive;
            }
        }

        for (Interactive interactive : this.interactives) {
            interactive.refreshInteract(OnlineGameScreen.player, interactive == closest);
        }
    }

    /**
     * Draws the map.
     *
     * @param batch the sprite batch used for drawing
     */
    public void drawMap(SpriteBatch batch) {
        for (Floor f : this.floors) {
            batch.draw(FLOOR_TEXTURES[f.textureIndex], f.pos.x, f.pos.y);
        }

        for (Wall w : this.walls) {
            batch.draw(WALL_TEXTURES.get(w.assetPath), w.pos.x * 16, w.pos.y * 16);
        }

        if (this.stairs != null) {
            this.stairs.draw(STAIRS_TEXTURE, batch);
        }

        for (Chest chest : chests) {
            chest.draw(batch);
        }

        // don't draw monsters while editiing the monsters list
        this.drawingMonsters = true;
        ArrayList<Monster> monsterstmp = new ArrayList<>(Map.monsters);
        int size = monsterstmp.size();
        for (int i = 0; i < size; i++) {
            Monster monster = monsterstmp.get(i);
            monster.draw(batch);
        }
        this.drawingMonsters = false;
    }

    /**
     * Returns the player spawn point.
     *
     * @return the spawn point
     */
    public Point getPlayerSpawn() {
        return this.rooms.get(0).getCenterOutOfMap();
    }

    /**
     * Checks if points are on the floor.
     *
     * @param points the points to check
     * @return true if all points are on the floor, false otherwise
     */
    public boolean arePointsOnFloor(Point[] points) {
        boolean[] areIn = new boolean[points.length];
        Arrays.fill(areIn, false);

        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            for (Floor floor : this.floors) {
                Point p1 = floor.pos;
                Point p2 = floor.pos.add(Floor.TEXTURE_WIDTH, Floor.TEXTURE_HEIGHT);
                if (point.isPointIn(p1, p2)) {
                    areIn[i] = true;
                }
            }
        }

        for (boolean isIn : areIn) {
            if (!isIn) {
                return false;
            }
        }

        return true;
    }
}
