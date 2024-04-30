package fr.studiokakou.kakouquest.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.studiokakou.kakouquest.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * The Map class represents a game map.
 * This class is used to create a Map object.
 *
 * @version 1.0
 */
public class Map {
    /**
     * The floors of the map. It is a list of floors.
     */
    public ArrayList<Floor> floors = new ArrayList<>();

    public int map_height;
    /**
     * The width of the map.
     */
    public int map_width;
    /**
     * The list of rooms.
     */
    ArrayList<Room> rooms =  new ArrayList<>();
    /**
     * The list of bridges.
     */
    ArrayList<Bridge> bridges = new ArrayList<>();
    /**
     * The list of walls.
     */
    ArrayList<Wall> walls = new ArrayList<>();
    /**
     * The minimum height of a room.
     */
    public static int ROOM_MIN_HEIGHT=7;
    /**
     * The minimum width of a room.
     */
    public static int ROOM_MIN_WIDTH=7;
    /**
     * The maximum height of a room.
     */
    public static int ROOM_MAX_HEIGHT=21;
    /**
     * The maximum width of a room.
     */
    public static int ROOM_MAX_WIDTH=21;
    /**
     * A hash table storing distances.
     */
    public Hashtable<Float, Object> distances = new Hashtable<>();


    /**
     * Constructs a Map object.
     * This constructor is used to create a Map object.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public Map(int width, int height){
        this.map_height = height;
        this.map_width = width;

        this.initMap();
    }

    /**
     * Initializes the map.
     * This method is used to initialize the map.
     * It generates rooms and floors.
     *
     * @see Map#generateRooms()
     * @see Map#genFloors()
     */
    public void initMap(){

        generateRooms();

        this.sortRooms();

        generateBridges();

        this.genFloors();

        this.genWalls();

        this.getRealSize();
    }

    /**
     * Adjusts the position of floors.
     */
    public void getRealSize(){
        for (Floor f : this.floors){
            f.pos = f.pos.mult(Floor.TEXTURE_WIDTH);
        }
    }

    /**
     * Generates walls.
     */
    public void genWalls(){
        for (Floor f : this.floors){
            ArrayList<Wall> surroundWalls = f.getSurrounding(this.floors);
            this.walls.addAll(surroundWalls);
        }

        for (Bridge b : this.bridges){
            ArrayList<Wall> toAddWalls = b.genBridgeWall(this.rooms, this.bridges);
            this.walls.addAll(toAddWalls);
        }
    }

    /**
     * Draws the map.
     *
     * @param batch the sprite batch
     */
    public void drawMap(SpriteBatch batch){
        for (Floor f : this.floors){
            batch.draw(f.texture, f.pos.x, f.pos.y);
        }

        for (Wall w : this.walls){
            w.draw(batch);
        }
    }

    /**
     * Generates rooms.
     */
    public void generateRooms(){
        for (int i = 0; i < 30; i++) {
            int startX = Utils.randint(0, this.map_width-Map.ROOM_MAX_WIDTH);
            int startY = Utils.randint(0, this.map_height-Map.ROOM_MAX_HEIGHT);
            int endX = startX+Utils.randint(Map.ROOM_MIN_WIDTH,Map.ROOM_MAX_WIDTH);
            int endY = startY+Utils.randint(Map.ROOM_MIN_HEIGHT,Map.ROOM_MAX_HEIGHT);
            Room r = new Room(startX, startY, endX, endY, false);
            if (! r.isColliding(this.rooms)){
                this.rooms.add(r);
            }
        }
    }

    /**
     * Generates bridges between rooms.
     */
    public void generateBridges(){
        if (this.rooms.size()==1){
            return;
        }
        for (int i = 0; i < this.rooms.size() - 1; i++) {
            this.bridges.add(new Bridge(this.rooms.get(i), this.rooms.get(i+1), this.rooms));
        }
    }

    /**
     * Generates floors.
     */
    public void genFloors(){
        for (Room r : this.rooms){
            for (int i = (int) r.start.x ; i < r.end.x ; i++) {
                for (int j = (int) r.start.y; j < r.end.y; j++) {
                    this.floors.add(new Floor(i, j));
                }
            }
        }
        for (Bridge b : this.bridges){
            for (Point p : b.points){
                this.floors.add(new Floor(p.x, p.y));
            }
        }
    }

    /**
     * Returns the player spawn point.
     *
     * @return the spawn point
     */
    public Point getPlayerSpawn(){
        return this.rooms.get(0).getCenterOutOfMap();
    }





    /**
     * Sorts rooms by distance.
     */
    public void sortRooms(){
        ArrayList<Room> sortedRooms = new ArrayList<>();
        sortedRooms.add(this.rooms.get(0));
        this.rooms.remove(0);

        while (this.rooms.size()>1){
            Room toAdd = sortedRooms.get(sortedRooms.size()-1).getNearestRoom(this.rooms);
            sortedRooms.add(toAdd);
            this.rooms.remove(toAdd);
        }

        sortedRooms.add(this.rooms.get(0));
        this.rooms.clear();

        this.rooms = sortedRooms;
    }

    /**
     * Checks if points are on floor.
     *
     * @param points the points to check
     * @return true if all points are on floor, false otherwise
     */
    public boolean arePointsOnFloor(Point[] points){
        boolean[] areIn = new boolean[points.length];
        Arrays.fill(areIn, false);

        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            for (Floor floor : this.floors) {
                Point p1 = floor.pos;
                Point p2 = floor.pos.add(Floor.TEXTURE_WIDTH, Floor.TEXTURE_HEIGHT);
                if (point.isPointIn(p1, p2)) {
                    areIn[i]=true;
                }
            }
        }

        for (boolean isIn : areIn){
            if (!isIn){
                return false;
            }
        }

        return true;
    }

    /**
     * Disposes resources.
     */
    public void dispose(){
        for (Floor f : this.floors){
            f.texture.dispose();
        }
    }
}
